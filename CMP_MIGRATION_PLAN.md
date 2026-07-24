# Plan: Android Compose → Compose Multiplatform (Android + iOS, Koin DI)

## Current state (assessed)

| Area | Current | CMP verdict |
|---|---|---|
| `core-model` (8 files) | Pure Kotlin, no Android | Move to `commonMain` as-is |
| `core-common` | Dispatchers, `Result` + Hilt `@Module` | Logic common; drop Hilt module |
| `core-designsystem` | Compose theme, icons | Mostly common; dynamic-color = `expect/actual` |
| `core-domain` (use cases) | Plain classes + `@Inject` | Common; swap DI |
| `core-data` | Repos + Hilt + **DataStore(Context)** | Common logic; DataStore factory `expect/actual` |
| `core-database` | **Room 2.8.4**, converters | Room *is* KMP — needs driver + builder rewrite |
| `feature:history/about/settings` | Compose + `hiltViewModel` | Common UI; ViewModel via `koinViewModel` |
| `feature:timer` | Compose + **WorkManager + foreground Service + notifications + BroadcastReceiver** | Hardest. Background timer fully Android → `expect/actual` |
| Navigation | **navigation3** (`NavKey`/`NavDisplay`) | **Confirmed KMP-ready** — see Phase 0 findings |
| DI | **Hilt everywhere** (18 files) | Rip out → Koin |
| Firebase | Crashlytics/Perf (convention plugin) | Keep Android-only; iOS optional later |
| `build-logic` (13 convention plugins) | All Android-only | Rewrite for KMP |
| Resources | `strings.xml` x2, 11 drawables | Move to `commonMain/composeResources` |

**Good news:** Room 2.8.4, DataStore 1.2.1, Lifecycle 2.11, Kotlin 2.3, coroutines — all already KMP-capable versions. Nav is already model-based (serializable `NavKey`), which ports cleanly. Domain core is Android-free.

**Hard blockers:** Hilt (no KMP), timer background service (Android-only APIs).

---

## Phase 0 findings (spike complete, 2026-07-24)

### Navigation 3 — confirmed KMP-ready
- **Compose Multiplatform 1.10+ required** (released Jan 2026) — supports Navigation 3 on Android, iOS, desktop, web.
- Dependency coordinates change for the UI artifact: `org.jetbrains.androidx.navigation3:navigation3-ui:1.1.1` (JetBrains-published multiplatform build) replaces `androidx.navigation3:navigation3-ui`. `navigation3-runtime` (the `NavKey`/`EntryProviderScope` bits current code uses) is transitively covered.
- **Caveat — must fix before iOS works**: Android's `NavKey` serialization relies on JVM reflection, unavailable on iOS/web. Must switch every `rememberNavBackStack()` call to pass an explicit `SavedStateConfiguration` with a `polymorphic(NavKey::class) { subclass(...) }` registration for every route (`TimerRoute`, `HistoryRoute`, `AboutRoute`, `SettingsRoute`, etc). Without this, nav only works on Android. This is a required Phase 5 task, not optional.
- Decision: **keep navigation3**, no need to swap to a different nav library.

### Room 2.8.4 — confirmed KMP-ready, exact recipe
- Deps: `androidx.room:room-runtime:2.8.4`, `androidx.sqlite:sqlite-bundled:2.7.0` (commonMain), `androidx.room:room-compiler:2.8.4` via KSP for every target (`kspAndroid`, `kspIosArm64`, `kspIosSimulatorArm64`, `kspIosX64`).
- Pattern: `@Database` class stays in commonMain annotated `@ConstructedBy(AppDatabaseConstructor::class)`; add `expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>` — Room's KSP compiler generates the `actual` for each target, no manual platform code needed there.
- Platform code needed is just the **builder factory**: `androidMain` passes `Room.databaseBuilder<AppDatabase>(context, name = dbFile.absolutePath)`; `iosMain` passes `Room.databaseBuilder<AppDatabase>(name = documentDirectoryPath + "/app.db")` (via `NSFileManager.defaultManager.URLForDirectory(NSDocumentDirectory, ...)`). Common code finishes both with `.setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO)`.
- Matches Phase 4 plan as written — no changes needed there beyond using these exact artifact names.
- Note: confirm `libs.versions.toml`'s current `ksp = "2.3.6"` is compatible with Kotlin 2.3.0 + Room 2.8.4 compiler before wiring — verify during Phase 4, not blocking Phase 0.

### iOS framework packaging — decision made
- **Direct `binaries.framework` export**, no CocoaPods. Nothing in the current dependency graph needs a native iOS pod (Firebase iOS is explicitly deferred per Phase 7). Direct export avoids adding CocoaPods/Xcode-plugin toil for a benefit we don't need yet. Revisit only if/when Firebase-on-iOS is picked up.

### Net effect on plan
- Phase 1 must pin **Compose Multiplatform >= 1.10** in the version catalog (bumps `androidxComposeBom`-adjacent tooling — check compatibility with Kotlin 2.3.0, already satisfied per JetBrains blog).
- Phase 5's navigation step (item 18) is now concrete: swap dependency coordinates + add `SavedStateConfiguration`/polymorphic serialization wiring for all `NavKey` routes. No library swap required.
- Risk list below updated to drop "nav3 KMP availability" as resolved.

---

## Target end-state

Each library module becomes KMP (`kotlin("multiplatform")` + `com.android.library` + `org.jetbrains.compose`) with `commonMain / androidMain / iosMain` source sets. `:app` stays the Android application. Add an `iosApp` Xcode project + a shared root Composable so both platforms mount the same UI.

```
commonMain  → all domain, UI, repos, use-cases, Koin modules, Compose screens
androidMain → Room builder(context), DataStore(filesDir), WorkManager timer, notifications, Firebase, dynamic color
iosMain     → Room builder(NSDocumentDir), DataStore(path), NSTimer/BGTask timer, UNUserNotification
```

---

## Phased plan (ordering matters — each phase compiles before the next)

### Phase 0 — Prep & spike (de-risk first) — DONE
1. ~~Branch `feat/cmp-migration`.~~ Done. Bump to Compose Multiplatform **1.10+** (confirmed compatible with Kotlin 2.3.0) + Compose Hot Reload; add JetBrains Compose plugin to catalog.
2. ~~Spike the two unknowns~~ Done — see "Phase 0 findings" above. nav3: confirmed KMP-ready via CMP 1.10+, dependency coordinate swap + polymorphic serialization needed for iOS. Room: confirmed KMP-ready, exact builder/KSP recipe captured.
3. ~~Decide iOS framework packaging~~ Done — **direct `binaries.framework` export**, no CocoaPods (nothing needs a native pod while Firebase-iOS stays deferred).

### Phase 1 — Convention plugins & build (foundation) — DONE (2026-07-24)
4. ~~Rewrite `build-logic`~~ Done. Added three new precompiled convention plugins in `build-logic/convention/src/main/kotlin/`:
   - `KotlinMultiplatformConventionPlugin` (id `foursixmethod.kmp.library`) — applies `org.jetbrains.kotlin.multiplatform` + `com.android.kotlin.multiplatform.library`, declares `iosX64/iosArm64/iosSimulatorArm64`, sets `compileSdk=37/minSdk=23/jvmTarget=17` on the Android target, wires `commonTest` → `kotlin("test")`.
   - `KotlinMultiplatformFeatureConventionPlugin` (id `foursixmethod.kmp.feature`) — layers Compose Multiplatform + `kotlin.plugin.compose` + `kotlin.plugin.serialization` on top, wires `commonMain` project deps (`core-model/-designsystem/-data/-database/-common/-domain`) + Compose runtime/foundation/material3/ui/resources + coroutines/serialization/lifecycle-compose; `androidMain` gets Coil.
   - `KmpRoomConventionPlugin` (id `foursixmethod.kmp.room`) — applies KSP + the `androidx.room` gradle plugin, `commonMain` gets `room-runtime` + `sqlite-bundled`, KSP room-compiler wired for `kspAndroid`/`kspIosX64`/`kspIosArm64`/`kspIosSimulatorArm64`.
   - Catalog additions: `composeMultiplatform = "1.11.1"` version + `compose-multiplatform`/`kotlin-multiplatform`/`android-kotlin-multiplatform-library`/`androidx-room` plugin aliases, `sqlite-bundled`/`room-gradlePlugin`/`kotlinx-coroutines-core` libraries. Root `build.gradle.kts` registers the 4 new plugin ids `apply false` (required so `pluginManager.apply(id)` inside the convention classes can resolve them). None of this touches any existing module yet — `:app` and all current modules are untouched and still build.
   - Validated: `./gradlew :build-logic:convention:compileKotlin` and `./gradlew help` both green (only pre-existing/unrelated deprecation warnings).
5. ~~Add iOS targets~~ Done as part of item 4 (all three iOS targets declared in the base KMP plugin). Compose Multiplatform resources config deferred to Phase 5 when actual `strings.xml`/drawables move to `commonMain/composeResources` — no module has resources to migrate yet.

**Two AGP-9/KGP-2.3 API gotchas found while implementing (worth knowing before Phase 3+ touches real modules):**
- **AGP 9 requires the new single-variant plugin.** `com.android.library` and Kotlin Multiplatform can no longer apply to the same module on AGP 9.x — you must use `com.android.kotlin.multiplatform.library` instead (confirmed via JetBrains' AGP-9 migration blog). This plugin is single-variant (no debug/release build types) — fine for library/feature modules, irrelevant to `:app` which stays a classic Android application module.
- **The `kotlin { android { ... } }` DSL sugar only exists in `.gradle.kts` scripts.** AGP registers its Android KMP target as a dynamically-added Gradle extension (`ExtensionContainer.add(KotlinMultiplatformAndroidLibraryTarget::class, "android", instance)`), and Gradle only generates the nice `android { }` accessor for literal `.gradle.kts` files via its per-script type-safe-accessor feature. Hand-written `Plugin<Project>` classes (like every convention plugin in this repo) must instead do `extensions.configure<KotlinMultiplatformExtension> { configure<KotlinMultiplatformAndroidLibraryTarget> { ... } }` (the generic `Any.configure<T>` helper, which casts to `ExtensionAware` internally). Source-set accessors (`sourceSets.commonMain`, `.androidMain`, `.iosMain`, etc.) are unaffected — those are genuine Kotlin interface-scoped extension properties (`KotlinMultiplatformSourceSetConventions`) mixed into `KotlinMultiplatformExtension` itself, so they resolve normally with no import needed, confirmed by the compiler accepting them with zero complaints on the first pass.

### Phase 2 — DI swap: Hilt → Koin (unblocks everything) — DONE (2026-07-24)
6. ~~Add Koin~~ Done — `koin-bom` (4.1.1) + `koin-core`, `koin-android`, `koin-androidx-workmanager`, `koin-compose-viewmodel`. Kept Android-only, single-target, per plan (no KMP conversion in this phase).
7. ~~Replace Hilt modules~~ Done across all 18 flagged files plus build config: `core-common` (`FsmDispatchers` loses its `@Qualifier` annotation class — Koin uses `named(FsmDispatchers.IO.name)` instead — `DispatcherModule.kt` → `dispatcherModule`), `core-data` (`RepositoryModule.kt` → `repositoryModule`, both repos lose `@Inject`/`@Singleton`/`@ApplicationContext`), `core-database` (`DaosModule.kt`+`PersistenceModule.kt` merged into one `databaseModule` in a new `DatabaseModule.kt`), `core-domain` (new `di/DomainModule.kt` using `factoryOf(::UseCase)`). `@Dispatcher(FsmDispatchers.IO)` qualifier annotation → plain constructor param, qualified at the `single { }` call site instead.
8. ~~ViewModel + Application wiring~~ Done. `hiltViewModel()` → `koinViewModel()` (from `org.koin.compose.viewmodel`, the KMP-first artifact — chosen over `koin-androidx-compose` since Phase 5+ goes multiplatform anyway) in `HistoryScreen`/`SettingsScreen`/`TimerScreen`/`MainActivity`. Each feature got its own Koin module (`historyModule`, `settingsModule`, `timerModule`, `appModule`) using `viewModelOf(::ViewModel)`. `FlowSixApplication` → `startKoin { androidContext(this) workManagerFactory() modules(...) }`; kept `Configuration.Provider` (plain Android API, not Hilt-specific) rather than trusting `workManagerFactory()` to fully replace it, since the manifest already disables the default `WorkManagerInitializer` and relies on `Configuration.Provider`'s on-demand-init fallback — pulling the Koin-registered `WorkerFactory` via `get<WorkerFactory>()` preserves that exact working mechanism with the smallest possible diff.
9. ~~Keep Android-only~~ Confirmed — no module went KMP in this phase.

**Timer feature specifics (the trickiest part — WorkManager + foreground service + BroadcastReceiver):**
- `TimerWorker` (`@HiltWorker`/`@AssistedInject`) → plain constructor, registered via Koin's `worker { params -> TimerWorker(params.get(), params.get(), get(), get()) }` DSL (`org.koin.androidx.workmanager.dsl.worker`).
- `TimerActionReceiver` (was `@AndroidEntryPoint` + field `@Inject`) → Koin has no field-injection equivalent for framework components Android instantiates by reflection (BroadcastReceiver), so it implements `KoinComponent` directly and uses `by inject()` — resolves fine since `startKoin` always runs in `Application.onCreate` before any broadcast can fire.
- `TimerController`/`TimerSessionRepository` → `singleOf(::Class)` (Koin's constructor-reference DSL; no annotations needed, each param resolved by type via `get()`).

**Verification:** No device/emulator available in this environment (no `ANDROID_HOME`/`emulator` binary), so the Koin dependency graph could not be runtime-verified by actually launching the app — Koin resolves its graph lazily at DI-call time, so a missing registration would only surface then, not at compile time. Compensated with (a) full green builds — `:app:assembleDemoDebug`, `:app:assembleProdDebug`, `testDemoDebugUnitTest` all `BUILD SUCCESSFUL`, and (b) a manual trace of every constructor parameter across all 8 Koin modules against what's registered in `FlowSixApplication`'s `modules(...)` list, confirming every dependency resolves to exactly one provider. Runtime confirmation on a real device/emulator is still recommended before merging.

**Unrelated pre-existing bug fixed to unblock verification:** `core-database/util/Converters.kt` was missing the `import kotlinx.serialization.decodeFromString` (only had `encodeToString`), so `Json.decodeFromString<List<StepEntity>>(value)` resolved to the wrong overload and failed to compile — confirmed identical on `main`, so this predates the CMP work entirely and was presumably never caught because no one had run a full `assemble` recently. Fixed with a one-line import addition since it blocked validating this phase's actual changes.

### Phase 3 — Move platform-neutral code to commonMain — DONE (2026-07-24, merged with Phase 4)
10. ~~`core-model` → `commonMain`~~ Done — **not verbatim**: `Recipe.createAt` `java.time.LocalDate` → `kotlinx.datetime.LocalDate` (+ `currentDate()` helper via `Clock.System.todayIn(TimeZone.currentSystemDefault())`); `Step.getWaterWithScale` `BigDecimal.setScale(_, RoundingMode.UP)` → multiplatform `formatScaleUp()` (ceil-away-from-zero + fixed decimals, exact match for non-negative values). Module now `foursixmethod.kmp.library`.
11. ~~`core-common` `Result`/dispatchers → `commonMain`~~ Done. Coroutines bumped **1.6.4 → 1.10.2**. `Result` and `Dispatchers.Default` are common-clean. `Dispatchers.IO` has **no common declaration** (separate actual on JVM and Native) → introduced `expect val ioDispatcher` (androidMain = `Dispatchers.IO`; iosMain = `Dispatchers.Default`, since **`Dispatchers.IO` is `internal` on Kotlin/Native**). `Number.roundTo` `String.format` → `kotlin.math.round`/`pow`.
12. ~~`core-domain` use cases → `commonMain`~~ Done — pure move, no code change (had to happen with Phase 4: a KMP module's `commonMain` cannot consume the still-Android `core-data`, so domain could only move once data did).

### Phase 4 — Data layer KMP — DONE (2026-07-24)
13. ~~**Room** KMP~~ Done. `@ConstructedBy(AppDatabaseConstructor::class)` + `expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase>` (KSP generates the actual per target). Platform builder split into `expect val platformDatabaseModule` (androidMain: `Room.databaseBuilder<AppDatabase>(context, getDatabasePath("recipe-database").absolutePath)`; iosMain: `NSDocumentDirectory + "/recipe-database.db"`), finalized in common `databaseModule` with `.setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(ioDispatcher).fallbackToDestructiveMigration(true)`. `LocalDateConverter` → `kotlinx.datetime.LocalDate` (`toString()`/`parse()` are ISO-8601, byte-compatible with the old `DateTimeFormatter.ISO_LOCAL_DATE` data). Room enum columns (`Balance`/`Level`/`State`) still handled natively by Room — no converters added. **Verified: `kspAndroidMain` + `kspKotlinIosSimulatorArm64` + `kspKotlinIosArm64` all generate + compile.** `ksp = 2.3.6` is compatible with Kotlin 2.3.0 + Room 2.8.4 (risk #6 resolved).
14. ~~**DataStore** KMP~~ Done. `datastore-preferences-core` (multiplatform) + `okio`; common `createUserSettingsDataStore(producePath)` via `PreferenceDataStoreFactory.createWithPath`; `expect val platformDataModule` supplies the path (androidMain = `filesDir/datastore/user_settings.preferences_pb`, **matching the old `preferencesDataStore(name="user_settings")` location so existing data survives**; iosMain = `NSDocumentDirectory`). `DataStoreUserSettingsRepository` now takes `DataStore<Preferences>` instead of `Context`.
15. ~~Repos → `commonMain`~~ Done. `OfflineRecipeRepository` (`LocalDate.now()` → `currentDate()`) and `DataStoreUserSettingsRepository` both in commonMain; interfaces + mappers (`RecipeExt`/`StepExt`) moved as-is. `repositoryModule` stays common; `FlowSixApplication.modules(...)` now also loads `platformDataModule` + `platformDatabaseModule`.

**Gotchas found while implementing (worth knowing before Phase 5+):**
- **`platform(bom)` is now a hard error inside a KMP `sourceSet.dependencies { }`** (deprecation elevated to error, KT-58759). Use `implementation(project.dependencies.platform(libs.koin.bom))`.
- **`Dispatchers.IO` is unusable from `commonMain`** (no common decl) **and `internal` on Kotlin/Native.** Hence the `ioDispatcher` expect/actual with `Dispatchers.Default` on iOS.
- **`iosX64` target dropped.** `androidx.sqlite:sqlite-bundled:2.7.0` publishes no `ios_x64` variant; modern KMP libs target only `iosArm64` (device) + `iosSimulatorArm64` (Apple-Silicon sim). Removed from both `KotlinMultiplatformConventionPlugin` and `KmpRoomConventionPlugin` KSP wiring.
- **Android namespace derived in the convention plugin** from the Gradle path (`:core-model` → `com.yopachara.fourtosixmethod.core.model`) so no module script sets it; `core-common`'s namespace normalizes from the old `...foursixmethod.core.common` (resource-less lib, safe).
- **kotlinx-datetime 0.6.2 format API**: no `LocalDate.format(fmt)` member — it's `fmt.format(date)`; the literal-string builder function is `char(Char)` only (no `chars(String)`); `toEpochDays()`/`fromEpochDays()` are `Int`. `HistoryItem`'s month name is now `MonthNames.ENGLISH_ABBREVIATED` (was device-locale `MMM`) — minor product-visible change.

**Verification (this phase):** No device/emulator, but compiler coverage is broad and green — `:app:assembleDemoDebug` + `:app:assembleProdDebug` (full Android graph through all 5 now-KMP modules), `:app:testDemoDebugUnitTest`, and for every core module: `compileCommonMainKotlinMetadata` + `compileAndroidMain` + `compileKotlinIosSimulatorArm64`, plus `compileKotlinIosArm64` (device) for the Room modules. iOS **runtime** (actual DB/DataStore I/O on a simulator) still unproven — needs the Phase 7 iOS shell. Koin graph still lazily resolved (see Phase 2 note); the two new platform modules were traced against `FlowSixApplication.modules(...)`.

### Phase 5 — UI to commonMain
16. Move `core-designsystem`, `feature:history/about/settings` screens + ViewModels to `commonMain`. Dynamic color = `expect fun dynamicColorScheme()` (Android: Material You; iOS: return null → fall back to brand palette). `AboutScreen` Context usage (open URL/version) → `expect` opener + `BuildKonfig`/expect version.
17. Resources: move `strings.xml` + drawables into `commonMain/composeResources`, regenerate `Res.*` accessors, replace `stringResource`/`painterResource` imports with Compose Multiplatform resources.
18. Navigation: apply Phase-0 decision (port nav3 to common, or swap to CMP navigation).

### Phase 6 — Timer feature (hardest, do last)
19. Split `TimerViewModel` tick/domain logic into `commonMain` (it's already a coroutine `Flow` countdown — fully portable).
20. Abstract the background/notification side behind `expect interface TimerController` + `TimerNotifications`:
    - **androidMain**: keep existing WorkManager + foreground `Service` + `NotificationManagerCompat` + `BroadcastReceiver` implementation unchanged.
    - **iosMain**: `UNUserNotificationCenter` for pour-step alerts; background countdown via scheduled local notifications (iOS won't run a live foreground service — schedule notifications at step boundaries + optional `BGTaskScheduler`). Accept reduced background fidelity vs Android and document it.
21. `TimerSessionRepository` state → common.

### Phase 7 — iOS app shell & cutover
22. Add `iosApp` Xcode project, `MainViewController` calling shared root Composable + `initKoin()`.
23. Firebase: keep Android via existing convention plugin; iOS Crashlytics = separate (GitLive SDK or native pod) — defer unless required.
24. Smoke-test both platforms; wire minimal instrumented/common tests (current tests are stubs, so no regression suite to preserve).

---

## Top risks

1. **navigation3 non-JVM serialization** — resolved as KMP-ready in Phase 0, but every `NavKey` route needs explicit `SavedStateConfiguration` + polymorphic serializer registration for iOS, or nav silently only works on Android. Concrete Phase 5 task now, not a library-swap risk.
2. **iOS background timer fidelity** — iOS has no WorkManager/foreground-service equivalent; the live pour timer degrades to scheduled local notifications when backgrounded. Product-visible; confirm acceptable.
3. ~~**coroutines 1.6.4**~~ RESOLVED (Ph3) — bumped to 1.10.2; `Dispatchers.IO` handled via `ioDispatcher` expect/actual (Native has no public IO dispatcher → `Dispatchers.Default` on iOS).
4. ~~**`java.time.LocalDate` in Room converter**~~ RESOLVED (Ph3/4) — swapped to `kotlinx-datetime`; ISO string form keeps stored data compatible.
5. ~~**Hilt removal blast radius**~~ RESOLVED (Ph2).
6. ~~**KSP/Kotlin version compat**~~ RESOLVED (Ph4) — `ksp 2.3.6` + Room 2.8.4 + Kotlin 2.3.0 generate & compile on android + iosArm64 + iosSimulatorArm64.

## Effort (rough)

- DI swap (Ph2): ~2-3 days
- Room+DataStore KMP (Ph4): ~2 days
- UI+resources (Ph5): ~2-3 days
- Timer expect/actual + iOS (Ph6-7): ~3-5 days
- Build-logic/plugins (Ph1): ~1-2 days
- Spikes/buffer: ~2 days
- **Total: ~2-3 weeks** for a working Android+iOS build; timer iOS polish extends it.

---

## Entry point

Recommended: **Phase 0 spike** (verify nav3 KMP + Room native build) since both gate the rest — do in a throwaway branch without touching the working app.

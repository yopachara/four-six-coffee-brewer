# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Compose Multiplatform app (Android + iOS) implementing Tetsu Kasuya's 4:6 coffee brewing method — a step-by-step pour-over timer where users set weight, ratio, and taste balance (Body/Sweet/Acidity, Basic/Strong/Weak), then follow a generated pour schedule with a countdown timer. History of past brews is stored locally and charted.

Multi-module Gradle/Kotlin project, originally structured after Google's Now in Android (NiA) sample; some NiA-derived naming survives (`FlowSixApp`, `FSMBuildType`, `PrintApkLocationTask`, and the `com.yopachara.fourtosixmethod` package).

**Migration status:** the port from Android-only Compose + Hilt to Compose Multiplatform + Koin is complete (all phases, see `CMP_MIGRATION_PLAN.md` for the full history and the gotchas hit along the way). **iOS has never actually been run** — the dev machine has the Xcode command line tools but no full Xcode, so `linkDebugFrameworkIosSimulatorArm64` fails and everything iOS-side is compile-verified only. Treat iOS runtime behaviour as unproven.

## Build & test commands

```
./gradlew :app:assembleDemoDebug        # build one Android flavor/build-type variant
./gradlew assembleDebug                 # build demo+prod debug APKs
./gradlew testAndroidHostTest           # run commonTest across all KMP modules
./gradlew :core-model:testAndroidHostTest   # ...for one module
./gradlew lint
```

Test-task naming is easy to get wrong here:

- **`./gradlew test` only covers `:app`.** Every other module is a KMP library whose JVM test task is `testAndroidHostTest`, which `test` does not aggregate. Use `testAndroidHostTest` to run the real suite.
- Host tests exist at all only because the base KMP convention plugin calls `withHostTest { }`. Without it Gradle prints a warning and registers no task, leaving `commonTest` silently unrunnable.
- `commonTest` lives in `core-model`, `feature:timer`, and `shared` (13 tests). Running the iOS equivalents (`iosSimulatorArm64Test`) needs a simulator.

iOS:

```
./gradlew :shared:compileKotlinIosSimulatorArm64      # works today
./gradlew :shared:linkDebugFrameworkIosSimulatorArm64 # needs full Xcode
open iosApp/iosApp.xcodeproj                          # needs full Xcode
```

Product flavors: `demo` (applicationId suffix `.demo`) and `prod`, each cross-built with `debug`/`release`. (`FSMBuildType` also declares `BENCHMARK`, but no build type or `:benchmarks` module actually exists — it is dead code.) Firebase Crashlytics/Performance are wired only through the app's Firebase convention plugin — Android only; Firebase-on-iOS is deliberately deferred.

## Architecture

Every module except `:app` is Kotlin Multiplatform with `commonMain` / `androidMain` / `iosMain` source sets, targeting Android + `iosArm64` + `iosSimulatorArm64`. `iosX64` is intentionally absent (`androidx.sqlite:sqlite-bundled` publishes no `ios_x64` variant).

The rule of thumb: **code goes in `commonMain` unless it touches a platform API**, and platform pieces are reached either through `expect`/`actual` or through a Koin module supplied per platform (`expect val platformDataModule` and friends — see DI below).

### Module graph

- `app` — Android application module, and *only* the Android entry point: `MainActivity` (mounts `FlowSixRoot()`) and `FlowSixApplication` (calls `initKoin { androidContext(...); workManagerFactory() }`). Depends on `:shared` alone.
- `shared` — everything both platforms share above the feature layer: `FlowSixRoot` (theme + `AppViewModel`), `FlowSixApp`/`FlowSixAppState` (bottom nav + state holder), the `NavHost`, `NavigationState`/`Navigator`/`TopLevelDestination`, `initKoin()`, and the bottom-nav label strings as composeResources. `iosMain` adds `MainViewController()` (`ComposeUIViewController { FlowSixRoot() }`) and `initKoinIos()`, and the iOS targets export a static `Shared` framework.
- `iosApp` — hand-written Xcode project; one SwiftUI view wrapping `MainViewController()`. Never opened by Xcode (see migration status above).
- `feature:timer`, `feature:history`, `feature:about`, `feature:settings` — one Gradle module per screen. Each exposes a `xxxScreen()`/`xxxRoute()` Compose entry point plus a `navigation/` package (route `NavKey` + a `EntryProviderScope.xxxScreen()` extension), all in `commonMain`.
- `core-model` — pure domain types: `Recipe`, `Step`, `State` (First..Sixth pour stages), `Level` (Basic/Strong/Week), `Balance` (Acid/Basic/Sweet). Uses `kotlinx.datetime.LocalDate`, and `Float.scaleUp`/`formatScaleUp` in place of `BigDecimal.setScale(_, UP)`.
- `core-data` — `RecipeRepository`/`UserSettingsRepository` interfaces + `OfflineRecipeRepository` (Room) and `DataStoreUserSettingsRepository` impls, entity↔domain mappers (`RecipeExt.kt`, `StepExt.kt`), `repositoryModule`. `expect val platformDataModule` supplies the DataStore path.
- `core-database` — Room `AppDatabase`, `RecipeDao`, entities, converters, `databaseModule`. `expect val platformDatabaseModule` supplies the platform builder.
- `core-domain` — use cases (`InsertRecipeUseCase`, `GetRecipeHistoryListUseCase`) between features and `core-data`.
- `core-common` — `FsmDispatchers` + `dispatcherModule`, `expect val ioDispatcher`, `Result` sealed wrapper, small numeric utils. Note the package is `...core.network` for historical reasons, not `...core.common`.
- `core-designsystem` — theme (`Color`/`Shape`/`Theme`/`Type`), `FlowSixIcons`, and the shared drawables as composeResources. `expect fun SystemBarsAppearance(darkTheme)` is the only platform bit.
- `build-logic/convention` — included build providing convention plugins applied by id. KMP: `foursixmethod.kmp.library` (base: Android + iOS targets, namespace derived from the Gradle path, host tests), `foursixmethod.kmp.feature` (adds Compose Multiplatform, Koin, navigation3 and the shared project deps), `foursixmethod.kmp.room`. Android-only, used by `:app`: `foursixmethod.android.application{,.compose,.flavors,.firebase}`. The `foursixmethod.android.library`/`.feature`/`.room`/`.test`/`*.jacoco` plugins are leftovers from before the migration and no module applies them.

### Recipe/timer domain logic

`Recipe` (core-model) is the central computed model: setting `ratio`, `coffeeWeight`, `balance`, `level`, `isIcedDrip`, or `hotRatio` triggers `generateSteps()`, which recomputes `steps: List<Step>` via `computeStep()` (`Step.kt`) for each pour stage. Water percentage per step comes from `Balance.sweetIndex/acidIndex` (stages 1–2, controls sweetness/acidity) and `Level.firstIndex` (stages 3+, controls body/strength); stage durations come from `getStateTotalTime()`, which varies by `Level`. `State` enumerates the 4–6 pour stages depending on `Level` (`getTotalState()`).

The countdown itself lives in **`TimerEngine`** (`feature:timer`, `commonMain`): it owns the ticker flow, the per-tick session-state update, and the save-on-completion via `InsertRecipeUseCase`, and takes an `onTick` callback for the one platform-specific step. Cancelling the coroutine running `TimerEngine.run` throws out of the collect loop, so the completion tail (reset + save) runs only on a natural finish — that is how "stop without saving" works.

Background behaviour sits behind `interface TimerController` (`commonMain`) with a per-platform implementation:

- `AndroidTimerController` — WorkManager + a foreground worker + `NotificationManagerCompat` + `TimerActionReceiver`. Keeps a live countdown notification.
- `IosTimerController` — runs the engine in an app-scoped `Dispatchers.Default` scope and pre-schedules one `UNTimeIntervalNotificationTrigger` per remaining pour boundary. **Known fidelity gap:** while iOS suspends the app the on-screen readout freezes and re-syncs on resume; the pour alerts still fire on time.

### DI

Koin throughout (no Hilt anywhere). `initKoin(appDeclaration)` in `:shared` starts the whole graph; each platform passes only what the shared graph cannot express. Modules bind repositories (`repositoryModule`), Room DB/DAO (`databaseModule`), use cases (`domainModule`), each feature's ViewModels, and qualified dispatchers (`dispatcherModule`, `named(FsmDispatchers.IO.name)`).

Two habits matter here:

- **Inject dispatchers rather than hardcoding.** `Dispatchers.IO` has no common declaration and is `internal` on Kotlin/Native — use the injected dispatcher or `ioDispatcher`, never `Dispatchers.IO` in common code.
- **Koin resolves lazily, so a wiring mistake compiles fine and fails at first use.** Adding a `platform*Module` means registering it in both `initKoin`'s list and the platform actual. Past bugs here survived several phases of green builds because nothing ever ran them; hand-tracing catches a *missing* registration but not a value pulled from the wrong source.

### Navigation

navigation3 in `commonMain`. `androidx.navigation3:navigation3-runtime` is already multiplatform; only the UI artifact comes from JetBrains (`org.jetbrains.androidx.navigation3:navigation3-ui`, versioned separately in the catalog).

**When you add a route, register it in `shared/.../navigation/NavKeySerialization.kt`.** `NavKey` is an open interface, and the navigation3 overloads that resolve stored keys by reflection are Android-only, so off Android every route must be listed in a `polymorphic(NavKey::class) { subclass(...) }` module. Forget it and the route restores only on Android. `NavKeySerializationTest` guards the top-level routes.

## Gotchas worth knowing before changing build files

These cost real debugging time during the migration; the full list is in `CMP_MIGRATION_PLAN.md`.

- **In KMP Android library modules, assume anything resource- or test-related is off until proven on.** `androidResources.enable` and host tests are both disabled by default, and both fail *silently* — a module owning composeResources ships zero assets, and a `commonTest` folder never runs. Any module that owns composeResources or a real `res/` folder needs `kotlin { android { androidResources.enable = true } }`; consumers do not.
- **Compile-green means little.** Phases 5 and 6 each shipped launch-crashing bugs invisible to every compile-only check. Run the app on a device before calling Android work done.
- **`platform(bom)` is a hard error inside a KMP `sourceSet.dependencies { }`** — use `implementation(project.dependencies.platform(libs.koin.bom))`.
- **The `kotlin { android { ... } }` DSL sugar only exists in `.gradle.kts` scripts.** Hand-written `Plugin<Project>` classes must use `extensions.configure<KotlinMultiplatformExtension> { configure<KotlinMultiplatformAndroidLibraryTarget> { ... } }`. Source-set accessors (`commonMain`, `androidMain`, …) work in both.
- **Kotlin 2.3.0 constrains native dependency versions.** Vico is pinned to 2.4.4 because 2.5.x klibs are built with Kotlin 2.4.0, which this compiler cannot consume — and the Android JVM ABI is lenient enough to hide it, so only the iOS link fails.
- **CMP's vector parser only accepts literal colors** — no `@android:color/white`, no theme attrs — so Android drawables cannot move to composeResources verbatim.

## graphify

See global instructions — this repo has a knowledge graph at `graphify-out/`. Prefer `graphify query`/`explain`/`path` over raw grep for "where is X" / "what calls Y" questions before falling back to `GRAPH_REPORT.md` or manual search. Run `graphify update .` after making code changes.

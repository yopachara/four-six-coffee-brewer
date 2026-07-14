# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Android app implementing Tetsu Kasuya's 4:6 coffee brewing method — a step-by-step pour-over timer where users set weight, ratio, and taste balance (Body/Sweet/Acidity, Basic/Strong/Weak), then follow a generated pour schedule with a countdown timer. History of past brews is stored locally and charted.

Multi-module Gradle/Kotlin project, structured after Google's Now in Android (NiA) sample — module names, convention-plugin layout, and package (`com.yopachara.fourtosixmethod`) still carry NiA-derived symbols (`NiaNavHost`, `FlowSixApp`, `PrintApkLocationTask`).

## Build & test commands

```
./gradlew assembleDebug              # build demo+prod debug APKs
./gradlew :app:assembleDemoDebug     # build one flavor/build-type variant
./gradlew test                       # all unit tests (JVM)
./gradlew :feature:timer:test        # unit tests for one module
./gradlew connectedAndroidTest        # instrumented tests (needs device/emulator)
./gradlew lint
```

Note: unit/instrumented tests across modules are currently unimplemented stubs (`ExampleUnitTest`, `ExampleInstrumentedTest`) — there is no real test suite yet to run as a regression check.

Product flavors: `demo` (applicationId suffix `.demo`) and `prod`, each cross-built with `debug`/`release` (`app` also defines a `benchmark` build type via `FSMBuildType`). Firebase Crashlytics/Performance plugins are wired only for the app's Firebase convention plugin.

## Architecture

### Module graph

- `app` — application module: `MainActivity`, `FlowSixApplication`, `NiaNavHost` (top-level `NavHost` composable wiring feature nav graphs), `FlowSixApp`/`FlowSixAppState` (bottom nav + `NavController` state holder).
- `feature:timer`, `feature:history`, `feature:about` — one Gradle module per screen. Each exposes a `xxxScreen()`/`xxxRoute()` Compose entry point plus a `navigation/` package (e.g. `TimerNavigation.kt`) with a route string and a `NavGraphBuilder.xxxScreen()` extension, consumed by `NiaNavHost`.
- `core-model` — pure Kotlin domain types: `Recipe`, `Step`, `State` (First..Sixth pour stages), `Level` (Basic/Strong/Week), `Balance` (Acid/Basic/Sweet). No Android/Compose dependency.
- `core-data` — `RecipeRepository` interface + `OfflineRecipeRepository` impl (Room-backed), entity↔domain mappers (`RecipeExt.kt`, `StepExt.kt`), Hilt `RepositoryModule`.
- `core-database` — Room `AppDatabase`, `RecipeDao`, entities (`RecipeEntity`, `StepEntity`), type converters, Hilt modules (`DaosModule`, `PersistenceModule`).
- `core-domain` — use cases (`InsertRecipeUseCase`, `GetRecipeHistoryListUseCase`) sitting between features and `core-data`.
- `core-common` — `FsmDispatchers`/`Dispatcher` qualifier + `DispatcherModule` for injectable `CoroutineDispatcher`s, `Result` sealed wrapper, small numeric utils.
- `core-designsystem` — theme (`Color`/`Shape`/`Theme`/`Type`), `FlowSixIcons`.
- `build-logic/convention` — included build providing convention plugins (`foursixmethod.android.*`) applied by id from each module's `build.gradle.kts` instead of repeating `android {}` blocks. Key plugins: `android.application`, `android.application.compose`, `android.application.flavors`, `android.application.firebase`, `android.library`, `android.library.compose`, `android.feature` (bundles library+hilt+standard module deps for feature modules), `android.hilt`, `android.room`, `android.test`, `*.jacoco`.

### Recipe/timer domain logic

`Recipe` (core-model) is the central computed model: setting `ratio`, `coffeeWeight`, `balance`, or `level` triggers `generateSteps()`, which recomputes `steps: List<Step>` via `computeStep()` (`Step.kt`) for each pour stage. Water percentage per step comes from `Balance.sweetIndex/acidIndex` (stages 1–2, controls sweetness/acidity) and `Level.firstIndex` (stages 3+, controls body/strength); stage durations come from `getStateTotalTime()`, which varies by `Level`. `State` enumerates the 4–6 pour stages depending on `Level` (`getTotalState()`).

`TimerViewModel` (feature:timer) holds a single `TimerDisplayState` in a `StateFlow`, mutated via `setCoffeeWeight/Ratio/Balance/Level` (each rebuilds the wrapped `Recipe`) and driven by a tick loop (`initTimer`, a `Flow` counting down from `Recipe.getTotalTime()` once per second via `delay(1000)`, `conflate()`d against the domain `onTick` computation). On natural completion it persists the brew via `InsertRecipeUseCase`; `stopTime()` cancels the running job and resets to `TimerState.Stop` without saving.

### DI

Hilt throughout. Modules bind `RecipeRepository` (`RepositoryModule`), Room DB/DAO (`DaosModule`, `PersistenceModule`), and qualified coroutine dispatchers (`DispatcherModule`, using `@Dispatcher(FsmDispatchers.IO)` etc.) — inject dispatchers rather than hardcoding `Dispatchers.IO` in new repository/use-case code.

## graphify

See global instructions — this repo has a knowledge graph at `graphify-out/`. Prefer `graphify query`/`explain`/`path` over raw grep for "where is X" / "what calls Y" questions before falling back to `GRAPH_REPORT.md` or manual search. Run `graphify update .` after making code changes.
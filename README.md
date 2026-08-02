# 4:6 Coffee Brew Timer ☕

Compose Multiplatform app implementing Tetsu Kasuya's 4:6 coffee brewing method — a step-by-step
pour-over timer. Set weight, ratio, and taste balance (Body/Sweet/Acidity, Basic/Strong/Weak), then
follow a generated pour schedule with a countdown timer. Past brews are saved locally and charted in
a history screen.

respect to Tetsu Kasuya

## Features

- Pour schedule generated from coffee weight, ratio, and taste balance
- Countdown timer per pour step, with notifications that keep firing in the background
- Iced-drip mode (splits the recipe into hot water and ice)
- Brew history stored locally (Room) and charted
- Light/dark theming and a configurable accent colour
- Adaptive layout: phone portrait, phone landscape, and tablet each get their own arrangement

## Platforms

Android is the shipping platform. The codebase was ported to Compose Multiplatform, so the UI,
navigation, domain logic, and persistence are all shared, and an iOS app target exists — but **iOS
has not been run yet.** It compiles for `iosArm64`/`iosSimulatorArm64`; linking the framework and
opening `iosApp/` needs a full Xcode install, which the current dev setup lacks. Treat iOS as
work in progress.

## Adaptive layout

One screen, three arrangements, chosen from the window size rather than the device:

| Window | Navigation | Timer screen |
| --- | --- | --- |
| Compact width (< 600dp) — phone portrait | Bottom bar | Single scrolling column; the pour schedule sits behind a *Full schedule* toggle |
| Medium width (600–839dp) — phone landscape, small tablet | Icon-only rail | Two panes: timer + transport controls beside a permanent schedule pane |
| Expanded width (≥ 840dp) — tablet | Rail with labels | Same two panes, larger readout and roomier padding |

The size class is measured once near the root with `BoxWithConstraints` (so it stays in
`commonMain` and re-measures for free on rotation, resize, or a foldable opening) and published
through `LocalWindowSizeClass` in `core-designsystem`. A short window keeps phone-sized type even
when it is wide, which is what stops a phone in landscape from clipping.

The one place the platforms deliberately differ is the background timer: Android keeps a live
foreground countdown notification, while iOS pre-schedules a local notification per pour boundary,
so alerts still fire on time but the on-screen readout freezes while the app is suspended.

## Tech stack

- Kotlin Multiplatform, Compose Multiplatform
- Koin for DI, Room + DataStore for persistence, Coroutines/Flow
- navigation3, Vico for charts
- Multi-module Gradle project (Now in Android-style structure), with convention plugins in
  `build-logic/convention` for shared module config

## Modules

Every module except `app` is Kotlin Multiplatform (`commonMain` / `androidMain` / `iosMain`).

- `app` — Android entry point only: `Application` + `Activity`
- `shared` — the shared root composable, adaptive navigation (bottom bar or rail), navigation host,
  and DI setup; also exports the iOS framework
- `iosApp` — Xcode project hosting the shared UI
- `feature:timer`, `feature:history`, `feature:about`, `feature:settings` — one module per screen
- `core-model` — pure Kotlin domain types (`Recipe`, `Step`, `Level`, `Balance`)
- `core-data` / `core-database` — repositories + Room persistence
- `core-domain` — use cases between features and data
- `core-common` — dispatchers, `AppLogger` (logcat on Android, `NSLog` on iOS), shared utils
- `core-designsystem` — theme, icons, shared drawables, window size class

## Building

```
./gradlew :app:assembleDemoDebug   # Android APK (demo flavor)
./gradlew testAndroidHostTest      # shared unit tests
```

Note that `./gradlew test` only covers `:app` — the multiplatform modules' tests run under
`testAndroidHostTest`.

For iOS, once a full Xcode is installed: `open iosApp/iosApp.xcodeproj` and run. The Gradle build
of the shared framework is wired in as a build phase.

<table style="padding:10px">
  <tr>
    <td><img width="260px" alt="Screenshot_20260721_094856" src="https://github.com/user-attachments/assets/0c1a24d7-5ac1-4f72-aaec-1e529de184a4" /></td>
    <td><img width="260px" alt="Screenshot_20260721_094907" src="https://github.com/user-attachments/assets/0079162d-d1f2-4826-8c64-da24200a1555"/></td>
    <td><img width="260px" alt="Screenshot_20260721_094914" src="https://github.com/user-attachments/assets/456fa2e7-efdf-4006-b423-53cb29ff0bac" /></td>
    <td><img width="260px" alt="Screenshot_20260721_094924" src="https://github.com/user-attachments/assets/f88125a2-65a8-4fdc-a8a8-e5df1d46cb6b" /></td>
    <td><img width="260px" alt="Screenshot_20260721_094931" src="https://github.com/user-attachments/assets/f23125ea-3fe1-42d7-a424-836adac0af6c" /></td>
    <td><img width="260px" alt="Screenshot_20260721_094942" src="https://github.com/user-attachments/assets/5512e92e-0972-403c-8506-3aaea4fcada6" /></td>
  </tr>

</table>
<table>
  <tr>
   <td><img width="520" alt="Screenshot_1785680066" src="https://github.com/user-attachments/assets/e9b8a2db-a06d-44af-a8dc-d1dde2bc5347" /> </td>
   <td><img width="520" alt="Screenshot_1785680076" src="https://github.com/user-attachments/assets/440b06cb-51df-4e4f-bb70-26cbcc4b38cf" /> </td>
 </tr>
</table>

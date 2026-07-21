# 4:6 Coffee Brew Timer ☕

Android app implementing Tetsu Kasuya's 4:6 coffee brewing method — a step-by-step pour-over timer.
Set weight, ratio, and taste balance (Body/Sweet/Acidity, Basic/Strong/Weak), then follow a generated
pour schedule with a countdown timer. Past brews are saved locally and charted in a history screen.

respect to Tetsu Kasuya

## Features

- Pour schedule generated from coffee weight, ratio, and taste balance
- Countdown timer per pour step
- Brew history stored locally (Room) and charted
- Light/dark theming via Compose

## Tech stack

- Kotlin, Jetpack Compose
- Multi-module Gradle project (Now in Android-style structure), Hilt DI, Room, Coroutines/Flow
- Convention plugins in `build-logic/convention` for shared module config

## Modules

- `app` — application entry point, navigation host, bottom nav
- `feature:timer`, `feature:history`, `feature:about` , `feature:settings` — one module per screen
- `core-model` — pure Kotlin domain types (`Recipe`, `Step`, `Level`, `Balance`)
- `core-data` / `core-database` — repository + Room persistence
- `core-domain` — use cases between features and data
- `core-common` — dispatchers, shared utils
- `core-designsystem` — theme, icons


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

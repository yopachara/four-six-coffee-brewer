# 4:6 Method Brewer Scale ☕

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
- `feature:timer`, `feature:history`, `feature:about` — one module per screen
- `core-model` — pure Kotlin domain types (`Recipe`, `Step`, `Level`, `Balance`)
- `core-data` / `core-database` — repository + Room persistence
- `core-domain` — use cases between features and data
- `core-common` — dispatchers, shared utils
- `core-designsystem` — theme, icons

<table style="padding:10px">
  <tr>
    <td><img src="https://github.com/user-attachments/assets/21ff4b01-4a23-42a5-8ed9-1f2e982dfb4c" alt="1" width = 260px ></td>
    <td><img src="https://github.com/user-attachments/assets/cc866f9c-4f00-4f53-84ac-e661f0a10809" alt="4" width = 260px ></td>   
    <td><img src="https://github.com/user-attachments/assets/b5722f1e-fb56-4e53-b739-8f6c358e2e26" alt="2" width = 260px ></td>
    <td><img src="https://github.com/user-attachments/assets/06a0703b-8fda-455e-a5dd-724702f5c940" alt="3" width = 260px ></td>   
    <td><img src="https://github.com/user-attachments/assets/4935b1b6-3a94-4a91-b016-278e12066b62" alt="5" width = 260px ></td>

  </tr>
</table>

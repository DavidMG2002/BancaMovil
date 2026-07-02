# BancaMovil

A mobile banking simulation app for Android, built with Kotlin and Jetpack Compose.

## Overview

BancaMovil simulates the core operations of a mobile banking application, including user authentication, money transfers, transaction history, and profile management.

## Features

- **Login & Registration** — user authentication flow
- **Home** — account overview and quick actions
- **Transfers** — send money between accounts
- **Transaction History** — view past transfers
- **Profile** — manage user account details

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Architecture:** Clean Architecture (`presentation` / `domain` / `data` layers), MVVM
- **Navigation:** Jetpack Navigation Compose
- **Backend / Auth:** Firebase (Authentication, Realtime Database)
- **File storage:** Supabase Storage
- **Image loading:** Coil
- **Networking:** Ktor

## Project Structure

```
app/src/main/java/com/example/bancamovil/
├── data/
│   ├── datasource/     # Firebase, Supabase and camera data sources
│   └── repository/     # Repository implementations
├── domain/
│   ├── model/           # Domain models (User, Transfer, UserProfile)
│   ├── repository/      # Repository interfaces
│   └── usecase/         # Business logic (Login, Register, Transfer, History)
├── presentation/
│   ├── login/
│   ├── register/
│   ├── home/
│   ├── transfer/
│   ├── history/
│   ├── profile/
│   ├── navigation/
│   └── components/
└── ui/theme/            # Compose theming (colors, typography)
```

## Getting Started

### Prerequisites

- Android Studio (latest stable version)
- JDK 17+
- A Firebase project with `google-services.json` configured in `app/`

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/DavidMG2002/BancaMovil.git
   ```
2. Open the project in Android Studio.
3. Add your own `google-services.json` file to the `app/` directory (Firebase configuration).
4. Sync Gradle and run the app on an emulator or physical device.

## Branches

- `main` — stable/production-ready code
- `develop` — active development

## Contributors

- [DavidMG2002](https://github.com/DavidMG2002)

## License

This project is for educational purposes (academic simulation project).

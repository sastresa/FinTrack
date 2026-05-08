# FinTrack

FinTrack is an Android-only personal finance tracker built with Kotlin, Jetpack Compose, Coroutines and Flow, MVVM, Room, Navigation Compose, WorkManager, and Material 3.

## Features

- Create, edit, delete, search, and filter income and expense transactions.
- Manage categories for income and expenses.
- View a monthly dashboard with balance, income, expenses, savings rate, category breakdown, and recent transactions.
- Track monthly budgets and exceeded-budget warnings.
- Review monthly reports and category breakdowns.
- Configure currency and dark mode preference.
- Export local data to CSV through WorkManager.
- Run a local backup simulation through WorkManager.

## Architecture

The app is local-first and organized by layer:

```text
app/src/main/java/com/fintrack/
  data/
    local/
      dao/
      db/
      entity/
      mapper/
    repository/
  domain/
    model/
    repository/
    usecase/
  ui/
    navigation/
    screen/
    component/
    theme/
  worker/
  di/
```

Composables render immutable state and send events upward. ViewModels own `StateFlow` screen state and call use cases. Use cases depend on repository contracts. The Room-backed repository is the persistence boundary.

## Build

This project includes a local `gradlew` launcher that uses the cached Gradle 9.1.0 distribution on this machine.

```bash
./gradlew :app:assembleDebug
```

If building on another machine, install Android SDK platform 36 or update `local.properties` to point at your SDK.

## Tests

Run JVM tests:

```bash
./gradlew :app:testDebugUnitTest
```

Compile instrumented tests:

```bash
./gradlew :app:compileDebugAndroidTestKotlin
```

Run instrumented tests when an emulator or Android device is connected:

```bash
./gradlew :app:connectedDebugAndroidTest
```

## Notes

- Money is stored as minor units (`Long`) to avoid floating point drift.
- Room stores dates through type converters.
- CSV export writes to app-local `filesDir/exports`.
- The debug database builder uses destructive fallback for development only.
- Recurring transaction metadata and date calculation are present; automatic future transaction generation is intentionally left as stretch scope.

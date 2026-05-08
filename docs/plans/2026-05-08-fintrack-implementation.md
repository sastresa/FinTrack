# FinTrack Implementation Plan

> **For Claude:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Build the FinTrack Android MVP as a local-first Kotlin app with clean layering, Room persistence, WorkManager export, Compose UI, and focused tests.

**Architecture:** Use a single Android app module with `domain`, `data`, `ui`, `worker`, `di`, and `util` packages. UI state is owned by ViewModels and exposed as immutable `StateFlow`; repositories are the only data boundary for use cases and ViewModels.

**Tech Stack:** Kotlin 2.3.10, Android Gradle Plugin 9.0.1, Jetpack Compose Material 3, Coroutines/Flow, Lifecycle ViewModel, Navigation Compose, Room 2.8.4, WorkManager 2.11.1, JUnit, AndroidX test, Compose UI test.

---

## Task 1: Project Setup

**Files:**
- Create: `settings.gradle.kts`
- Create: `build.gradle.kts`
- Create: `gradle.properties`
- Create: `local.properties`
- Create: `gradlew`
- Create: `app/build.gradle.kts`
- Create: `app/src/main/AndroidManifest.xml`
- Create: `app/src/main/java/com/fintrack/MainActivity.kt`
- Create: `app/src/main/java/com/fintrack/di/AppContainer.kt`
- Create: `app/src/main/java/com/fintrack/FinTrackApplication.kt`

**Step 1: Write minimal build files**

Configure one `:app` Android module, compile SDK 36, min SDK 26, Kotlin JVM target 17, Compose enabled through the Kotlin Compose compiler plugin, Room schema export, and dependencies for Compose, lifecycle, navigation, Room, WorkManager, and tests.

**Step 2: Add a local Gradle launcher**

Create `gradlew` as a small shell script that calls the cached Gradle 9.1.0 binary at:

```bash
/Users/laura.oran/.gradle/wrapper/dists/gradle-9.1.0-bin/9agqghryom9wkf8r80qlhnts3/gradle-9.1.0/bin/gradle
```

**Step 3: Verify project skeleton**

Run:

```bash
./gradlew --offline :app:tasks
```

Expected: Gradle resolves the Android app project and lists app tasks.

**Step 4: Commit**

```bash
git add .
git commit -m "chore: scaffold Android project"
```

## Task 2: Domain Models and Pure Use Cases

**Files:**
- Create: `app/src/main/java/com/fintrack/domain/model/*.kt`
- Create: `app/src/main/java/com/fintrack/domain/repository/FinanceRepository.kt`
- Create: `app/src/main/java/com/fintrack/domain/usecase/*.kt`
- Create: `app/src/test/java/com/fintrack/domain/usecase/*Test.kt`
- Create: `app/src/test/java/com/fintrack/domain/model/RecurringRuleTest.kt`

**Step 1: Write failing tests**

Cover:

- Transaction input validation rejects blank title, non-positive amount, missing category, and missing date.
- Money parser converts decimal display input to minor units.
- Monthly summary calculates income, expenses, balance, and savings rate.
- Search and filters match title, notes, type, category, and date range.
- Budget progress calculates spent, remaining, percentage, and exceeded status.
- Recurring rule calculates the next weekly and monthly date.

Run:

```bash
./gradlew --offline :app:testDebugUnitTest --tests "com.fintrack.domain.*"
```

Expected: tests fail because domain code is not implemented.

**Step 2: Implement domain code**

Add immutable models:

- `Transaction`
- `TransactionType`
- `Category`
- `CategoryType`
- `Budget`
- `RecurringRule`
- `RecurringFrequency`
- `MonthlySummary`
- `ReportData`
- `BudgetProgress`
- `TransactionFilter`
- `ValidationResult`

Implement required use cases. Keep calculation and validation use cases pure.

**Step 3: Verify**

Run the same domain test command.

Expected: all domain tests pass.

**Step 4: Commit**

```bash
git add app/src/main/java/com/fintrack/domain app/src/test/java/com/fintrack/domain
git commit -m "feat: add finance domain layer"
```

## Task 3: Fake Repository and ViewModels

**Files:**
- Create: `app/src/test/java/com/fintrack/data/FakeFinanceRepository.kt`
- Create: `app/src/main/java/com/fintrack/ui/screen/**/**UiState.kt`
- Create: `app/src/main/java/com/fintrack/ui/screen/**/**ViewModel.kt`
- Create: `app/src/test/java/com/fintrack/ui/screen/**/**ViewModelTest.kt`

**Step 1: Write failing ViewModel tests**

Cover:

- Dashboard initial and populated state.
- Transaction list search and type/category/date filters.
- Editor validation and save event.
- Categories add/edit/delete events.
- Budgets progress and exceeded warning.
- Reports month selection and export event.
- Settings currency and dark mode events.

Run:

```bash
./gradlew --offline :app:testDebugUnitTest --tests "com.fintrack.ui.*"
```

Expected: tests fail because ViewModels and fake repository are missing.

**Step 2: Implement fake repository and ViewModels**

The fake repository stores data in `MutableStateFlow` and implements `FinanceRepository`. Each ViewModel exposes exactly one public immutable `StateFlow` and event methods such as `onTitleChanged`, `onSaveClicked`, `onDeleteClicked`, `onSearchQueryChanged`, and `onFilterChanged`.

**Step 3: Verify**

Run the same ViewModel test command.

Expected: ViewModel tests pass.

**Step 4: Commit**

```bash
git add app/src/main/java/com/fintrack/ui app/src/test/java/com/fintrack
git commit -m "feat: add screen state and viewmodels"
```

## Task 4: Room Persistence and Repository Implementation

**Files:**
- Create: `app/src/main/java/com/fintrack/data/local/entity/*.kt`
- Create: `app/src/main/java/com/fintrack/data/local/dao/*.kt`
- Create: `app/src/main/java/com/fintrack/data/local/db/FinTrackDatabase.kt`
- Create: `app/src/main/java/com/fintrack/data/local/db/Converters.kt`
- Create: `app/src/main/java/com/fintrack/data/local/mapper/*.kt`
- Create: `app/src/main/java/com/fintrack/data/repository/RoomFinanceRepository.kt`
- Create: `app/src/test/java/com/fintrack/data/local/mapper/*Test.kt`
- Create: `app/src/androidTest/java/com/fintrack/data/local/dao/*Test.kt`
- Create: `app/src/androidTest/java/com/fintrack/data/repository/RoomFinanceRepositoryTest.kt`

**Step 1: Write failing mapping and DAO tests**

Cover mapping round trips, insert/update/delete, observed query flows, month filtering, category joins for summaries, and export ordering.

Run:

```bash
./gradlew --offline :app:testDebugUnitTest --tests "com.fintrack.data.local.mapper.*"
./gradlew --offline :app:connectedDebugAndroidTest
```

Expected: tests fail because Room code is missing. Connected tests require an emulator or device.

**Step 2: Implement Room layer**

Use Room as source of truth. Store money as `Long` minor units, dates/instants/year-months through converters, and enums as strings. Use destructive fallback only in the debug/dev database builder.

**Step 3: Verify**

Run mapper unit tests and connected Android tests when a device is available.

**Step 4: Commit**

```bash
git add app/src/main/java/com/fintrack/data app/src/test/java/com/fintrack/data app/src/androidTest/java/com/fintrack/data
git commit -m "feat: persist finance data with Room"
```

## Task 5: Compose UI and Navigation

**Files:**
- Create: `app/src/main/java/com/fintrack/ui/navigation/*.kt`
- Create: `app/src/main/java/com/fintrack/ui/component/*.kt`
- Create: `app/src/main/java/com/fintrack/ui/theme/*.kt`
- Create: `app/src/main/java/com/fintrack/ui/screen/dashboard/*.kt`
- Create: `app/src/main/java/com/fintrack/ui/screen/transactions/*.kt`
- Create: `app/src/main/java/com/fintrack/ui/screen/editor/*.kt`
- Create: `app/src/main/java/com/fintrack/ui/screen/categories/*.kt`
- Create: `app/src/main/java/com/fintrack/ui/screen/budgets/*.kt`
- Create: `app/src/main/java/com/fintrack/ui/screen/reports/*.kt`
- Create: `app/src/main/java/com/fintrack/ui/screen/settings/*.kt`
- Create: `app/src/androidTest/java/com/fintrack/ui/*Test.kt`

**Step 1: Write failing Compose UI tests**

Cover dashboard summary rendering, transaction list item rendering, editor validation messages, filter behavior, and navigation to primary screens.

Run:

```bash
./gradlew --offline :app:connectedDebugAndroidTest
```

Expected: tests fail until screens exist. Connected tests require an emulator or device.

**Step 2: Implement UI**

Use Material 3, a single top-level scaffold, bottom navigation for main destinations, route constants for required routes, and screen-specific stateless composables. Composables receive state and callbacks only; they do not call repositories or use cases.

**Step 3: Verify**

Run:

```bash
./gradlew --offline :app:assembleDebug
./gradlew --offline :app:testDebugUnitTest
./gradlew --offline :app:connectedDebugAndroidTest
```

**Step 4: Commit**

```bash
git add app/src/main/java/com/fintrack/ui app/src/androidTest/java/com/fintrack/ui app/src/main/java/com/fintrack/MainActivity.kt
git commit -m "feat: build FinTrack Compose UI"
```

## Task 6: WorkManager Export and Backup Simulation

**Files:**
- Create: `app/src/main/java/com/fintrack/worker/ExportTransactionsWorker.kt`
- Create: `app/src/main/java/com/fintrack/worker/BackupSimulationWorker.kt`
- Modify: `app/src/main/java/com/fintrack/domain/usecase/ExportTransactionsUseCase.kt`
- Modify: `app/src/main/java/com/fintrack/domain/usecase/GenerateCsvUseCase.kt`
- Create: `app/src/test/java/com/fintrack/domain/usecase/GenerateCsvUseCaseTest.kt`
- Create: `app/src/androidTest/java/com/fintrack/worker/*Test.kt`

**Step 1: Write failing tests**

Cover CSV header/content escaping, export worker success, export worker failure, missing input data, and backup simulation success.

**Step 2: Implement workers**

Workers accept explicit input data, run off the main thread, return `Result.success` or `Result.failure`, and write export files to app-local storage.

**Step 3: Verify**

Run unit tests and connected worker tests when a device is available.

**Step 4: Commit**

```bash
git add app/src/main/java/com/fintrack/worker app/src/test/java/com/fintrack/domain/usecase app/src/androidTest/java/com/fintrack/worker
git commit -m "feat: add export and backup workers"
```

## Task 7: README and Final Verification

**Files:**
- Create: `README.md`
- Create or update: `.gitignore`

**Step 1: Document the project**

README must explain features, architecture, package layout, local-first storage, build/test commands, worker behavior, and known limits.

**Step 2: Run final verification**

Run:

```bash
./gradlew --offline :app:assembleDebug
./gradlew --offline :app:testDebugUnitTest
```

If an emulator/device is available, also run:

```bash
./gradlew --offline :app:connectedDebugAndroidTest
```

**Step 3: Inspect git state**

Run:

```bash
git status --short
```

Expected: no unexpected files outside intended project artifacts.

**Step 4: Commit**

```bash
git add README.md .gitignore
git commit -m "docs: document FinTrack starter app"
```

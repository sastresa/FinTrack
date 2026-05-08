# FinTrack Design

## Goal

Build an Android-only personal finance tracker in Kotlin. FinTrack is local-first and uses Jetpack Compose, Coroutines and Flow, MVVM, Room, Navigation Compose, WorkManager, Material 3, and focused tests across domain logic, repositories, ViewModels, workers, and primary UI flows.

## Product Scope

The v1 app supports transaction CRUD, income and expense tracking, categories, a monthly dashboard summary, searchable and filterable history, budget progress tracking, reports, settings, local CSV export, and empty/loading/error states.

Stretch features such as recurring transaction execution, biometric lock, PDF export, cloud sync, and advanced charts are intentionally outside the initial build except for storing recurring metadata and adding a backup/export WorkManager path.

## Architecture

The app is layered by responsibility:

- `domain`: immutable models, repository contracts, pure use cases, validation, filters, summaries, report data, and budget progress.
- `data`: Room entities, DAOs, database, mappers, CSV export, and repository implementation.
- `ui`: immutable state classes, ViewModels, Navigation Compose graph, Material 3 theme, screens, and reusable Compose components.
- `worker`: WorkManager tasks for CSV export and backup simulation.
- `di`: lightweight dependency wiring for application-wide singletons and ViewModel factories.

The UI never accesses Room or repositories directly. Composables render immutable state and send events upward. ViewModels own screen state and call use cases. Use cases depend on repository contracts. The data repository is the only source-of-truth boundary for persistence and emits `Flow` values from Room.

## Data Flow

User actions flow upward:

```text
Composable event -> ViewModel -> Use case -> Repository -> Room DAO
```

State flows downward:

```text
Room Flow -> Repository mapping -> Use case -> ViewModel StateFlow -> Composable
```

This keeps business logic out of composables and makes logic testable without Android UI dependencies.

## Domain Model Decisions

Money is represented as minor units with a currency-aware display layer. Domain models use `Long` for persisted monetary amounts to avoid floating point errors. UI input accepts decimal strings and validation converts them into minor units.

Dates use `LocalDate`, `YearMonth`, and `Instant` in domain code. Room stores them through type converters. Reports and budgets are calculated by calendar month.

Transactions can be marked recurring in v1, and the schema includes a `RecurringRule` model. The first implementation stores the metadata and tests recurring calculation logic, but automatic generation of future transactions remains stretch scope.

## Screens

The app uses one top-level `Scaffold` with bottom navigation for:

- Dashboard
- Transactions
- Budgets
- Reports
- Settings

Additional routes:

- `transactionEditor/{id?}` for create and edit
- `categories` for category management

Dashboard shows balance, monthly income, monthly expenses, savings rate, category breakdown, recent transactions, and quick add. The transaction list supports search, filters, grouping by date, swipe delete, and edit navigation. The editor validates title, amount, category, and date. Budgets show progress and exceeded warnings. Reports show monthly and category summaries and export. Settings owns currency, dark mode, export/backup, and clear local data actions.

## Error Handling

Every screen state includes `isLoading`, nullable `errorMessage`, and data fields. Form states include user input fields plus validation errors. Repository and worker failures are surfaced to ViewModels as user-readable messages without leaking persistence details into UI code.

## Background Work

WorkManager handles CSV export and backup simulation. Workers run off the main thread, accept explicit input data, return success/failure, and are covered by worker tests.

## Testing Strategy

Tests are organized by risk:

- Unit tests cover validation, money parsing, filtering/search, summaries, budget progress, and recurring rule date logic.
- ViewModel tests cover initial state, event handling, save/delete behavior, error propagation, and state updates after repository changes.
- Repository tests cover Room DAO behavior, mapping, insert/update/delete, query flows, and export data generation.
- Compose UI tests cover dashboard summary rendering, transaction list rendering/filtering, editor validation, and navigation to primary screens.
- Worker tests cover export success, failure, and input validation.

## Implementation Approach

Build the project as production-quality starter code rather than a tutorial demo. Keep the initial app focused on the MVP, use fake repositories to enable ViewModel and UI tests early, then connect Room as the source of truth.

The implementation order is:

1. Project setup and dependencies
2. Domain models and repository interfaces
3. Fake repository
4. ViewModels and unit tests
5. Room entities, DAO, database, mappers
6. Repository implementation
7. Compose screens and navigation
8. WorkManager export task
9. UI tests
10. README and cleanup

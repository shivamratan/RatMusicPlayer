# Room Database Schema Sync

The application is crashing because the Room database schema has been modified (likely by adding or changing fields in `TrackEntity`) but the database version number in `MusicDatabase` remains at 1. Room detects this mismatch and throws an `IllegalStateException`.

## Proposed Changes

### Data Layer

#### [MODIFY] [MusicDatabase.kt](file:///Users/shivamratan/AndroidStudioProjects/ExoplayerSample/app/src/main/java/com/ratanapps/exoplayersample/data/local/MusicDatabase.kt)
- Increment the `version` from `1` to `2` in the `@Database` annotation.

#### [MODIFY] [DatabaseModule.kt](file:///Users/shivamratan/AndroidStudioProjects/ExoplayerSample/app/src/main/java/com/ratanapps/exoplayersample/di/DatabaseModule.kt)
- Add `.fallbackToDestructiveMigration()` to the `Room.databaseBuilder` chain. This ensures that if the schema changes again without a proper migration, Room will recreate the database instead of crashing.

## Verification Plan

### Manual Verification
1. Re-run the application.
2. Verify that the app no longer crashes on startup.
3. Verify that the music database is correctly initialized (though previous data will be cleared due to destructive migration).

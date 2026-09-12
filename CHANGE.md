# Change Log

All notable changes to **Islamic Knowledge Platform** are tracked here by milestone and release.

## Phase 0 — Build health

### Completed
- Restricted `settings.gradle.kts` to modules that have real build scripts (`:app`, `:core:design`, `:core:model`, `:feature:home`, `:feature:quran`).
- Left roadmap placeholder directories in the tree without registering them as Gradle projects.
- Added Gradle Wrapper scripts and `gradle-wrapper.properties` (Gradle 8.11.1).
- Documented one-time `gradle-wrapper.jar` generation and local Quran content generation in `DEVELOPMENT.md`.
- Made CI prefer `./gradlew` when the wrapper jar is present, with a system `gradle` fallback.
- Release workflow now validates Quran content before packaging.
- Merged via PR #2.

### Optional follow-up
- Commit `gradle/wrapper/gradle-wrapper.jar` when a desktop Gradle install is available.

## v0.2.0-alpha — Quran (in progress)

### Completed
- Added the Quran feature module and offline Quran reader architecture.
- Added the 114-surah catalog and full-content generation pipeline.
- Added Arabic Quran text with Bengali Rowwad translation metadata.
- Added Juz, Hizb, page, and Sajdah metadata to reader content.
- Added surah filtering by Bengali, English, and Arabic names.
- Added in-surah search and Quran-wide search entry.
- Added bookmarks, last-read/resume, and local notes.
- Added copy and Android share actions.
- Added Arabic/Bengali display toggles and reader font scaling.
- Added focused full-screen Quran reader experience.
- Added retry/backoff handling to the Quran content generator for transient API failures.
- Added CI validation for 114 surahs and 6,236 ayahs before Android builds.
- Continued Material 3 design-system work for Islamic green/warm-neutral theming, Bengali-first typography, and consistent shapes.
- Updated README with current milestone status and roadmap.
- Persisted Arabic/Bengali display choices and reader font scale locally across reader sessions.
- Hardened note rendering so a newly saved note is reflected immediately in the current reader.
- Replaced the Quran reader back icon with the AutoMirrored Material icon for RTL-aware navigation.
- Hardened app navigation state so the selected tab, surah, and ayah can be restored by Compose saveable state.
- Enabled Android edge-to-edge and applied safe-drawing insets around the full-screen reader.
- Refined Quran-wide search with trimmed queries, a clear action, and explicit idle/empty states.
- **Phase 1 library UI:** Surah / Juz / Page / Bookmark / Notes tabs on the Quran home screen.
- Jump to the first ayah of a Juz or Mushaf page when structural metadata is present in the content package.
- Notes library list with open and delete; bookmark list with remove.
- Preference helpers for listing all notes and removing bookmarks.
- Repository helpers for Juz/page index lookup.
- Clearer accessibility content descriptions on primary reader and library actions.

### Remaining before v0.2.0-alpha
- Final reader UX polish on real devices (Arabic typography, scrolling feel).
- Accessibility and small/large-screen layout review on hardware.
- Full offline 114-surah QA (requires CI-generated package / device APK).
- Device testing of scrolling, search, bookmark, notes, copy/share, resume, Juz/page jumps.
- Final CI verification and release APK build (`v0.2.0-alpha`).

## v0.1.0-alpha01 — Foundation

### Released
- Initial Android/Compose project foundation.
- Modular project structure for core and feature modules.
- Material 3 design foundation and navigation shell.
- GitHub Actions Android build and release APK workflows.
- First published prerelease APK: `v0.1.0-alpha01`.

## Development policy

- Development is organized into coherent milestones rather than tiny isolated changes.
- CI is checked at milestone boundaries; larger Gradle/dependency/release changes require CI verification.
- A release is considered ready only after CI verification and device APK testing.
- Development APKs are not installed after every commit. A device-QA checkpoint will be announced when the Quran milestone reaches the appropriate state.
- Only register a module in `settings.gradle.kts` when it has a real `build.gradle.kts`.

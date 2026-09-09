# Islamic Knowledge Platform

> A Bengali-first, offline-first Islamic knowledge and practice platform for Android.

## Vision

Build a trustworthy Islamic knowledge platform where the internet enhances the experience but is not required for core knowledge and practice features.

## Principles

- Offline-first
- Source-backed
- License-aware
- Bengali-first
- Bangladesh-first, globally capable
- Privacy-friendly
- Respectful, accessible UX

## Planned modules

Quran · Hadith · Salah · Qibla · Dua & Athkar · Ramadan · Hajj & Umrah · Zakat · Islamic Calendar · Learn Islam · Seerah · Islamic Quiz · Global Search · Personal Dashboard · Optional Notifications

## Technology direction

Kotlin · Jetpack Compose · Material 3 · Clean Architecture + MVVM · Hilt · Room + SQLite FTS · DataStore · Navigation Compose · Coroutines + Flow · WorkManager · Media3 · Retrofit/OkHttp · modular feature-sliced architecture

## Offline / online model

Core knowledge and practice features are designed to work offline. Online connectivity is reserved for updates, licensed remote content, optional downloads/sync, and future source-grounded services.

## Content provenance

Every bundled or synchronized content source should record its source, publisher, author/translator, edition/reference, source version, license, attribution, verification status, and verification date. Unclear licensing means content is not bundled until rights are resolved.

## Release roadmap

- `v0.1.0-alpha` — Foundation
- `v0.2.0-alpha` — Quran
- `v0.3.0-alpha` — Hadith
- `v0.4.0-alpha` — Salah + Qibla
- `v0.5.0-beta` — Ramadan + Dua + Athkar
- `v0.6.0-beta` — Learn Islam + Seerah
- `v0.7.0-beta` — Hajj + Zakat + Calendar
- `v0.8.0-beta` — Quiz + Global Search
- `v0.9.0-rc` — Full testing
- `v1.0.0` — Stable

## Development workflow

- `main` — stable/releasable
- `develop` — integration
- `feature/*` — new features
- `fix/*` — bug fixes
- Semantic versioning
- GitHub Actions CI
- Release artifacts and release notes

## Project status

**Current milestone: `v0.1.0-alpha` — Foundation**

The foundation milestone establishes the Android project structure, design system, navigation shell, offline-first architecture, content provenance rules, and CI before large content modules are added.

## License

The application source license and third-party content licenses will be documented separately as the project is assembled. Content is not automatically covered by the software license; each source must retain its own attribution and license requirements.

# Islamic Knowledge Platform

> **Bengali-first · Offline-first · Source-backed Islamic knowledge & practice for Android**

[![Android CI](https://github.com/abbasali01843/islamic-knowledge-platform/actions/workflows/android-ci.yml/badge.svg)](https://github.com/abbasali01843/islamic-knowledge-platform/actions/workflows/android-ci.yml)
[![Latest Release](https://img.shields.io/github/v/release/abbasali01843/islamic-knowledge-platform?include_prereleases&label=latest%20release)](https://github.com/abbasali01843/islamic-knowledge-platform/releases)

## 📱 Download

### Latest test release — `v0.1.0-alpha01`

**[⬇️ Download APK](https://github.com/abbasali01843/islamic-knowledge-platform/releases/download/v0.1.0-alpha01/islamic-knowledge-platform-debug.apk)**

This is an **alpha testing build**. It is suitable for testing the current foundation and early app experience. Future releases will add the full Quran, Hadith, Salah, Qibla, Dua, Ramadan and other modules.

[View release notes & SHA-256](https://github.com/abbasali01843/islamic-knowledge-platform/releases/tag/v0.1.0-alpha01)

> **Android:** min SDK 26 (Android 8.0+) · **Application ID:** `com.islamicknowledge.platform`

## 🌙 Vision

Build a trustworthy Islamic knowledge platform where the internet enhances the experience but is not required for core knowledge and practice features.

## ✨ Principles

- **Offline-first** — core knowledge and practice should remain usable without internet.
- **Source-backed** — religious content should be traceable to identifiable sources.
- **License-aware** — unclear licensing means content is not bundled until rights are resolved.
- **Bengali-first** — Bengali is a primary user experience, not an afterthought.
- **Bangladesh-first, globally capable** — local prayer, Ramadan and calendar needs with configurable global support.
- **Privacy-friendly** — most core features should work without requiring an account.
- **Respectful & accessible UX** — readable Arabic, Bengali and English with accessible controls.

## 🧭 Planned modules

| Module | Planned scope |
|---|---|
| Quran | Arabic, Bengali/English translations, tafsir, search, bookmarks, notes, audio, offline content |
| Hadith | Bukhari, Muslim and additional collections, references, grades, search, bookmarks, offline content |
| Salah | Prayer times, countdown, monthly timetable, calculation settings, notifications and prayer guide |
| Qibla | GPS/compass-based Qibla direction with calibration and offline support |
| Dua & Athkar | Morning/evening, after Salah, sleep, travel, protection and daily duas/adhkar |
| Ramadan | Sehri/Iftar, countdown, calendar, fasting guide, Taraweeh and last-ten-nights tools |
| Hajj & Umrah | Step-by-step guides, duas and practical checklists |
| Zakat | Calculator, nisab guidance and recipient information |
| Islamic Calendar | Hijri/Gregorian dates and source-backed important dates |
| Learn Islam | Beginner learning paths and progress tracking |
| Seerah | Source-backed Seerah content |
| Quiz | Quran, Hadith, Seerah, Salah, Ramadan and general Islamic knowledge |
| Global Search | Search across Quran, Hadith, Dua and knowledge content |
| Personal Dashboard | Reading/learning progress and personal activity |
| Notifications | Prayer, Sehri/Iftar, Jumu'ah, adhkar and daily content reminders |

## 🏗️ Architecture

The project is being built as a modular Kotlin/Android application with a feature-sliced structure.

```text
app/
core/
  common/ database/ network/ model/ navigation/ search/
  audio/ location/ notifications/ design/
feature/
  home/ quran/ hadith/ prayer/ ramadan/ qibla/
  dua/ athkar/ learn/ seerah/ calendar/ zakat/ hajj/ quiz/
data/
  quran/ hadith/ dua/ knowledge/
```

### Technology direction

**Kotlin · Jetpack Compose · Material 3 · Clean Architecture + MVVM · Hilt · Room · SQLite FTS · DataStore · Navigation Compose · Coroutines + Flow · WorkManager · Media3 · Retrofit/OkHttp**

## 🔐 Offline / online model

The core principle is:

> **The internet should enhance the app, not make the app work.**

Offline use is intended for core knowledge and practice. Online connectivity is reserved for licensed updates, optional downloads/synchronization and future source-grounded services.

## 📚 Content provenance & licensing

Religious content is treated separately from application source code. Every bundled or synchronized source should record:

- Source / publisher
- Author or translator
- Edition / reference
- Source version
- License
- Attribution requirements
- Verification status
- Verification date

A dataset being available on GitHub does **not** automatically mean its contents may be redistributed in the app. Unclear licensing means the content is not bundled until the rights are resolved.

## 🗺️ Release roadmap

- `v0.1.0-alpha` — **Foundation** ✅
- `v0.2.0-alpha` — **Quran** 🚧
- `v0.3.0-alpha` — **Hadith**
- `v0.4.0-alpha` — **Salah + Qibla**
- `v0.5.0-beta` — **Ramadan + Dua + Athkar**
- `v0.6.0-beta` — **Learn Islam + Seerah**
- `v0.7.0-beta` — **Hajj + Zakat + Calendar**
- `v0.8.0-beta` — **Quiz + Global Search**
- `v0.9.0-rc` — **Full testing**
- `v1.0.0` — **Stable release**

## 📊 Current status

**Current release: `v0.1.0-alpha01` — Foundation**

The current release establishes the Android project structure, reusable design system, navigation shell, offline-first architecture, content provenance rules and automated CI/release workflow.

The next milestone is **Quran**, with a focus on license-verified, source-backed content rather than simply bundling an arbitrary online dataset.

## 🛠️ Development workflow

- `main` — stable/releasable code
- `develop` — integration branch
- `feature/*` — new features
- `fix/*` — bug fixes
- Semantic versioning
- GitHub Actions CI
- Tagged releases with APK artifacts

## 📄 License

The application source license and third-party content licenses are documented separately as the project is assembled. **Application source code and religious content are not automatically covered by the same license.** Each third-party source must retain its own attribution and license requirements.

---

**Project:** Islamic Knowledge Platform  
**Package:** `com.islamicknowledge.platform`  
**Platform:** Android 8.0+  
**Current version:** `0.1.0-alpha01`
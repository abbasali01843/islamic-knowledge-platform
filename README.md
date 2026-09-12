# Islamic Knowledge Platform

> **Bengali-first · Offline-first · Source-backed Islamic knowledge & practice for Android**

[![Android CI](https://github.com/abbasali01843/islamic-knowledge-platform/actions/workflows/android-ci.yml/badge.svg)](https://github.com/abbasali01843/islamic-knowledge-platform/actions/workflows/android-ci.yml)
[![Latest Release](https://img.shields.io/github/v/release/abbasali01843/islamic-knowledge-platform?include_prereleases&label=latest%20release)](https://github.com/abbasali01843/islamic-knowledge-platform/releases)

## 📱 Download

### Latest published release — `v0.1.0-alpha01`

**[⬇️ Download APK](https://github.com/abbasali01843/islamic-knowledge-platform/releases/download/v0.1.0-alpha01/islamic-knowledge-platform-debug.apk)**

This published alpha contains the foundation release. The current `main` branch is ahead of that release and is actively building the Quran milestone; a new APK will be published only after the milestone passes CI and device QA.

[View release notes & SHA-256](https://github.com/abbasali01843/islamic-knowledge-platform/releases/tag/v0.1.0-alpha01)

> **Android:** min SDK 26 (Android 8.0+) · **Application ID:** `com.islamicknowledge.platform`

## 🌙 Vision

Build a trustworthy Islamic knowledge platform where the internet enhances the experience but is not required for core knowledge and practice features.

## ✨ Principles

- **Offline-first** — core knowledge and practice should remain usable without internet.
- **Source-backed** — religious content should be traceable to identifiable sources.
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

## 📖 Quran milestone — `v0.2.0-alpha`

The Quran milestone is currently under active development. The content pipeline has been verified in GitHub Actions with **114 surahs and 6,236 ayahs** generated into the build.

### Implemented so far

- 114-surah Bengali/Arabic catalog
- Offline Quran content package generated during CI
- Bengali Rowwad translation source metadata
- Arabic text + Bengali translation reader
- Surah list and Bengali/English/Arabic filtering
- In-surah Arabic/Bengali search
- Quran-wide search entry point
- Bookmark add/remove and bookmark list
- Last-read position and Resume flow
- Per-ayah notes stored locally
- Copy and Android share actions
- Arabic/Bengali display toggles
- Reader font-size controls
- Reader display choices and font scale persisted locally
- Juz, Hizb and page metadata parsing/display
- Sajdah-ayah indication
- Focused full-screen Quran reader navigation
- Navigation state restoration for selected tab/surah/ayah
- Android edge-to-edge setup with safe-drawing insets for the full-screen reader
- Refined Quran search with trimmed queries, clear action, and explicit empty/idle states
- Islamic green/warm-neutral Material 3 design direction
- Bengali-first typography and component shapes
- CI retry/backoff handling for Quran content API rate limits

### Still required before `v0.2.0-alpha`

- Final reader UX and interaction polish
- Bookmark and notes management screens/flows
- Juz/Hizb/page navigation UI rather than metadata only
- Accessibility and small-screen/large-screen layout review
- Arabic typography/rendering review on real devices
- Offline installation and full 114-surah content QA
- Device testing of scrolling, search, bookmark, notes, copy/share and resume
- Final CI verification and release APK build

**Important:** a green CI build does not by itself mean the Quran milestone is release-ready. The final APK must pass real-device QA first.

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

**Active Gradle modules today:** `:app`, `:core:design`, `:core:model`, `:feature:home`, `:feature:quran`.  
Other folders are roadmap placeholders and are registered in Gradle only when they have real build scripts.

### Technology direction

**Kotlin · Jetpack Compose · Material 3 · Clean Architecture + MVVM · Hilt · Room · SQLite FTS · DataStore · Navigation Compose · Coroutines + Flow · WorkManager · Media3 · Retrofit/OkHttp**

## 🔐 Offline / online model

The core principle is:

> **The internet should enhance the app, not make the app work.**

Offline use is intended for core knowledge and practice. Online connectivity is reserved for licensed updates, optional downloads/synchronization and future source-grounded services.

## 📚 Content provenance

Religious content is treated separately from application source code. Every bundled or synchronized source should record:

- Source / publisher
- Author or translator
- Edition / reference
- Source version
- License
- Attribution requirements
- Verification status
- Verification date

## 🗺️ Release roadmap

- `v0.1.0-alpha` — **Foundation** ✅
- `v0.2.0-alpha` — **Quran** 🚧 active development
- `v0.3.0-alpha` — **Hadith**
- `v0.4.0-alpha` — **Salah + Qibla**
- `v0.5.0-beta` — **Ramadan + Dua + Athkar**
- `v0.6.0-beta` — **Learn Islam + Seerah**
- `v0.7.0-beta` — **Hajj + Zakat + Calendar**
- `v0.8.0-beta` — **Quiz + Global Search**
- `v0.9.0-rc` — **Full testing**
- `v1.0.0` — **Stable release**

## 📊 Current status

**Published release:** `v0.1.0-alpha01` — Foundation  
**Current development:** Phase 0 (build health) + `v0.2.0-alpha` Quran  
**Quran milestone estimate:** **~80% complete**

See [`CHANGE.md`](CHANGE.md) for the running development report and milestone history.  
See [`DEVELOPMENT.md`](DEVELOPMENT.md) for local setup, Gradle Wrapper, and Quran content generation.

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
**Published version:** `0.1.0-alpha01`  
**Development milestone:** `0.2.0-alpha`

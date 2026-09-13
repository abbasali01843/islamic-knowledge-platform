# Islamic Knowledge Platform

> **Bengali-first · Offline-first · Source-backed Islamic knowledge & practice for Android**

[![Android CI](https://github.com/abbasali01843/islamic-knowledge-platform/actions/workflows/android-ci.yml/badge.svg)](https://github.com/abbasali01843/islamic-knowledge-platform/actions/workflows/android-ci.yml)
[![Latest Release](https://img.shields.io/github/v/release/abbasali01843/islamic-knowledge-platform?include_prereleases&label=latest%20release)](https://github.com/abbasali01843/islamic-knowledge-platform/releases)

## 📱 Download

### Latest published release — `v0.2.0-alpha01` (Quran)

**[⬇️ Download APK](https://github.com/abbasali01843/islamic-knowledge-platform/releases/download/v0.2.0-alpha01/islamic-knowledge-platform-debug.apk)**

Offline Quran reader with 114 surahs, Bengali translation, Juz/page index, bookmarks, notes, search, and resume. Device QA passed on Android phone.

[View release notes & SHA-256](https://github.com/abbasali01843/islamic-knowledge-platform/releases/tag/v0.2.0-alpha01)

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

## 📖 Quran milestone — `v0.2.0-alpha` ✅

Released as **`v0.2.0-alpha01`** after CI verification and real-device QA.

### Included

- 114-surah Bengali/Arabic catalog and full offline content package (6,236 ayahs)
- Bengali Rowwad translation source metadata
- Arabic + Bengali reader with font scale and display toggles
- Surah / Juz / Page / Bookmark / Notes library tabs
- In-surah and Quran-wide search
- Bookmarks, last-read/resume, local notes
- Copy and share
- Progress bar, previous/next surah, keep-screen-on, auto last-read
- Arabic RTL typography with improved line height
- Sajdah indication; Juz/Hizb/page metadata
- Material 3 Islamic green/warm-neutral design; Bengali-first UI

## 🏗️ Architecture

```text
app/
core/
  design/ model/
feature/
  home/ quran/
```

**Active Gradle modules:** `:app`, `:core:design`, `:core:model`, `:feature:home`, `:feature:quran`.  
Other folders are roadmap placeholders until they have real build scripts.

### Technology direction

**Kotlin · Jetpack Compose · Material 3 · Clean Architecture + MVVM · Hilt · Room · SQLite FTS · DataStore · Navigation Compose · Coroutines + Flow · WorkManager · Media3 · Retrofit/OkHttp**

## 🔐 Offline / online model

> **The internet should enhance the app, not make the app work.**

Offline use is intended for core knowledge and practice. Online connectivity is reserved for licensed updates, optional downloads/synchronization and future source-grounded services.

## 📚 Content provenance

Religious content is treated separately from application source code. Every bundled or synchronized source should record source/publisher, translator, edition, license, attribution, and verification status.

## 🗺️ Release roadmap

- `v0.1.0-alpha` — **Foundation** ✅
- `v0.2.0-alpha` — **Quran** ✅
- `v0.3.0-alpha` — **Hadith** ← next
- `v0.4.0-alpha` — **Salah + Qibla**
- `v0.5.0-beta` — **Ramadan + Dua + Athkar**
- `v0.6.0-beta` — **Learn Islam + Seerah**
- `v0.7.0-beta` — **Hajj + Zakat + Calendar**
- `v0.8.0-beta` — **Quiz + Global Search**
- `v0.9.0-rc` — **Full testing**
- `v1.0.0` — **Stable release**

## 📊 Current status

**Published release:** `v0.2.0-alpha01` — Quran  
**Next development:** `v0.3.0-alpha` Hadith  

See [`CHANGE.md`](CHANGE.md) for the running development report.  
See [`DEVELOPMENT.md`](DEVELOPMENT.md) for local setup and content generation.

## 🛠️ Development workflow

- `main` — stable/releasable code
- `develop` — integration branch
- `feature/*` — new features
- `fix/*` — bug fixes
- Semantic versioning · GitHub Actions CI · Tagged releases with APK artifacts

## 📄 License

Application source and third-party religious content licenses are documented separately. **They are not automatically the same license.**

---

**Project:** Islamic Knowledge Platform  
**Package:** `com.islamicknowledge.platform`  
**Platform:** Android 8.0+  
**Published version:** `0.2.0-alpha01`

# Islamic Knowledge Platform

> **Bengali-first · Offline-first · Source-backed Islamic knowledge & practice**

Monorepo with **two clients** sharing the same product vision:

| Client | Stack | Status |
|--------|--------|--------|
| **Android** | Kotlin · Jetpack Compose · Material 3 | `v0.2.0-alpha01` released · root Gradle project |
| **Web** | React 18 · Vite · Tailwind · TypeScript | Active development (prayer, hadith, dua, zakat, calendar) |

[![Android CI](https://github.com/abbasali01843/islamic-knowledge-platform/actions/workflows/android-ci.yml/badge.svg)](https://github.com/abbasali01843/islamic-knowledge-platform/actions/workflows/android-ci.yml)
[![Web CI](https://github.com/abbasali01843/islamic-knowledge-platform/actions/workflows/web-ci.yml/badge.svg)](https://github.com/abbasali01843/islamic-knowledge-platform/actions/workflows/web-ci.yml)
[![Latest Release](https://img.shields.io/github/v/release/abbasali01843/islamic-knowledge-platform?include_prereleases&label=latest%20release)](https://github.com/abbasali01843/islamic-knowledge-platform/releases)

## 📱 Android — Download

**[⬇️ APK v0.2.0-alpha01](https://github.com/abbasali01843/islamic-knowledge-platform/releases/download/v0.2.0-alpha01/islamic-knowledge-platform-debug.apk)**

Offline Quran reader: 114 surahs, Bengali translation, Juz/page index, bookmarks, notes, search, resume. Device QA passed.

> **min SDK 26** · package `com.islamicknowledge.platform`

## 🌐 Web — Run locally

```bash
npm install
npm run dev
```

Build: `npm run build` → `dist/`

Web modules in progress: Quran, Salah + Qibla, Hadith, Dua, Zakat, Hijri calendar, Learn Salah.

## 🗂 Repository layout (monorepo)

```text
# Android (Gradle root — current)
app/  core/  feature/  scripts/  gradle/
settings.gradle.kts  build.gradle.kts  gradlew

# Web (Vite root — current)
src/  index.html  package.json  vite.config.ts  tsconfig.json

# Shared docs & CI
README.md  CHANGE.md  DEVELOPMENT.md
.github/workflows/android-ci.yml
.github/workflows/web-ci.yml
.github/workflows/release-apk.yml
```

> **Roadmap:** optionally nest clients under `android/` and `web/` folders later; path-filtered CI already treats them as separate pipelines.

## ✨ Principles

- Offline-first core knowledge
- Source-backed religious content
- Bengali-first UX
- Privacy-friendly (no account required for core features)

## 🗺️ Release roadmap

- `v0.1.0-alpha` — Foundation ✅
- `v0.2.0-alpha` — Quran (Android) ✅
- Web parity + Salah / Hadith / Dua — in progress
- `v0.3.0-alpha` — Hadith (Android module)
- `v0.4.0-alpha` — Salah + Qibla (Android)
- … → `v1.0.0` stable

## 📊 Current status

| Track | Version / focus |
|-------|-----------------|
| Android release | `v0.2.0-alpha01` |
| Android `main` build | Restored Gradle root (this PR) |
| Web | Feature expansion on `src/` |

See [`CHANGE.md`](CHANGE.md) and [`DEVELOPMENT.md`](DEVELOPMENT.md).

## 📄 License

Application source and third-party religious content licenses are documented separately.

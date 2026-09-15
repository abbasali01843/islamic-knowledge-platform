# Islamic Knowledge Platform (Web)

> **Bengali-first · Offline-first · Source-backed Islamic knowledge & practice**

A responsive React + Vite web application rewrite of the Islamic Knowledge Platform, preserving all core features, architecture, and design aesthetics from the original Android app.

## 🌙 Core Features

- **114-Surah Catalog**: Complete Quran catalog with Bengali, English, and Arabic names, revelation type (Meccan / Medinan), and ayah counts.
- **Offline Quran Reader**:
  - Uthmani Arabic script with right-to-left display and authentic calligraphy font (Amiri).
  - Bengali translations (Rowwad Translation Center, QuranEnc).
  - Toggles for displaying Arabic (`عربي`) and Bengali (`বাংলা`).
  - Adjustable reading font scaling (80% – 150%).
  - Reading progress indicator and reading resume banner.
  - Previous / Next Surah navigation bar.
- **Bookmarks & Notes**:
  - Offline bookmarking of ayahs with local persistence.
  - Personal note taking with editing and deletion capabilities.
  - Dedicated Bookmarks and Notes library tabs.
- **Quran Search**:
  - Instant client-side search across offline ayahs in Bengali and Arabic.
- **Home & Quick Actions**:
  - Prayer status banner and quick access cards to Quran, Hadith, Salah, and Dua modules.
- **Islamic Green Aesthetic**:
  - Consistent light and dark modes following Material 3 Islamic color palettes (`#176B4D` primary, `#D4F2E2` container).

## 🛠 Tech Stack

- **Framework**: React 18 with TypeScript
- **Bundler**: Vite
- **Styling**: Tailwind CSS
- **Icons**: Lucide React
- **Typography**: Amiri (Arabic), Hind Siliguri (Bengali), Plus Jakarta Sans (UI)

## 🚀 Running Locally

```bash
npm install
npm run dev
```

Build for production:
```bash
npm run build
```

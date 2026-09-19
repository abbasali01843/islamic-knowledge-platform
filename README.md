# Islamic Knowledge Platform — Web

**Learn • Practice • Live Islam**

Standalone web client for Islamic Knowledge Platform.

## Features

- Quran — 114 surahs, Bengali translation, search, bookmarks and reading progress
- Hadith — searchable collections, topics, Nawawi 40 and bookmarks
- Prayer — daily prayer times with location-aware calculation
- Dua & Adhkar — categorized daily supplications and Tasbeeh
- Learn Salah
- Zakat calculator
- Hijri calendar
- Responsive PWA-friendly experience
- Light/dark theme
- Local persistence for preferences and bookmarks

## Development

    npm install
    npm run dev
    npm run build
    npm run preview

## Architecture

The Web client is independent from the Android application. Hadith is planned around a hybrid API + local-cache + curated fallback architecture so temporary network/API failures do not make the section unusable.

## Brand

**Islamic Knowledge Platform**
*Learn • Practice • Live Islam*

## Content principle

Religious content should remain source-backed, clearly attributed, and should not silently replace verified Bengali translations with machine-generated religious translations.

See CONTENT_LICENSE.md for content licensing notes.

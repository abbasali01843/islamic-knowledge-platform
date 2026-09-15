import { ReaderAyah } from '../types';
import rawData from './quran_reader.json';

interface RawAyah {
  number: number;
  arabic: string;
  bengali: string;
  juz?: number | null;
  hizb?: number | null;
  page?: number | null;
  hasSajdah?: boolean;
}

interface RawSurah {
  number: number;
  ayahCount: number;
  ayahs: RawAyah[];
}

interface RawDocument {
  schemaVersion: number;
  generatedAt: string;
  arabicSource: {
    name: string;
    version: string;
    url: string;
    attribution: string;
  };
  bengaliSource: {
    name: string;
    version: string;
    url: string;
    publisher?: string;
    attribution: string;
  };
  surahs: RawSurah[];
}

const document: RawDocument = rawData as RawDocument;

export class QuranReaderRepository {
  static ayahsForSurah(surahNumber: number): ReaderAyah[] {
    const surah = document.surahs.find((s) => s.number === surahNumber);
    return surah ? surah.ayahs : [];
  }

  static search(query: string): Array<{ surahNumber: number; ayah: ReaderAyah }> {
    const q = query.trim().toLowerCase();
    if (!q) return [];
    const results: Array<{ surahNumber: number; ayah: ReaderAyah }> = [];
    for (const surah of document.surahs) {
      for (const ayah of surah.ayahs) {
        if (
          ayah.arabic.toLowerCase().includes(q) ||
          ayah.bengali.toLowerCase().includes(q)
        ) {
          results.push({ surahNumber: surah.number, ayah });
        }
      }
    }
    return results;
  }

  /** First (surahNumber, ayah) that belongs to the given Juz, if content has metadata. */
  static firstAyahForJuz(juz: number): { surahNumber: number; ayah: ReaderAyah } | null {
    for (const surah of document.surahs) {
      const ayah = surah.ayahs.find((a) => a.juz === juz);
      if (ayah) return { surahNumber: surah.number, ayah };
    }
    return null;
  }

  /** First (surahNumber, ayah) on the given Mushaf page, if content has metadata. */
  static firstAyahForPage(page: number): { surahNumber: number; ayah: ReaderAyah } | null {
    for (const surah of document.surahs) {
      const ayah = surah.ayahs.find((a) => a.page === page);
      if (ayah) return { surahNumber: surah.number, ayah };
    }
    return null;
  }

  static availableJuzNumbers(): number[] {
    const set = new Set<number>();
    for (const surah of document.surahs) {
      for (const ayah of surah.ayahs) {
        if (ayah.juz && ayah.juz > 0) {
          set.add(ayah.juz);
        }
      }
    }
    return Array.from(set).sort((a, b) => a - b);
  }

  static availablePageNumbers(): number[] {
    const set = new Set<number>();
    for (const surah of document.surahs) {
      for (const ayah of surah.ayahs) {
        if (ayah.page && ayah.page > 0) {
          set.add(ayah.page);
        }
      }
    }
    return Array.from(set).sort((a, b) => a - b);
  }

  static hasStructuralIndex(): boolean {
    return (
      this.availableJuzNumbers().length > 0 ||
      this.availablePageNumbers().length > 0
    );
  }

  static sourceAttribution(): string {
    return `বাংলা অনুবাদ: ${document.bengaliSource.name} • V${document.bengaliSource.version} • QuranEnc.com`;
  }
}

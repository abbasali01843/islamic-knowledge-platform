import type { HadithGrade, HadithItem } from '../types/hadith';

const API_ROOT = 'https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1';
const CACHE_KEY = 'ikp_hadith_api_cache_v1';
const CACHE_TTL_MS = 24 * 60 * 60 * 1000;

type ApiHadith = { hadithnumber?: number | string; hadithNumber?: number | string; text?: string; hadithArabic?: string; hadithBengali?: string; reference?: { hadith?: number | string }; grades?: Array<{ grade?: string }>; grade?: string };
type CachePayload = { savedAt: number; items: HadithItem[] };
const BOOKS = [
  { id: 'bukhari', api: 'bukhari', name: 'সহীহুল বুখারী' },
  { id: 'muslim', api: 'muslim', name: 'সহীহ মুসলিম' },
  { id: 'tirmidhi', api: 'tirmidhi', name: 'জামে আত-তিরমিযী' },
] as const;

async function getJson(url: string): Promise<unknown> {
  const response = await fetch(url, { headers: { Accept: 'application/json' } });
  if (!response.ok) throw new Error('Hadith API ' + response.status);
  return response.json();
}
function rows(payload: unknown): ApiHadith[] {
  if (Array.isArray(payload)) return payload as ApiHadith[];
  if (payload && typeof payload === 'object') {
    const value = payload as { hadiths?: unknown; data?: unknown };
    const data = value.hadiths ?? value.data;
    if (Array.isArray(data)) return data as ApiHadith[];
  }
  return [];
}
function numberOf(item: ApiHadith, fallback: number): number {
  const value = item.hadithnumber ?? item.hadithNumber ?? item.reference?.hadith ?? fallback;
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : fallback;
}
function gradeOf(item: ApiHadith): HadithGrade {
  const raw = String(item.grade ?? item.grades?.[0]?.grade ?? '').toLowerCase();
  return raw.includes('hasan') ? 'HASAN' : 'SAHIH';
}
function normalizePair(book: (typeof BOOKS)[number], arabic: ApiHadith[], bengali: ApiHadith[]): HadithItem[] {
  const byNumber = new Map<number, ApiHadith>();
  bengali.forEach((item, index) => byNumber.set(numberOf(item, index + 1), item));
  return arabic.map((ar, index) => {
    const number = numberOf(ar, index + 1);
    const bn = byNumber.get(number);
    const grade = gradeOf(ar);
    return {
      id: 'api-' + book.id + '-' + number, bookId: book.id, bookNameBengali: book.name, hadithNumber: number,
      chapterNameBengali: 'অনলাইন হাদিস সংগ্রহ', narratorBengali: '',
      arabicText: ar.text ?? ar.hadithArabic ?? '', bengaliText: bn?.text ?? bn?.hadithBengali ?? '',
      grade, gradeLabelBengali: grade === 'HASAN' ? 'হাসান' : 'সহীহ', topicId: 'ALL', tags: ['api', book.id]
    };
  }).filter(item => item.arabicText && item.bengaliText);
}
async function fetchSection(book: (typeof BOOKS)[number], section: number): Promise<HadithItem[]> {
  const [arabic, bengali] = await Promise.all([
    getJson(API_ROOT + '/editions/ara-' + book.api + '/sections/' + section + '.json'),
    getJson(API_ROOT + '/editions/ben-' + book.api + '/sections/' + section + '.json'),
  ]);
  return normalizePair(book, rows(arabic), rows(bengali));
}
function readCache(): HadithItem[] {
  try { const raw = localStorage.getItem(CACHE_KEY); if (!raw) return []; const parsed = JSON.parse(raw) as CachePayload; return Date.now() - parsed.savedAt <= CACHE_TTL_MS && Array.isArray(parsed.items) ? parsed.items : []; } catch { return []; }
}
function writeCache(items: HadithItem[]) { try { localStorage.setItem(CACHE_KEY, JSON.stringify({ savedAt: Date.now(), items })); } catch { /* optional */ } }
export async function fetchLiveHadiths(): Promise<{ items: HadithItem[]; fromCache: boolean }> {
  const cached = readCache();
  if (cached.length) return { items: cached, fromCache: true };
  const results = await Promise.allSettled(BOOKS.map(book => fetchSection(book, 1)));
  const items = results.flatMap(result => result.status === 'fulfilled' ? result.value : []);
  if (!items.length) throw new Error('Hadith API unavailable');
  writeCache(items);
  return { items, fromCache: false };
}
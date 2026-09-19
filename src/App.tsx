import React, { useState, useEffect } from 'react';
import { Sun, Moon } from 'lucide-react';
import { Surah, HomeDestination } from './types';
import { findQuranSurah } from './data/quranCatalog';
import { HomeScreen } from './components/HomeScreen';
import { QuranScreen } from './components/QuranScreen';
import { QuranReaderScreen } from './components/QuranReaderScreen';
import { PrayerTimesScreen } from './components/prayer/PrayerTimesScreen';
import { DuaScreen } from './components/dua/DuaScreen';
import { HadithScreen } from './components/hadith/HadithScreen';
import { LearnSalahScreen } from './components/learn/LearnSalahScreen';
import { ZakatScreen } from './components/zakat/ZakatScreen';
import { CalendarScreen } from './components/calendar/CalendarScreen';
import { Navbar } from './components/Navbar';
import { WebModulesScreen } from './components/WebModulesScreen';

export const App: React.FC = () => {
  const [selectedTab, setSelectedTab] = useState<number>(0);
  const [selectedSurahNumber, setSelectedSurahNumber] = useState<number>(0);
  const [selectedAyah, setSelectedAyah] = useState<number>(0);
  const [showWebModules, setShowWebModules] = useState(false);
  const [activeSpecialModule, setActiveSpecialModule] = useState<
    'LEARN_SALAH' | 'ZAKAT' | 'CALENDAR' | null
  >(null);
  const [isDarkMode, setIsDarkMode] = useState<boolean>(() => {
    if (typeof window !== 'undefined') {
      const saved = localStorage.getItem('app_theme');
      if (saved) return saved === 'dark';
      return window.matchMedia('(prefers-color-scheme: dark)').matches;
    }
    return false;
  });

  useEffect(() => {
    if (isDarkMode) {
      document.documentElement.classList.add('dark');
      localStorage.setItem('app_theme', 'dark');
    } else {
      document.documentElement.classList.remove('dark');
      localStorage.setItem('app_theme', 'light');
    }
  }, [isDarkMode]);

  const applyRoute = () => {
    const path = window.location.pathname.replace(/\/+$/, '') || '/';
    const queryModule = new URLSearchParams(window.location.search).get('module');
    setShowWebModules(false);
    setActiveSpecialModule(null);
    setSelectedSurahNumber(0);
    setSelectedAyah(0);

    if (queryModule === 'quran') { setSelectedTab(1); return; }
    if (queryModule === 'hadith') { setSelectedTab(4); return; }

    if (path.startsWith('/quran/')) {
      const n = Number(path.split('/')[2]);
      if (Number.isInteger(n) && findQuranSurah(n)) {
        setSelectedTab(1); setSelectedSurahNumber(n); return;
      }
    }
    const tabs: Record<string, number> = {'/':0,'/quran':1,'/prayer':2,'/dua':3,'/hadith':4};
    if (tabs[path] !== undefined) { setSelectedTab(tabs[path]); return; }
    if (path === '/learn/salah') { setActiveSpecialModule('LEARN_SALAH'); return; }
    if (path === '/zakat') { setActiveSpecialModule('ZAKAT'); return; }
    if (path === '/calendar') { setActiveSpecialModule('CALENDAR'); return; }
    if (['/qibla','/ramadan','/hajj','/seerah','/quiz'].includes(path)) setShowWebModules(true);
  };

  const navigate = (path: string) => {
    window.history.pushState({}, '', path);
    applyRoute();
  };

  useEffect(() => {
    applyRoute();
    const onPopState = () => applyRoute();
    window.addEventListener('popstate', onPopState);
    return () => window.removeEventListener('popstate', onPopState);
  }, []);

  const selectedSurah = findQuranSurah(selectedSurahNumber);
  const isReaderOpen = selectedTab === 1 && selectedSurah !== undefined;

  const openQuran = () => {
    navigate('/quran');
    setActiveSpecialModule(null);
    setSelectedTab(1);
    setSelectedSurahNumber(0);
    setSelectedAyah(0);
  };

  const openSurah = (surah: Surah, ayahNumber: number | null) => {
    navigate('/quran/' + surah.number);
    setActiveSpecialModule(null);
    setSelectedTab(1);
    setSelectedSurahNumber(surah.number);
    setSelectedAyah(ayahNumber || 0);
  };

  const handleQuickAction = (dest: HomeDestination) => {
    switch (dest) {
      case 'QURAN':
        openQuran();
        break;
      case 'PRAYER':
        navigate('/prayer');
        setActiveSpecialModule(null);
        setSelectedTab(2);
        setSelectedSurahNumber(0);
        setSelectedAyah(0);
        break;
      case 'DUA':
        navigate('/dua');
        setActiveSpecialModule(null);
        setSelectedTab(3);
        setSelectedSurahNumber(0);
        setSelectedAyah(0);
        break;
      case 'HADITH':
        navigate('/hadith');
        setActiveSpecialModule(null);
        setSelectedTab(4);
        setSelectedSurahNumber(0);
        setSelectedAyah(0);
        break;
      case 'LEARN_SALAH':
        navigate('/learn/salah');
        setActiveSpecialModule('LEARN_SALAH');
        break;
      case 'ZAKAT':
        navigate('/zakat');
        setActiveSpecialModule('ZAKAT');
        break;
      case 'CALENDAR':
        navigate('/calendar');
        setActiveSpecialModule('CALENDAR');
        break;
    }
  };

  return (
    <div className="min-h-screen bg-[#F7FAF7] dark:bg-[#101511] text-[#181D19] dark:text-[#E1E5E1] transition-colors flex flex-col justify-between">
      {/* Top Bar for non-reader views */}
      {!isReaderOpen && !activeSpecialModule && (
        <header className="sticky top-0 z-30 bg-[#F7FAF7]/95 dark:bg-[#101511]/95 backdrop-blur-xs border-b border-[#E8EFEA] dark:border-[#3A4D43]/60 px-4 py-2.5">
          <div className="max-w-2xl mx-auto flex items-center justify-between">
            <div className="flex items-center gap-2">
              <span className="font-bold text-base tracking-tight text-[#176B4D] dark:text-[#9DD6B9]">
                ইসলামিক জ্ঞান
              </span>
              <span className="text-xs px-2 py-0.5 rounded-full bg-[#D4F2E2] text-[#002114] dark:bg-[#005236] dark:text-[#D4F2E2] font-semibold">
                v0.4.0
              </span>
            </div>
            <button type="button" onClick={() => navigate('/qibla')} className="px-3 py-2 rounded-xl bg-[#D4F2E2] dark:bg-[#005236] text-[#176B4D] dark:text-[#D4F2E2] text-xs font-bold">আরও</button>
            <button
              type="button"
              onClick={() => setIsDarkMode(!isDarkMode)}
              title={isDarkMode ? 'লাইট মোড' : 'ডার্ক মোড'}
              className="p-2 rounded-xl text-[#414A45] dark:text-[#C1CAC4] hover:bg-[#E8EFEA] dark:hover:bg-[#3F4943] transition-colors"
            >
              {isDarkMode ? <Sun className="w-5 h-5" /> : <Moon className="w-5 h-5" />}
            </button>
          </div>
        </header>
      )}

      {/* Main Content Area */}
      <main className="flex-1">
        {showWebModules ? <WebModulesScreen onBack={() => setShowWebModules(false)} /> : null}
        {!showWebModules && (activeSpecialModule === 'LEARN_SALAH' ? (
          <LearnSalahScreen onBack={() => setActiveSpecialModule(null)} />
        ) : activeSpecialModule === 'ZAKAT' ? (
          <ZakatScreen onBack={() => navigate('/')} />
        ) : activeSpecialModule === 'CALENDAR' ? (
          <CalendarScreen onBack={() => navigate('/')} />
        ) : (
          <>
            {selectedTab === 0 && (
              <HomeScreen onQuickActionClick={handleQuickAction} />
            )}

            {selectedTab === 1 && (
              <>
                {!selectedSurah ? (
                  <QuranScreen onSurahClick={openSurah} />
                ) : (
                  <QuranReaderScreen
                    surah={selectedSurah}
                    initialAyah={selectedAyah > 0 ? selectedAyah : null}
                    onBack={() => navigate('/quran')}
                    onNavigateToSurah={(next) => navigate('/quran/' + next.number)}
                  />
                )}
              </>
            )}

            {selectedTab === 2 && <PrayerTimesScreen />}

            {selectedTab === 3 && <DuaScreen />}

            {selectedTab === 4 && <HadithScreen />}
          </>
        ))}
      </main>

      {/* Bottom Navbar (hidden during reading mode) */}
      {!showWebModules && !isReaderOpen && (
        <Navbar
          selectedTab={activeSpecialModule ? -1 : selectedTab}
          onSelectTab={(idx) => {
            const paths = ['/', '/quran', '/prayer', '/dua', '/hadith'];
            navigate(paths[idx] || '/');
            setActiveSpecialModule(null);
            setSelectedTab(idx);
            if (idx !== 1) {
              setSelectedSurahNumber(0);
              setSelectedAyah(0);
            }
          }}
        />
      )}
    </div>
  );
};

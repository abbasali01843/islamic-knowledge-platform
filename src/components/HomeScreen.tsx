import React from 'react';
import { BookOpen, Clock, Moon, Landmark } from 'lucide-react';
import { HomeDestination } from '../types';
import { SectionHeader } from './SectionHeader';

interface HomeScreenProps {
  onQuickActionClick: (destination: HomeDestination) => void;
}

interface QuickAction {
  title: string;
  subtitle: string;
  destination: HomeDestination;
  icon: React.ComponentType<{ className?: string }>;
}

export const HomeScreen: React.FC<HomeScreenProps> = ({ onQuickActionClick }) => {
  const actions: QuickAction[] = [
    {
      title: 'কুরআন',
      subtitle: 'পড়া ও অনুসন্ধান',
      destination: 'QURAN',
      icon: BookOpen,
    },
    {
      title: 'হাদিস',
      subtitle: 'সহিহ উৎসভিত্তিক জ্ঞান',
      destination: 'HADITH',
      icon: BookOpen,
    },
    {
      title: 'নামাজ',
      subtitle: 'আজকের সময়সূচি',
      destination: 'PRAYER',
      icon: Landmark,
    },
    {
      title: 'দোয়া ও যিকর',
      subtitle: 'দৈনন্দিন আমল',
      destination: 'DUA',
      icon: Moon,
    },
  ];

  return (
    <div className="max-w-2xl mx-auto px-4 py-6 space-y-6 pb-24">
      {/* Header Greeting */}
      <div>
        <h1 className="text-2xl font-bold tracking-tight text-[#181D19] dark:text-[#E1E5E1]">
          আসসালামু আলাইকুম
        </h1>
        <p className="text-sm text-[#414A45] dark:text-[#C1CAC4] mt-0.5">
          Islamic Knowledge Platform
        </p>
      </div>

      {/* Next Prayer Banner */}
      <div className="rounded-2xl bg-[#D4F2E2] dark:bg-[#005236] text-[#002114] dark:text-[#D4F2E2] p-5 shadow-sm">
        <div className="flex items-center gap-4">
          <div className="w-12 h-12 rounded-xl bg-white/40 dark:bg-black/20 flex items-center justify-center shrink-0">
            <Clock className="w-7 h-7 text-[#002114] dark:text-[#D4F2E2]" />
          </div>
          <div className="space-y-0.5">
            <span className="text-xs font-semibold uppercase tracking-wider text-[#002114]/80 dark:text-[#D4F2E2]/80">
              পরবর্তী নামাজ
            </span>
            <div className="text-2xl font-bold">ফজর</div>
            <p className="text-xs text-[#002114]/75 dark:text-[#D4F2E2]/75">
              স্থান ও সময় নির্ধারণ করলে লাইভ কাউন্টডাউন দেখাবে।
            </p>
          </div>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="space-y-3">
        <SectionHeader title="দ্রুত অ্যাকশন" />
        <div className="grid grid-cols-2 gap-3">
          {actions.map((action) => {
            const IconComponent = action.icon;
            return (
              <button
                key={action.title}
                type="button"
                onClick={() => onQuickActionClick(action.destination)}
                className="text-left p-4 rounded-2xl bg-[#E8EFEA] dark:bg-[#3F4943] hover:bg-[#d8e3dc] dark:hover:bg-[#4d5952] transition-colors flex flex-col justify-between space-y-3 border border-transparent active:scale-[0.99]"
              >
                <IconComponent className="w-6 h-6 text-[#176B4D] dark:text-[#9DD6B9]" />
                <div>
                  <h3 className="font-semibold text-base text-[#181D19] dark:text-[#E1E5E1]">
                    {action.title}
                  </h3>
                  <p className="text-xs text-[#414A45] dark:text-[#C1CAC4] mt-0.5">
                    {action.subtitle}
                  </p>
                </div>
              </button>
            );
          })}
        </div>
      </div>

      {/* Today's Content */}
      <div className="space-y-3">
        <SectionHeader title="আজকের কনটেন্ট" />
        <div className="p-5 rounded-2xl bg-[#E8EFEA] dark:bg-[#3F4943] space-y-2 border border-black/5 dark:border-white/5">
          <h3 className="font-semibold text-base text-[#181D19] dark:text-[#E1E5E1]">
            দৈনিক আয়াত
          </h3>
          <p className="text-sm text-[#414A45] dark:text-[#C1CAC4] leading-relaxed">
            কনটেন্ট ইঞ্জিন যুক্ত হলে এখানে উৎসসহ আয়াত দেখানো হবে।
          </p>
        </div>
      </div>
    </div>
  );
};

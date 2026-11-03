import React from 'react';
import { ClipboardList, Clock, CheckCircle2 } from 'lucide-react';

interface SummaryCardsProps {
  total: number;
  pending: number;
  resolved: number;
}

export const SummaryCards: React.FC<SummaryCardsProps> = ({ total, pending, resolved }) => {
  const cards = [
    {
      title: 'Total Complaints',
      count: total,
      description: 'All recorded campus issues',
      icon: ClipboardList,
      bgColor: 'bg-blue-50',
      iconColor: 'text-blue-700',
      badgeColor: 'bg-blue-100 text-blue-800',
    },
    {
      title: 'Pending Complaints',
      count: pending,
      description: 'Requires attention or ongoing',
      icon: Clock,
      bgColor: 'bg-amber-50',
      iconColor: 'text-amber-700',
      badgeColor: 'bg-amber-100 text-amber-800',
    },
    {
      title: 'Resolved Complaints',
      count: resolved,
      description: 'Successfully fixed issues',
      icon: CheckCircle2,
      bgColor: 'bg-emerald-50',
      iconColor: 'text-emerald-700',
      badgeColor: 'bg-emerald-100 text-emerald-800',
    },
  ];

  return (
    <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 lg:gap-6">
      {cards.map((card) => {
        const Icon = card.icon;
        return (
          <div
            key={card.title}
            className="bg-white rounded-2xl p-5 border border-slate-200/90 shadow-xs hover:shadow-md transition-all duration-200"
          >
            <div className="flex items-center justify-between">
              <div className={`w-12 h-12 rounded-xl ${card.bgColor} flex items-center justify-center ${card.iconColor}`}>
                <Icon className="w-6 h-6" />
              </div>
              <span className={`text-xs font-semibold px-2.5 py-1 rounded-full ${card.badgeColor}`}>
                {card.title.split(' ')[0]}
              </span>
            </div>
            <div className="mt-4">
              <div className="text-3xl font-extrabold text-slate-900 tracking-tight">
                {card.count}
              </div>
              <div className="text-sm font-semibold text-slate-700 mt-0.5">
                {card.title}
              </div>
              <div className="text-xs text-slate-500 mt-1">
                {card.description}
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
};

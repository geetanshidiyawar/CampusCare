import React from 'react';
import { Complaint, CATEGORIES, STATUSES } from '../types/complaint';
import { BarChart3 } from 'lucide-react';

interface DashboardAnalyticsProps {
  complaints: Complaint[];
}

export const DashboardAnalytics: React.FC<DashboardAnalyticsProps> = ({ complaints }) => {
  const total = complaints.length;

  const categoryCounts = CATEGORIES.reduce((acc, cat) => {
    acc[cat] = complaints.filter((c) => c.category === cat).length;
    return acc;
  }, {} as Record<string, number>);

  const statusCounts = STATUSES.reduce((acc, st) => {
    acc[st] = complaints.filter((c) => c.status === st).length;
    return acc;
  }, {} as Record<string, number>);

  const categoryColor = (cat: string) => {
    switch (cat) {
      case 'Wi-Fi':
        return 'bg-blue-600';
      case 'Hostel':
        return 'bg-teal-600';
      case 'Classroom':
        return 'bg-amber-500';
      case 'Laboratory':
        return 'bg-purple-600';
      default:
        return 'bg-slate-500';
    }
  };

  return (
    <div className="bg-white rounded-2xl border border-slate-200/90 p-5 sm:p-6 shadow-xs">
      <div className="flex items-center gap-2.5 pb-4 border-b border-slate-100">
        <div className="w-9 h-9 rounded-xl bg-blue-100 flex items-center justify-center text-blue-700 shrink-0">
          <BarChart3 className="w-5 h-5" />
        </div>
        <div>
          <h2 className="text-base font-bold text-slate-900">
            Campus Analytics Overview
          </h2>
          <p className="text-xs text-slate-500">
            Real-time distribution across campus departments and resolution pipeline
          </p>
        </div>
      </div>

      <div className="mt-5 grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Category Breakdown */}
        <div>
          <h3 className="text-xs font-bold text-slate-700 uppercase tracking-wider mb-3">
            Complaints by Category
          </h3>
          <div className="space-y-3">
            {CATEGORIES.map((category) => {
              const count = categoryCounts[category] || 0;
              const percentage = total > 0 ? Math.round((count / total) * 100) : 0;
              return (
                <div key={category} className="space-y-1">
                  <div className="flex items-center justify-between text-xs">
                    <span className="font-semibold text-slate-700">{category}</span>
                    <span className="font-bold text-slate-900">
                      {count} <span className="text-slate-400 font-normal">({percentage}%)</span>
                    </span>
                  </div>
                  <div className="w-full h-2 rounded-full bg-slate-100 overflow-hidden">
                    <div
                      className={`h-full rounded-full transition-all duration-500 ${categoryColor(category)}`}
                      style={{ width: `${percentage}%` }}
                    />
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        {/* Status Pipeline Breakdown */}
        <div>
          <h3 className="text-xs font-bold text-slate-700 uppercase tracking-wider mb-3">
            Status Pipeline Breakdown
          </h3>
          <div className="grid grid-cols-3 gap-2.5">
            <div className="p-3 rounded-xl bg-amber-50 border border-amber-200/80 text-center">
              <span className="text-2xl font-extrabold text-amber-700 block">
                {statusCounts['Raised'] || 0}
              </span>
              <span className="text-[11px] font-bold text-amber-800 uppercase tracking-wide block mt-0.5">
                Raised
              </span>
              <span className="text-[10px] text-amber-600 block mt-0.5">
                {total > 0 ? Math.round(((statusCounts['Raised'] || 0) / total) * 100) : 0}% of total
              </span>
            </div>

            <div className="p-3 rounded-xl bg-blue-50 border border-blue-200/80 text-center">
              <span className="text-2xl font-extrabold text-blue-700 block">
                {statusCounts['In Progress'] || 0}
              </span>
              <span className="text-[11px] font-bold text-blue-800 uppercase tracking-wide block mt-0.5">
                In Progress
              </span>
              <span className="text-[10px] text-blue-600 block mt-0.5">
                {total > 0 ? Math.round(((statusCounts['In Progress'] || 0) / total) * 100) : 0}% of total
              </span>
            </div>

            <div className="p-3 rounded-xl bg-emerald-50 border border-emerald-200/80 text-center">
              <span className="text-2xl font-extrabold text-emerald-700 block">
                {statusCounts['Resolved'] || 0}
              </span>
              <span className="text-[11px] font-bold text-emerald-800 uppercase tracking-wide block mt-0.5">
                Resolved
              </span>
              <span className="text-[10px] text-emerald-600 block mt-0.5">
                {total > 0 ? Math.round(((statusCounts['Resolved'] || 0) / total) * 100) : 0}% of total
              </span>
            </div>
          </div>

          <div className="mt-4 p-3 rounded-xl bg-slate-50 border border-slate-200 text-xs text-slate-600 flex items-center justify-between">
            <span className="font-medium">Total Registered Incidents:</span>
            <span className="font-bold text-slate-900">{total} Complaints</span>
          </div>
        </div>
      </div>
    </div>
  );
};

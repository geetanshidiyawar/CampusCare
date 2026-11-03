import React, { useState } from 'react';
import { Complaint, ComplaintStatus } from '../types/complaint';
import { calculateUrgencyScore, getHoursPending, formatComplaintAge } from '../utils/urgency';
import { StatusDropdown, UrgencyBadge, CategoryBadge } from './Badges';
import { 
  Flame, 
  ChevronRight, 
  CheckCircle2, 
  Clock, 
  TrendingUp, 
  Info 
} from 'lucide-react';

interface PriorityQueueProps {
  complaints: Complaint[];
  onStatusChange: (complaintId: string, newStatus: ComplaintStatus) => void;
  onViewAll: () => void;
}

export const PriorityQueue: React.FC<PriorityQueueProps> = ({
  complaints,
  onStatusChange,
  onViewAll,
}) => {
  const [showFormulaDetails, setShowFormulaDetails] = useState(false);

  return (
    <div className="bg-white rounded-2xl border border-slate-200/90 p-5 sm:p-6 shadow-xs">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3 pb-5 border-b border-slate-100">
        <div className="flex items-center gap-3">
          <div className="w-10 h-10 rounded-xl bg-red-100 flex items-center justify-center text-red-600 shrink-0">
            <Flame className="w-5 h-5" />
          </div>
          <div>
            <div className="flex items-center gap-2">
              <h2 className="text-lg font-bold text-slate-900">
                Top 3 Priority Complaints
              </h2>
              <span className="px-2 py-0.5 text-[11px] font-bold rounded-full bg-red-100 text-red-700">
                High Urgency Queue
              </span>
            </div>
            <p className="text-xs text-slate-500 mt-0.5">
              Ranked dynamically by campus urgency scoring algorithm
            </p>
          </div>
        </div>

        <div className="flex items-center gap-2">
          <button
            type="button"
            onClick={() => setShowFormulaDetails(!showFormulaDetails)}
            className={`inline-flex items-center gap-1.5 px-3 py-1.5 text-xs font-semibold rounded-lg transition-colors border ${
              showFormulaDetails
                ? 'bg-blue-50 text-blue-700 border-blue-200'
                : 'text-slate-600 hover:text-slate-900 border-slate-200 hover:bg-slate-50'
            }`}
          >
            <Info className="w-3.5 h-3.5" />
            <span>{showFormulaDetails ? 'Hide Formula' : 'Urgency Formula'}</span>
          </button>

          <button
            type="button"
            onClick={onViewAll}
            className="inline-flex items-center gap-1 text-xs font-semibold text-blue-700 hover:text-blue-800 ml-1 hover:underline"
          >
            <span>View All</span>
            <ChevronRight className="w-4 h-4" />
          </button>
        </div>
      </div>

      {/* Formula Explanation Collapsible Banner */}
      {showFormulaDetails && (
        <div className="my-4 p-4 rounded-xl bg-blue-50/80 border border-blue-200 text-slate-800 text-xs space-y-2 animate-in fade-in duration-150">
          <div className="flex items-center gap-1.5 font-bold text-blue-900">
            <TrendingUp className="w-4 h-4 text-blue-700" />
            <span>Urgency Scoring Algorithm</span>
          </div>
          <div className="bg-white/80 p-2.5 rounded-lg border border-blue-200 font-mono text-[11px] text-slate-900">
            Urgency Score = Category Weight + floor(Hours Pending / 24)
          </div>
          <div className="grid grid-cols-2 sm:grid-cols-4 gap-2 pt-1">
            <div className="bg-white/70 p-2 rounded-lg border border-blue-100">
              <span className="font-semibold block text-slate-700">Laboratory</span>
              <span className="text-blue-700 font-bold">Weight: 3</span>
            </div>
            <div className="bg-white/70 p-2 rounded-lg border border-blue-100">
              <span className="font-semibold block text-slate-700">Wi-Fi</span>
              <span className="text-blue-700 font-bold">Weight: 2</span>
            </div>
            <div className="bg-white/70 p-2 rounded-lg border border-blue-100">
              <span className="font-semibold block text-slate-700">Hostel</span>
              <span className="text-blue-700 font-bold">Weight: 2</span>
            </div>
            <div className="bg-white/70 p-2 rounded-lg border border-blue-100">
              <span className="font-semibold block text-slate-700">Classroom</span>
              <span className="text-blue-700 font-bold">Weight: 1</span>
            </div>
          </div>
          <p className="text-[11px] text-slate-600 pt-1">
            • Tie-breaking: Higher urgency score first → Older complaint first → Lower complaint ID first.<br />
            • Resolved complaints are immediately excluded from this priority queue.
          </p>
        </div>
      )}

      {/* Complaints List or Empty State */}
      <div className="mt-5 space-y-3.5">
        {complaints.length === 0 ? (
          <div className="py-10 px-4 text-center rounded-xl bg-slate-50 border border-dashed border-slate-200">
            <div className="w-12 h-12 mx-auto rounded-full bg-emerald-100 text-emerald-700 flex items-center justify-center mb-3">
              <CheckCircle2 className="w-6 h-6" />
            </div>
            <h3 className="text-sm font-bold text-slate-900">
              All complaints have been resolved. Great work!
            </h3>
            <p className="text-xs text-slate-500 mt-1 max-w-sm mx-auto">
              There are no unresolved complaints pending in the queue right now.
            </p>
          </div>
        ) : (
          complaints.map((complaint, index) => {
            const urgencyScore = calculateUrgencyScore(complaint);
            const hoursPending = getHoursPending(complaint.submittedAt);
            const isRankOne = index === 0;

            return (
              <div
                key={complaint.id}
                className={`p-4 sm:p-5 rounded-xl border transition-all duration-200 ${
                  isRankOne
                    ? 'bg-amber-50/40 border-amber-300 ring-1 ring-amber-300/60 shadow-xs'
                    : 'bg-white border-slate-200 hover:border-slate-300'
                }`}
              >
                <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2.5">
                  <div className="flex items-center gap-2.5 flex-wrap">
                    {/* Rank Badge */}
                    <span
                      className={`w-6 h-6 rounded-full flex items-center justify-center text-xs font-extrabold ${
                        isRankOne
                          ? 'bg-amber-600 text-white'
                          : 'bg-slate-700 text-white'
                      }`}
                    >
                      #{index + 1}
                    </span>

                    {/* ID */}
                    <span className="font-mono text-sm font-bold text-slate-900">
                      {complaint.id}
                    </span>

                    {/* Category */}
                    <CategoryBadge category={complaint.category} />

                    {/* Top Urgency Ribbon for #1 */}
                    {isRankOne && (
                      <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-extrabold bg-red-100 text-red-700 border border-red-200 uppercase tracking-wide">
                        <Flame className="w-3 h-3 text-red-600" />
                        Highest Urgency
                      </span>
                    )}
                  </div>

                  <UrgencyBadge score={urgencyScore} isTopRank={isRankOne} />
                </div>

                {/* Description */}
                <p className="text-sm text-slate-800 font-medium mt-3 leading-relaxed">
                  {complaint.description}
                </p>

                {/* Footer metadata & status action */}
                <div className="mt-4 pt-3 border-t border-slate-100 flex flex-col sm:flex-row sm:items-center justify-between gap-3">
                  <div className="flex items-center gap-2 text-xs text-slate-500">
                    <Clock className="w-3.5 h-3.5 text-slate-400" />
                    <span>
                      <strong className="text-slate-700 font-semibold">{hoursPending}h</strong> pending ({formatComplaintAge(complaint.submittedAt)})
                    </span>
                  </div>

                  <div className="flex items-center gap-2">
                    <span className="text-xs text-slate-500 hidden sm:inline">Status:</span>
                    <StatusDropdown
                      currentStatus={complaint.status}
                      onStatusChange={(newStatus) => onStatusChange(complaint.id, newStatus)}
                    />
                  </div>
                </div>
              </div>
            );
          })
        )}
      </div>
    </div>
  );
};

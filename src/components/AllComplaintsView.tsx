import React, { useState, useMemo } from 'react';
import { Complaint, ComplaintStatus, CATEGORIES, STATUSES } from '../types/complaint';
import { calculateUrgencyScore, getHoursPending, formatComplaintAge, formatSubmissionDate } from '../utils/urgency';
import { StatusDropdown, UrgencyBadge, CategoryBadge, StatusBadge } from './Badges';
import { 
  Search, 
  X, 
  Inbox, 
  Plus, 
  Clock, 
  Calendar, 
  RotateCcw
} from 'lucide-react';

interface AllComplaintsViewProps {
  complaints: Complaint[];
  onStatusChange: (complaintId: string, newStatus: ComplaintStatus) => void;
  onNavigateToSubmit: () => void;
}

export const AllComplaintsView: React.FC<AllComplaintsViewProps> = ({
  complaints,
  onStatusChange,
  onNavigateToSubmit,
}) => {
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<string>('All Categories');
  const [selectedStatus, setSelectedStatus] = useState<string>('All Statuses');
  const [sortBy, setSortBy] = useState<'newest' | 'urgency' | 'oldest'>('newest');

  // Combined search and filtering logic
  const filteredComplaints = useMemo(() => {
    const query = searchQuery.trim().toLowerCase();

    return complaints
      .filter((complaint) => {
        const matchesCategory =
          selectedCategory === 'All Categories' || complaint.category === selectedCategory;

        const matchesStatus =
          selectedStatus === 'All Statuses' || complaint.status === selectedStatus;

        const matchesQuery =
          !query ||
          complaint.id.toLowerCase().includes(query) ||
          complaint.description.toLowerCase().includes(query) ||
          complaint.category.toLowerCase().includes(query);

        return matchesCategory && matchesStatus && matchesQuery;
      })
      .sort((a, b) => {
        if (sortBy === 'urgency') {
          const scoreA = calculateUrgencyScore(a);
          const scoreB = calculateUrgencyScore(b);
          if (scoreB !== scoreA) return scoreB - scoreA;
          return a.submittedAt - b.submittedAt;
        }
        if (sortBy === 'oldest') {
          return a.submittedAt - b.submittedAt;
        }
        // newest
        return b.submittedAt - a.submittedAt;
      });
  }, [complaints, searchQuery, selectedCategory, selectedStatus, sortBy]);

  const isFilterActive =
    searchQuery.trim().length > 0 ||
    selectedCategory !== 'All Categories' ||
    selectedStatus !== 'All Statuses';

  const clearAllFilters = () => {
    setSearchQuery('');
    setSelectedCategory('All Categories');
    setSelectedStatus('All Statuses');
    setSortBy('newest');
  };

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 className="text-xl font-extrabold text-slate-900 tracking-tight">
            All Campus Complaints
          </h2>
          <p className="text-xs text-slate-500 mt-1">
            Search, filter, track status, and monitor urgency across all facility records
          </p>
        </div>

        <button
          type="button"
          onClick={onNavigateToSubmit}
          className="inline-flex items-center justify-center gap-2 px-4 py-2.5 text-xs font-bold text-white bg-blue-700 hover:bg-blue-800 rounded-xl shadow-xs transition-colors self-start sm:self-auto"
        >
          <Plus className="w-4 h-4" />
          <span>New Complaint</span>
        </button>
      </div>

      {/* Search and Filters Bar */}
      <div className="bg-white rounded-2xl border border-slate-200/90 p-4 sm:p-5 shadow-xs space-y-4">
        <div className="flex flex-col md:flex-row items-stretch md:items-center gap-3">
          {/* Search Box */}
          <div className="relative flex-1">
            <Search className="w-4 h-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2 pointer-events-none" />
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search by ID (e.g. C01), description, or category..."
              className="w-full pl-10 pr-9 py-2.5 text-xs bg-slate-50 border border-slate-200 rounded-xl focus:bg-white focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-transparent text-slate-800 placeholder:text-slate-400 transition-all"
            />
            {searchQuery && (
              <button
                type="button"
                onClick={() => setSearchQuery('')}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600"
                aria-label="Clear search"
              >
                <X className="w-4 h-4" />
              </button>
            )}
          </div>

          {/* Quick Sort Control */}
          <div className="flex items-center gap-2">
            <span className="text-xs font-semibold text-slate-500 shrink-0">Sort by:</span>
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value as any)}
              className="px-3 py-2 text-xs bg-slate-50 border border-slate-200 rounded-xl font-medium text-slate-700 focus:outline-none focus:ring-2 focus:ring-blue-600 cursor-pointer"
            >
              <option value="newest">Newest First</option>
              <option value="urgency">Urgency Score</option>
              <option value="oldest">Oldest Pending First</option>
            </select>
          </div>
        </div>

        {/* Filter Pills */}
        <div className="flex flex-wrap items-center gap-3 pt-2 border-t border-slate-100">
          {/* Category Dropdown */}
          <div className="flex items-center gap-2">
            <span className="text-xs font-semibold text-slate-500">Category:</span>
            <select
              value={selectedCategory}
              onChange={(e) => setSelectedCategory(e.target.value)}
              className="px-3 py-1.5 text-xs bg-slate-50 border border-slate-200 rounded-lg font-medium text-slate-700 focus:outline-none focus:ring-2 focus:ring-blue-600 cursor-pointer"
            >
              <option value="All Categories">All Categories</option>
              {CATEGORIES.map((cat) => (
                <option key={cat} value={cat}>
                  {cat}
                </option>
              ))}
            </select>
          </div>

          {/* Status Dropdown */}
          <div className="flex items-center gap-2">
            <span className="text-xs font-semibold text-slate-500">Status:</span>
            <select
              value={selectedStatus}
              onChange={(e) => setSelectedStatus(e.target.value)}
              className="px-3 py-1.5 text-xs bg-slate-50 border border-slate-200 rounded-lg font-medium text-slate-700 focus:outline-none focus:ring-2 focus:ring-blue-600 cursor-pointer"
            >
              <option value="All Statuses">All Statuses</option>
              {STATUSES.map((st) => (
                <option key={st} value={st}>
                  {st}
                </option>
              ))}
            </select>
          </div>

          {/* Clear Filters Button */}
          {isFilterActive && (
            <button
              type="button"
              onClick={clearAllFilters}
              className="inline-flex items-center gap-1.5 px-3 py-1.5 text-xs font-semibold text-blue-700 bg-blue-50 hover:bg-blue-100 rounded-lg transition-colors border border-blue-200"
            >
              <RotateCcw className="w-3.5 h-3.5" />
              <span>Clear Filters</span>
            </button>
          )}

          <div className="ml-auto text-xs text-slate-500 font-medium">
            Showing <strong className="text-slate-800">{filteredComplaints.length}</strong> of{' '}
            <strong className="text-slate-800">{complaints.length}</strong>
          </div>
        </div>
      </div>

      {/* Complaints List */}
      <div className="space-y-3.5">
        {complaints.length === 0 ? (
          <div className="py-16 px-4 text-center rounded-2xl bg-white border border-slate-200/90 shadow-xs">
            <div className="w-12 h-12 mx-auto rounded-full bg-slate-100 text-slate-500 flex items-center justify-center mb-3">
              <Inbox className="w-6 h-6" />
            </div>
            <h3 className="text-base font-bold text-slate-900">
              No complaints have been submitted yet.
            </h3>
            <p className="text-xs text-slate-500 mt-1 max-w-sm mx-auto">
              Get started by registering the first campus facility complaint.
            </p>
            <button
              type="button"
              onClick={onNavigateToSubmit}
              className="mt-4 inline-flex items-center gap-2 px-4 py-2 text-xs font-bold text-white bg-blue-700 hover:bg-blue-800 rounded-xl transition-colors"
            >
              <Plus className="w-4 h-4" />
              <span>Submit First Complaint</span>
            </button>
          </div>
        ) : filteredComplaints.length === 0 ? (
          <div className="py-16 px-4 text-center rounded-2xl bg-white border border-slate-200/90 shadow-xs">
            <div className="w-12 h-12 mx-auto rounded-full bg-amber-50 text-amber-600 flex items-center justify-center mb-3">
              <Search className="w-6 h-6" />
            </div>
            <h3 className="text-base font-bold text-slate-900">
              No complaints match your search or filters.
            </h3>
            <p className="text-xs text-slate-500 mt-1 max-w-sm mx-auto">
              Try adjusting your search query, or clear your active category and status filters.
            </p>
            <button
              type="button"
              onClick={clearAllFilters}
              className="mt-4 inline-flex items-center gap-1.5 px-4 py-2 text-xs font-semibold text-blue-700 bg-blue-50 hover:bg-blue-100 rounded-xl transition-colors border border-blue-200"
            >
              <RotateCcw className="w-3.5 h-3.5" />
              <span>Clear Filters</span>
            </button>
          </div>
        ) : (
          filteredComplaints.map((complaint) => {
            const urgencyScore = calculateUrgencyScore(complaint);
            const hoursPending = getHoursPending(complaint.submittedAt);

            return (
              <div
                key={complaint.id}
                className="bg-white rounded-2xl border border-slate-200/90 p-5 shadow-xs hover:shadow-md transition-all duration-200"
              >
                <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2.5">
                  <div className="flex items-center gap-2.5 flex-wrap">
                    <span className="font-mono text-xs font-extrabold px-2.5 py-1 rounded-lg bg-slate-100 text-slate-900 border border-slate-200">
                      {complaint.id}
                    </span>
                    <CategoryBadge category={complaint.category} showWeight />
                    <StatusBadge status={complaint.status} size="sm" />
                  </div>

                  <UrgencyBadge score={urgencyScore} />
                </div>

                <p className="text-sm text-slate-900 font-medium mt-3 leading-relaxed">
                  {complaint.description}
                </p>

                <div className="mt-4 pt-3.5 border-t border-slate-100 flex flex-col sm:flex-row sm:items-center justify-between gap-3 text-xs text-slate-500">
                  <div className="flex flex-wrap items-center gap-x-4 gap-y-1">
                    <div className="flex items-center gap-1.5">
                      <Calendar className="w-3.5 h-3.5 text-slate-400" />
                      <span>{formatSubmissionDate(complaint.submittedAt)}</span>
                    </div>
                    <div className="flex items-center gap-1.5">
                      <Clock className="w-3.5 h-3.5 text-slate-400" />
                      <span>{hoursPending}h elapsed ({formatComplaintAge(complaint.submittedAt)})</span>
                    </div>
                  </div>

                  <div className="flex items-center gap-2 self-end sm:self-auto">
                    <span className="font-semibold text-slate-600">Change Status:</span>
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

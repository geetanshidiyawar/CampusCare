import React, { useState, useEffect, useMemo } from 'react';
import { Complaint, ComplaintCategory, ComplaintStatus } from './types/complaint';
import { loadComplaints, saveComplaints, generateNextComplaintId, resetToSampleComplaints } from './utils/storage';
import { getTopPriorityComplaints } from './utils/urgency';
import { Navbar } from './components/Navbar';
import { SummaryCards } from './components/SummaryCards';
import { PriorityQueue } from './components/PriorityQueue';
import { DashboardAnalytics } from './components/DashboardAnalytics';
import { AllComplaintsView } from './components/AllComplaintsView';
import { ComplaintForm } from './components/ComplaintForm';
import { PlusCircle, ShieldCheck } from 'lucide-react';

export const App: React.FC = () => {
  const [complaints, setComplaints] = useState<Complaint[]>([]);
  const [currentTab, setCurrentTab] = useState<'dashboard' | 'all' | 'submit'>('dashboard');

  // Load complaints from localStorage once on initial mount
  useEffect(() => {
    const loaded = loadComplaints();
    setComplaints(loaded);
  }, []);

  // Sync to localStorage whenever complaints state changes
  const updateComplaints = (newComplaints: Complaint[]) => {
    setComplaints(newComplaints);
    saveComplaints(newComplaints);
  };

  // Dynamic counts calculated from live complaints data
  const totalCount = complaints.length;
  const pendingCount = useMemo(
    () => complaints.filter((c) => c.status !== 'Resolved').length,
    [complaints]
  );
  const resolvedCount = useMemo(
    () => complaints.filter((c) => c.status === 'Resolved').length,
    [complaints]
  );

  // Top 3 priority complaints (unresolved only, sorted by urgency score -> age -> id)
  const topPriority = useMemo(
    () => getTopPriorityComplaints(complaints),
    [complaints]
  );

  // Status management handler
  const handleStatusChange = (complaintId: string, newStatus: ComplaintStatus) => {
    const updated = complaints.map((c) =>
      c.id === complaintId ? { ...c, status: newStatus } : c
    );
    updateComplaints(updated);
  };

  // Submission handler
  const handleCreateComplaint = (category: ComplaintCategory, description: string): Complaint => {
    const nextId = generateNextComplaintId(complaints);
    const newComplaint: Complaint = {
      id: nextId,
      category,
      description,
      submittedAt: Date.now(),
      status: 'Raised',
    };
    const updated = [newComplaint, ...complaints];
    updateComplaints(updated);
    return newComplaint;
  };

  // Reset to initial 4 sample complaints
  const handleResetData = () => {
    const sample = resetToSampleComplaints();
    setComplaints(sample);
  };

  return (
    <div className="min-h-screen flex flex-col bg-slate-50 text-slate-900">
      {/* Top Navigation Bar */}
      <Navbar
        currentTab={currentTab}
        setCurrentTab={setCurrentTab}
        pendingCount={pendingCount}
        totalCount={totalCount}
        onResetData={handleResetData}
      />

      {/* Main Content Area */}
      <main className="flex-1 max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {currentTab === 'dashboard' && (
          <div className="space-y-8 animate-in fade-in duration-150">
            {/* Dashboard Header (Section 4.B) */}
            <div className="bg-white rounded-2xl border border-slate-200/90 p-6 sm:p-8 shadow-xs flex flex-col sm:flex-row sm:items-center justify-between gap-4">
              <div>
                <div className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full text-xs font-semibold bg-blue-50 text-blue-700 border border-blue-200 mb-2">
                  <ShieldCheck className="w-3.5 h-3.5" />
                  <span>College Hackathon Edition</span>
                </div>
                <h1 className="text-2xl sm:text-3xl font-extrabold tracking-tight text-slate-900">
                  Campus Complaint Dashboard
                </h1>
                <p className="text-sm text-slate-500 mt-1">
                  Monitor, manage, and prioritize campus complaints.
                </p>
              </div>

              <button
                type="button"
                onClick={() => setCurrentTab('submit')}
                className="inline-flex items-center justify-center gap-2 px-5 py-3 rounded-xl font-bold text-sm text-white bg-blue-700 hover:bg-blue-800 shadow-md shadow-blue-700/20 transition-all cursor-pointer self-start sm:self-auto"
              >
                <PlusCircle className="w-4 h-4" />
                <span>Submit New Complaint</span>
              </button>
            </div>

            {/* Dynamic Summary Cards (Section 4.C) */}
            <SummaryCards
              total={totalCount}
              pending={pendingCount}
              resolved={resolvedCount}
            />

            {/* Top 3 Priority Complaints Queue (Section 11 & 12) */}
            <PriorityQueue
              complaints={topPriority}
              onStatusChange={handleStatusChange}
              onViewAll={() => setCurrentTab('all')}
            />

            {/* Dashboard Analytics Overview (Section 15) */}
            <DashboardAnalytics complaints={complaints} />
          </div>
        )}

        {currentTab === 'all' && (
          <div className="animate-in fade-in duration-150">
            <AllComplaintsView
              complaints={complaints}
              onStatusChange={handleStatusChange}
              onNavigateToSubmit={() => setCurrentTab('submit')}
            />
          </div>
        )}

        {currentTab === 'submit' && (
          <div className="animate-in fade-in duration-150">
            <ComplaintForm
              onSubmit={handleCreateComplaint}
              onViewAll={() => setCurrentTab('all')}
            />
          </div>
        )}
      </main>

      {/* Footer */}
      <footer className="mt-auto border-t border-slate-200 bg-white py-6 text-center text-xs text-slate-500">
        <div className="max-w-7xl mx-auto px-4 flex flex-col sm:flex-row items-center justify-between gap-3">
          <div className="flex items-center gap-2 font-semibold text-slate-700">
            <span>CampusCare — Campus Complaint Management System</span>
          </div>
          <div className="text-slate-400">
            Dynamic Urgency Formula: Weight + ⌊Hours / 24⌋ • Browser LocalStorage Persistence
          </div>
        </div>
      </footer>
    </div>
  );
};

export default App;

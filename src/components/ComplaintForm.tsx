import React, { useState } from 'react';
import { Complaint, ComplaintCategory, CATEGORIES, CATEGORY_WEIGHTS } from '../types/complaint';
import { 
  Wifi, 
  Home, 
  GraduationCap, 
  FlaskConical, 
  Send, 
  CheckCircle2, 
  AlertCircle, 
  ArrowRight
} from 'lucide-react';

interface ComplaintFormProps {
  onSubmit: (category: ComplaintCategory, description: string) => Complaint | null;
  onViewAll: () => void;
}

export const ComplaintForm: React.FC<ComplaintFormProps> = ({ onSubmit, onViewAll }) => {
  const [category, setCategory] = useState<ComplaintCategory | ''>('');
  const [description, setDescription] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [submittedComplaint, setSubmittedComplaint] = useState<Complaint | null>(null);

  const getCategoryIcon = (cat: ComplaintCategory) => {
    switch (cat) {
      case 'Wi-Fi':
        return Wifi;
      case 'Hostel':
        return Home;
      case 'Classroom':
        return GraduationCap;
      case 'Laboratory':
        return FlaskConical;
    }
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMessage('');

    if (!category) {
      setErrorMessage('Please select a complaint category before submitting.');
      return;
    }

    if (!description.trim()) {
      setErrorMessage('Please provide a description of the complaint issue.');
      return;
    }

    if (description.trim().length < 5) {
      setErrorMessage('Description must be at least 5 characters long.');
      return;
    }

    const created = onSubmit(category, description.trim());
    if (created) {
      setSubmittedComplaint(created);
      setCategory('');
      setDescription('');
    }
  };

  const handleResetForm = () => {
    setSubmittedComplaint(null);
    setCategory('');
    setDescription('');
    setErrorMessage('');
  };

  return (
    <div className="max-w-3xl mx-auto space-y-6">
      {/* Success Notification Banner */}
      {submittedComplaint && (
        <div className="bg-emerald-50 border border-emerald-200 rounded-2xl p-6 shadow-sm animate-in fade-in zoom-in-95 duration-200">
          <div className="flex items-start gap-4">
            <div className="w-12 h-12 rounded-xl bg-emerald-100 flex items-center justify-center text-emerald-700 shrink-0">
              <CheckCircle2 className="w-6 h-6" />
            </div>
            <div className="flex-1">
              <div className="flex items-center gap-2">
                <h3 className="text-base font-bold text-emerald-900">
                  Complaint Registered Successfully!
                </h3>
                <span className="px-2 py-0.5 text-xs font-mono font-bold bg-emerald-200/80 text-emerald-900 rounded-md">
                  {submittedComplaint.id}
                </span>
              </div>
              <p className="text-xs text-emerald-800 mt-1 leading-relaxed">
                Your incident has been logged with initial status <strong className="font-semibold">Raised</strong> in the <strong className="font-semibold">{submittedComplaint.category}</strong> category and queued for campus administration.
              </p>

              <div className="mt-4 flex flex-wrap items-center gap-3">
                <button
                  type="button"
                  onClick={onViewAll}
                  className="inline-flex items-center gap-1.5 px-4 py-2 text-xs font-bold text-white bg-blue-700 hover:bg-blue-800 rounded-xl shadow-xs transition-colors"
                >
                  <span>View in All Complaints</span>
                  <ArrowRight className="w-3.5 h-3.5" />
                </button>
                <button
                  type="button"
                  onClick={handleResetForm}
                  className="inline-flex items-center gap-1.5 px-4 py-2 text-xs font-semibold text-slate-700 hover:text-slate-900 bg-white hover:bg-slate-50 border border-slate-200 rounded-xl transition-colors"
                >
                  <span>Submit Another Issue</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* Main Form Card */}
      <div className="bg-white rounded-2xl border border-slate-200/90 p-6 sm:p-8 shadow-xs">
        <div className="pb-6 border-b border-slate-100">
          <h2 className="text-xl font-extrabold text-slate-900 tracking-tight">
            Submit Campus Complaint
          </h2>
          <p className="text-xs text-slate-500 mt-1">
            Fill out the details below. Required fields are marked with an asterisk (*).
          </p>
        </div>

        <form onSubmit={handleSubmit} className="mt-6 space-y-6">
          {/* Validation Error Alert */}
          {errorMessage && (
            <div className="p-3.5 rounded-xl bg-red-50 border border-red-200 text-red-700 text-xs font-semibold flex items-center gap-2.5 animate-in fade-in duration-150">
              <AlertCircle className="w-4 h-4 shrink-0 text-red-600" />
              <span>{errorMessage}</span>
            </div>
          )}

          {/* Category Selection */}
          <div className="space-y-2.5">
            <label className="block text-xs font-bold text-slate-900 uppercase tracking-wider">
              1. Select Facility Category <span className="text-red-500">*</span>
            </label>
            <p className="text-xs text-slate-500">
              Each campus facility area has an urgency weight based on impact.
            </p>

            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3 pt-1">
              {CATEGORIES.map((cat) => {
                const Icon = getCategoryIcon(cat);
                const isSelected = category === cat;
                const weight = CATEGORY_WEIGHTS[cat];

                return (
                  <button
                    key={cat}
                    type="button"
                    onClick={() => {
                      setCategory(cat);
                      setErrorMessage('');
                    }}
                    className={`flex items-center justify-between p-3.5 rounded-xl border text-left transition-all ${
                      isSelected
                        ? 'border-blue-600 bg-blue-50/70 ring-2 ring-blue-500/20 shadow-xs'
                        : 'border-slate-200 hover:border-slate-300 hover:bg-slate-50/60 bg-white'
                    }`}
                  >
                    <div className="flex items-center gap-3">
                      <div
                        className={`w-9 h-9 rounded-lg flex items-center justify-center ${
                          isSelected ? 'bg-blue-600 text-white' : 'bg-slate-100 text-slate-600'
                        }`}
                      >
                        <Icon className="w-5 h-5" />
                      </div>
                      <div>
                        <div className={`text-sm font-bold ${isSelected ? 'text-blue-900' : 'text-slate-800'}`}>
                          {cat}
                        </div>
                        <div className="text-[11px] text-slate-500">
                          Campus facility area
                        </div>
                      </div>
                    </div>

                    <span
                      className={`text-xs font-semibold px-2 py-0.5 rounded-md ${
                        isSelected ? 'bg-blue-200 text-blue-900 font-bold' : 'bg-slate-100 text-slate-600'
                      }`}
                    >
                      Weight: {weight}
                    </span>
                  </button>
                );
              })}
            </div>
          </div>

          {/* Description Textarea */}
          <div className="space-y-2">
            <div className="flex items-center justify-between">
              <label htmlFor="description" className="block text-xs font-bold text-slate-900 uppercase tracking-wider">
                2. Complaint Description <span className="text-red-500">*</span>
              </label>
              <span className="text-[11px] text-slate-400">
                {description.length} characters
              </span>
            </div>
            <textarea
              id="description"
              rows={5}
              value={description}
              onChange={(e) => {
                setDescription(e.target.value);
                setErrorMessage('');
              }}
              placeholder="Provide specific details about the issue (e.g. precise building, room number, equipment affected, or safety concern)..."
              className="w-full p-3.5 text-sm rounded-xl border border-slate-200 focus:outline-none focus:ring-2 focus:ring-blue-600 focus:border-transparent transition-all placeholder:text-slate-400 text-slate-800 bg-white resize-y"
            />
            <p className="text-[11px] text-slate-500">
              New complaints are automatically assigned initial status <strong className="text-slate-700">Raised</strong> and given a unique ID.
            </p>
          </div>

          {/* Submit Action */}
          <div className="pt-3">
            <button
              type="submit"
              className="w-full sm:w-auto inline-flex items-center justify-center gap-2 px-6 py-3 rounded-xl font-bold text-sm text-white bg-blue-700 hover:bg-blue-800 shadow-md shadow-blue-700/20 transition-all cursor-pointer"
            >
              <Send className="w-4 h-4" />
              <span>Submit Complaint</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

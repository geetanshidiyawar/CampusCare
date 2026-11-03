import React, { useState, useRef, useEffect } from 'react';
import { ComplaintCategory, ComplaintStatus, CATEGORY_WEIGHTS } from '../types/complaint';
import { 
  Wifi, 
  Home, 
  GraduationCap, 
  FlaskConical, 
  Clock, 
  CheckCircle2, 
  AlertCircle, 
  ChevronDown,
  Flame,
  Check
} from 'lucide-react';

interface StatusBadgeProps {
  status: ComplaintStatus;
  size?: 'sm' | 'md';
}

export const StatusBadge: React.FC<StatusBadgeProps> = ({ status, size = 'md' }) => {
  const config = {
    'Raised': {
      bg: 'bg-amber-50 border-amber-200 text-amber-700',
      icon: Clock,
      dot: 'bg-amber-500',
    },
    'In Progress': {
      bg: 'bg-blue-50 border-blue-200 text-blue-700',
      icon: AlertCircle,
      dot: 'bg-blue-500',
    },
    'Resolved': {
      bg: 'bg-emerald-50 border-emerald-200 text-emerald-700',
      icon: CheckCircle2,
      dot: 'bg-emerald-500',
    },
  }[status];

  const IconComponent = config.icon;
  const sizeClasses = size === 'sm' ? 'px-2 py-0.5 text-xs' : 'px-2.5 py-1 text-xs';

  return (
    <span className={`inline-flex items-center gap-1.5 font-semibold rounded-full border ${config.bg} ${sizeClasses}`}>
      <IconComponent className="w-3.5 h-3.5" />
      <span>{status}</span>
    </span>
  );
};

interface UrgencyBadgeProps {
  score: number;
  isTopRank?: boolean;
}

export const UrgencyBadge: React.FC<UrgencyBadgeProps> = ({ score, isTopRank }) => {
  const getStyles = () => {
    if (score >= 4) {
      return {
        bg: 'bg-red-50 text-red-700 border-red-200',
        dot: 'bg-red-500',
      };
    }
    if (score >= 2) {
      return {
        bg: 'bg-amber-50 text-amber-700 border-amber-200',
        dot: 'bg-amber-500',
      };
    }
    return {
      bg: 'bg-blue-50 text-blue-700 border-blue-200',
      dot: 'bg-blue-500',
    };
  };

  const styles = getStyles();

  return (
    <span
      className={`inline-flex items-center gap-1.5 px-2.5 py-1 rounded-lg text-xs font-bold border transition-all ${styles.bg} ${
        isTopRank ? 'ring-2 ring-red-400 ring-offset-1 shadow-sm' : ''
      }`}
    >
      <span className={`w-1.5 h-1.5 rounded-full ${styles.dot}`} />
      {isTopRank && <Flame className="w-3.5 h-3.5 text-red-500 animate-pulse" />}
      <span>Urgency {score}</span>
    </span>
  );
};

interface CategoryBadgeProps {
  category: ComplaintCategory;
  showWeight?: boolean;
}

export const CategoryBadge: React.FC<CategoryBadgeProps> = ({ category, showWeight }) => {
  const getIcon = () => {
    switch (category) {
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

  const IconComponent = getIcon();
  const weight = CATEGORY_WEIGHTS[category];

  return (
    <span className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-lg text-xs font-medium bg-slate-100 text-slate-800 border border-slate-200">
      <IconComponent className="w-3.5 h-3.5 text-slate-600" />
      <span>{category}</span>
      {showWeight && (
        <span className="text-[10px] font-semibold text-slate-500 ml-0.5 bg-slate-200/80 px-1.5 py-0.2 rounded">
          Weight: {weight}
        </span>
      )}
    </span>
  );
};

interface StatusDropdownProps {
  currentStatus: ComplaintStatus;
  onStatusChange: (newStatus: ComplaintStatus) => void;
}

export const StatusDropdown: React.FC<StatusDropdownProps> = ({ currentStatus, onStatusChange }) => {
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef<HTMLDivElement>(null);

  const statuses: ComplaintStatus[] = ['Raised', 'In Progress', 'Resolved'];

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  return (
    <div className="relative inline-block text-left" ref={dropdownRef}>
      <button
        type="button"
        onClick={() => setIsOpen(!isOpen)}
        className="inline-flex items-center justify-between gap-1.5 px-3 py-1.5 text-xs font-semibold bg-white border border-slate-200 rounded-lg hover:bg-slate-50 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition-all shadow-xs"
        aria-expanded={isOpen}
      >
        <StatusBadge status={currentStatus} size="sm" />
        <ChevronDown className="w-3.5 h-3.5 text-slate-400 ml-1" />
      </button>

      {isOpen && (
        <div className="absolute right-0 z-30 mt-1.5 w-44 origin-top-right rounded-xl bg-white p-1.5 shadow-lg border border-slate-200 focus:outline-none">
          <div className="text-[10px] font-semibold text-slate-400 px-2.5 py-1 uppercase tracking-wider">
            Update Status
          </div>
          {statuses.map((status) => {
            const isSelected = status === currentStatus;
            return (
              <button
                key={status}
                type="button"
                onClick={() => {
                  onStatusChange(status);
                  setIsOpen(false);
                }}
                className={`w-full flex items-center justify-between px-2.5 py-1.5 text-xs rounded-lg transition-colors ${
                  isSelected ? 'bg-blue-50 text-blue-800 font-semibold' : 'text-slate-700 hover:bg-slate-100'
                }`}
              >
                <StatusBadge status={status} size="sm" />
                {isSelected && <Check className="w-3.5 h-3.5 text-blue-600" />}
              </button>
            );
          })}
        </div>
      )}
    </div>
  );
};

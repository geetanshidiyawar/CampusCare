import { Complaint } from '../types/complaint';
import { parseIdNumber } from './urgency';

export const STORAGE_KEY = 'campuscare_complaints';
export const HIGHEST_ID_KEY = 'campuscare_highest_id';

/**
 * Creates the initial four sample complaints according to specifications.
 */
export function getInitialSampleComplaints(): Complaint[] {
  const now = Date.now();
  const ONE_HOUR = 3600 * 1000;

  return [
    {
      id: 'C01',
      category: 'Wi-Fi',
      description: 'Wi-Fi connectivity issue on campus.',
      submittedAt: now - 48 * ONE_HOUR,
      status: 'Raised',
    },
    {
      id: 'C02',
      category: 'Hostel',
      description: 'Water supply issue in hostel.',
      submittedAt: now - 8 * ONE_HOUR,
      status: 'In Progress',
    },
    {
      id: 'C03',
      category: 'Classroom',
      description: 'Classroom projector not working.',
      submittedAt: now - 72 * ONE_HOUR,
      status: 'Raised',
    },
    {
      id: 'C04',
      category: 'Laboratory',
      description: 'Laboratory equipment issue.',
      submittedAt: now - 24 * ONE_HOUR,
      status: 'Resolved',
    },
  ];
}

/**
 * Retrieves complaints from localStorage. If no data exists, initializes with the 4 sample records.
 */
export function loadComplaints(): Complaint[] {
  try {
    const rawData = localStorage.getItem(STORAGE_KEY);
    if (!rawData) {
      const initialData = getInitialSampleComplaints();
      saveComplaints(initialData);
      localStorage.setItem(HIGHEST_ID_KEY, '4');
      return initialData;
    }
    const parsed = JSON.parse(rawData);
    if (Array.isArray(parsed) && parsed.length >= 0) {
      return parsed;
    }
  } catch (err) {
    console.error('Error reading complaints from localStorage:', err);
  }
  const fallback = getInitialSampleComplaints();
  saveComplaints(fallback);
  return fallback;
}

/**
 * Persists complaints array to localStorage.
 */
export function saveComplaints(complaints: Complaint[]): void {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(complaints));
  } catch (err) {
    console.error('Error saving complaints to localStorage:', err);
  }
}

/**
 * Generates the next unique sequential complaint ID (C01, C02, C03...).
 * Never reuses existing IDs by tracking the highest ID number ever generated.
 */
export function generateNextComplaintId(complaints: Complaint[]): string {
  let highest = 4;
  try {
    const savedHighest = localStorage.getItem(HIGHEST_ID_KEY);
    if (savedHighest) {
      highest = Math.max(highest, parseInt(savedHighest, 10));
    }
  } catch {
    // Ignore localStorage parse error
  }

  // Also check existing complaints in list
  for (const c of complaints) {
    const num = parseIdNumber(c.id);
    if (num > highest) {
      highest = num;
    }
  }

  const nextNumber = highest + 1;
  try {
    localStorage.setItem(HIGHEST_ID_KEY, nextNumber.toString());
  } catch {
    // Ignore
  }

  return `C${nextNumber.toString().padStart(2, '0')}`;
}

/**
 * Resets complaint records to the fresh sample data (useful for demonstration).
 */
export function resetToSampleComplaints(): Complaint[] {
  const sample = getInitialSampleComplaints();
  saveComplaints(sample);
  localStorage.setItem(HIGHEST_ID_KEY, '4');
  return sample;
}

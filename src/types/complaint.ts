export type ComplaintCategory = 'Wi-Fi' | 'Hostel' | 'Classroom' | 'Laboratory';

export type ComplaintStatus = 'Raised' | 'In Progress' | 'Resolved';

export interface Complaint {
  id: string;
  category: ComplaintCategory;
  description: string;
  submittedAt: number; // Unix timestamp in milliseconds
  status: ComplaintStatus;
}

export const CATEGORIES: ComplaintCategory[] = ['Wi-Fi', 'Hostel', 'Classroom', 'Laboratory'];

export const STATUSES: ComplaintStatus[] = ['Raised', 'In Progress', 'Resolved'];

export const CATEGORY_WEIGHTS: Record<ComplaintCategory, number> = {
  'Wi-Fi': 2,
  'Hostel': 2,
  'Classroom': 1,
  'Laboratory': 3,
};

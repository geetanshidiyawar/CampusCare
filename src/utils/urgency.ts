import { Complaint, CATEGORY_WEIGHTS } from '../types/complaint';

/**
 * Calculates hours pending from submittedAt timestamp until current time.
 */
export function getHoursPending(submittedAt: number, currentTime: number = Date.now()): number {
  const elapsedMillis = Math.max(0, currentTime - submittedAt);
  return Math.floor(elapsedMillis / (1000 * 60 * 60));
}

/**
 * Calculates urgency score using the exact formula:
 * Urgency Score = Category Weight + floor(Hours Pending / 24)
 */
export function calculateUrgencyScore(complaint: Complaint, currentTime: number = Date.now()): number {
  const weight = CATEGORY_WEIGHTS[complaint.category] ?? 1;
  const hours = getHoursPending(complaint.submittedAt, currentTime);
  const floorDays = Math.floor(hours / 24);
  return weight + floorDays;
}

/**
 * Formats complaint elapsed age, e.g. "48 hours ago", "1 day ago", or "Just now".
 */
export function formatComplaintAge(submittedAt: number, currentTime: number = Date.now()): string {
  const hours = getHoursPending(submittedAt, currentTime);
  if (hours === 0) {
    const minutes = Math.floor(Math.max(0, currentTime - submittedAt) / (1000 * 60));
    return minutes < 2 ? 'Just now' : `${minutes} mins ago`;
  }
  if (hours === 1) return '1 hour ago';
  if (hours < 24) return `${hours} hours ago`;
  if (hours === 24) return '1 day ago';
  if (hours < 48) return `1 day ${hours % 24}h ago`;
  const days = Math.floor(hours / 24);
  return `${days} days ago (${hours}h)`;
}

/**
 * Formats timestamp to readable localized string.
 */
export function formatSubmissionDate(submittedAt: number): string {
  const date = new Date(submittedAt);
  return date.toLocaleString('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
    hour: 'numeric',
    minute: '2-digit',
    hour12: true,
  });
}

/**
 * Parses numeric part of complaint ID (e.g. "C05" -> 5).
 */
export function parseIdNumber(id: string): number {
  const match = id.match(/\d+/);
  return match ? parseInt(match[0], 10) : 0;
}

/**
 * Ranks unresolved complaints for the Top 3 Priority queue.
 * Rules:
 * 1. Exclude all Resolved complaints (only Raised or In Progress).
 * 2. Higher urgency score first.
 * 3. If scores are equal, older complaint first (smaller submittedAt timestamp).
 * 4. If both score and age are equal, lower complaint ID first (e.g. C01 before C02).
 */
export function getTopPriorityComplaints(complaints: Complaint[], currentTime: number = Date.now()): Complaint[] {
  return complaints
    .filter((c) => c.status !== 'Resolved')
    .sort((a, b) => {
      const scoreA = calculateUrgencyScore(a, currentTime);
      const scoreB = calculateUrgencyScore(b, currentTime);
      if (scoreB !== scoreA) {
        return scoreB - scoreA; // descending urgency
      }
      if (a.submittedAt !== b.submittedAt) {
        return a.submittedAt - b.submittedAt; // older first (smaller timestamp)
      }
      return parseIdNumber(a.id) - parseIdNumber(b.id); // lower ID first
    })
    .slice(0, 3);
}

# CampusCare — Campus Complaint Management System (Web Application)

A modern, responsive web application built with **React**, **TypeScript**, **Vite**, and **Tailwind CSS** that allows students and administrators to log, track, filter, manage, and prioritize campus-related complaints using a dynamic urgency scoring algorithm and local persistence.

---

## 🚀 Features

- **Dashboard**:
  - Summary cards with real-time dynamic counts: **Total Complaints**, **Pending Complaints**, and **Resolved Complaints**.
  - **Top 3 Priority Complaints Queue**: Unresolved complaints ranked by urgency scoring algorithm, with distinct styling and a highlighted badge for the `#1 Highest Urgency` complaint.
  - **Campus Analytics**: Real-time breakdown of complaints by facility category and pipeline status.
- **Urgency Scoring Algorithm**:
  - Formula: `Urgency Score = Category Weight + floor(Hours Pending / 24)`
  - Category weights:
    - **Laboratory**: 3
    - **Wi-Fi**: 2
    - **Hostel**: 2
    - **Classroom**: 1
  - Dynamic tie-breaking: Higher urgency score → Older complaint first → Lower complaint ID first.
  - Resolved complaints are excluded from the top priority queue.
- **Complaint Submission Form**:
  - Required fields with validation: Category (Wi-Fi, Hostel, Classroom, Laboratory) and Description.
  - Automatic unique ID generation (`C01`, `C02`, `C03`...).
  - Initial status set to `Raised`.
- **Complaint Management**:
  - Search by complaint ID, category, or description.
  - Filter by category and status simultaneously.
  - Inline editable status dropdown: `Raised` (Amber), `In Progress` (Blue), and `Resolved` (Green).
- **Data Persistence**:
  - Stored in browser `localStorage` under `campuscare_complaints`.
  - Initialized with the 4 sample complaints on first launch without overwriting user data on refresh:
    - **C01**: Wi-Fi, 48 hours ago, Raised
    - **C02**: Hostel, 8 hours ago, In Progress
    - **C03**: Classroom, 72 hours ago, Raised
    - **C04**: Laboratory, 24 hours ago, Resolved
  - "Reset Demo Data" button available in the top bar to easily restore demo data during hackathon evaluation.

---

## 🛠️ Local Development

### 1. Install dependencies
```bash
npm install
```

### 2. Start development server
```bash
npm run dev
```
The app will start on `http://localhost:3000`.

### 3. Build for production
```bash
npm run build
```
Production assets are generated in the `dist/` directory.

---

## 🌐 Deploying to Vercel

1. Push this repository to **GitHub**.
2. Go to [vercel.com](https://vercel.com) and click **"Add New Project"**.
3. Import your GitHub repository.
4. Vercel will automatically detect **Vite**:
   - **Framework Preset**: Vite
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`
   - **Install Command**: `npm install`
5. Click **"Deploy"**. Your live CampusCare web application URL will be ready in seconds!

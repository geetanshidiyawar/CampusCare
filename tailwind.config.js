/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        campus: {
          blue: '#1e40af',
          'blue-dark': '#1e3a8a',
          'blue-accent': '#2563eb',
          'blue-light': '#eff6ff',
          charcoal: '#0f172a',
          slate: '#334155',
          'slate-muted': '#64748b',
          border: '#e2e8f0',
          bg: '#f8fafc',
          amber: '#d97706',
          'amber-bg': '#fef3c7',
          green: '#059669',
          'green-bg': '#d1fae5',
          red: '#dc2626',
          'red-bg': '#fee2e2'
        }
      }
    },
  },
  plugins: [],
}

// Utility helper functions — small tools used across the app

// Format a date string to readable format e.g. "2024-01-15" → "Jan 15, 2024"
export function formatDate(dateString) {
    if (!dateString) return '-';
    return new Date(dateString).toLocaleDateString('en-US', { year: 'numeric', month: 'short', day: 'numeric' });
}

// Capitalize the first letter of a string
export function capitalize(str) {
    if (!str) return '';
    return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}

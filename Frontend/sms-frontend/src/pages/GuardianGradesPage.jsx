// YOUR TASK — GuardianGradesPage (build after StudentGradesPage is done)
// A guardian logs in and lands here — they see ALL their children's grades grouped by child.
//
// This page calls GET /api/guardians/my-children-grades
//
// STATE VARIABLES (useState):
//   childrenGrades — starts as []   — array of { student: {...}, grades: [...] }
//   loading        — starts as true
//   error          — starts as null
//
// WHAT TO DO IN useEffect:
//   1. Call guardianService.getChildrenGrades()   ← I build guardianService for you
//   2. On success: set childrenGrades, set loading false
//   3. On error:   set error, set loading false
//
// JSX STRUCTURE:
//   <h1>My Children's Grades</h1>
//   If loading → "Loading..."
//   If error   → error message in red
//   Otherwise  → map over childrenGrades:
//       For each child: show <Card title="{child.firstName} {child.lastName}">
//         Inside the card: map over grades → show subject, total, grade letter, remarks
//
// TEACHES YOU: nested map (mapping grades inside each child), passing data between components.

export default function GuardianGradesPage() {
    // TODO: add state, useEffect, and JSX
}

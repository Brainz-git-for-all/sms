// YOUR TASK — StudentGradesPage (build after LoginPage is done)
// A student logs in and lands here — they can only see their OWN grades.
//
// This page calls GET /api/students/my-grades (the endpoint you build in StudentController)
//
// STATE VARIABLES (useState):
//   grades   — starts as []      — list of grade objects from the API
//   loading  — starts as true    — show spinner while fetching
//   error    — starts as null    — show message if fetch fails
//
// WHAT TO DO IN useEffect (runs once when page loads):
//   1. Call studentService.getMyGrades()
//   2. On success: set grades to the response data, set loading to false
//   3. On error:   set error to the error message, set loading false
//
// JSX STRUCTURE:
//   A <Card title="My Grades"> wrapper
//   If loading → show "Loading your grades..."
//   If error   → show error in red
//   Otherwise  → map over grades array, for each grade show:
//       Subject name, Test1 score, Test2 score, Exam score, Total, Grade letter, Remarks
//
// TEACHES YOU: useEffect for data fetching, dependency array, async inside useEffect.

export default function StudentGradesPage() {
    // TODO: add state, useEffect, and JSX
}

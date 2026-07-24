// YOUR TASK — Page 1 (build AFTER Button, Input, Card are done)
// Build the login screen using your own components.
//
// STATE VARIABLES (useState):
//   username  — starts as ""  — updated by the username Input
//   password  — starts as ""  — updated by the password Input
//   error     — starts as ""  — set to error message if login fails
//
// FUNCTIONS:
//   handleSubmit(event):
//     1. Call event.preventDefault()   ← stops the page from refreshing
//     2. Call login(username, password) from AuthContext (I provide this)
//     3. If it throws an error, set the error state to the error message
//
// JSX STRUCTURE:
//   A full-screen centered <div>
//     <Card title="Login">
//       <form onSubmit={handleSubmit}>
//         <Input label="Username" value={username} onChange={e => setUsername(e.target.value)} />
//         <Input label="Password" type="password" value={password} onChange={e => setPassword(e.target.value)} />
//         {error && <p className="text-red-500">{error}</p>}
//         <Button label="Login" type="submit" />
//       </form>
//     </Card>
//
// WHEN DONE: tell me "I finished LoginPage.jsx" and paste your code.

export default function LoginPage() {
    // TODO: add state, handleSubmit, and return the JSX
}

// YOUR TASK — Component 3
// Build a reusable Card (white panel) that wraps other content.
//
// PROPS to accept:
//   children  (ReactNode, required) — whatever JSX is placed between <Card>...</Card>
//   title     (string, optional)    — heading shown at top of card; only render if provided
//   className (string, optional)    — extra Tailwind classes for customization
//
// STRUCTURE to return:
//   <div className="bg-white rounded-xl shadow p-6 {className}">
//     {title && <h3 className="...">{title}</h3>}
//     {children}
//   </div>
//
// WHEN DONE: tell me "I finished Card.jsx" and paste your code.

export default function Card({ title, children, className }) {
    // TODO: add props and return the JSX structure

    return(
       
         <div className="bg-white rounded-xl shadow p-6 {className}">
         {title && <h3 className="...">{title}</h3>}
         {children}
     
        </div>
    )

}

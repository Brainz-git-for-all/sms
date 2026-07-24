// YOUR TASK — Component 2
// Build a reusable Input component with a label and optional error message.
//
// PROPS to accept:
//   label       (string)   — text shown above the input
//   type        (string, default "text") — "text" | "password" | "email" | "number"
//   value       (string)   — current value (comes from useState in the parent)
//   onChange    (function) — updates the parent's state when user types
//   error       (string)   — error message shown below; only show if not empty
//   placeholder (string)   — grey hint text inside the field
//
// STRUCTURE to return:
//   <div>                              ← outer wrapper
//     <label>{label}</label>           ← label above the field
//     <input type onChange value placeholder />
//     {error && <p>{error}</p>}        ← only render if error is not empty
//   </div>
//
// WHEN DONE: tell me "I finished Input.jsx" and paste your code.

export default function Input({label,type,value,onChange,error,placeholder}) {
    return (
        <div>
            <label>{label}</label>
            <input
                type={type}
                onChange={onChange}
                value={value}
                placeholder={placeholder}
            ></input>
            {error && <p>{error}</p>}
        </div>
    )
}

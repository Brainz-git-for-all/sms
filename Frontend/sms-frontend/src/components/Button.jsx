// YOUR TASK — Component 1
// Build a reusable Button component.
//
// PROPS to accept:
//   label    (string, required)  — text shown on the button
//   onClick  (function, required) — called when button is clicked
//   variant  (string, default "primary") — "primary" | "danger" | "secondary"
//   disabled (boolean, default false) — greys out and blocks clicking
//   type     (string, default "button") — "button" or "submit"
//
// WHAT TO DO:
//   1. Define: export default function Button({ label, onClick, variant = "primary", disabled = false, type = "button" })
//   2. Create a baseClass variable with shared Tailwind classes (padding, font, rounded, etc.)
//   3. Use if/else or a ternary to pick colour classes based on variant:
//        primary   → blue background, white text
//        danger    → red background, white text
//        secondary → grey background, dark text
//   4. Return a <button> with type, onClick, disabled, and the combined className
//   5. Put {label} inside the button tag
//
// WHEN DONE: tell me "I finished Button.jsx" and paste your code.

function Button({ label, onClick, variant = "primary", disabled = false, type = "button" }) {

    const baseClass = "px-4 py-2 font-semibold rounded focus:outline-none focus:ring"; 
    
    const variantClass = variant === "primary" ? "bg-blue-500 text-white hover:bg-blue-600" :
                      variant === "danger" ? "bg-red-500 text-white hover:bg-red-600" :
                      "bg-gray-300 text-gray-800 hover:bg-gray-400";

    const disabledClass = disabled ? "opacity-50 cursor-not-allowed" : "";

    const className = `${baseClass} ${variantClass} ${disabledClass}`;
    return (
        <button
            type={type}
            onClick={onClick}
            disabled={disabled}
            className={className}
        >
            {label}
        </button>
    );
    
    
}
export default Button;

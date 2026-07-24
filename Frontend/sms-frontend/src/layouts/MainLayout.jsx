// I BUILD THIS — the sidebar + header shell that wraps all protected pages
// Do not modify. I will fill this in when we reach the layout step.

export default function MainLayout({ children }) {
    return (
        <div className="flex h-screen">
            <aside className="w-64 bg-slate-900 text-white p-6">Sidebar — I build this</aside>
            <main className="flex-1 overflow-auto bg-gray-50">{children}</main>
        </div>
    );
}

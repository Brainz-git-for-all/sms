// I BUILD THIS — global auth state (who is logged in, their token, their role)
// Provides a login() function you call in LoginPage.
// Do not modify. I will fill this in when we reach the auth context step.

import { createContext } from 'react';
export const AuthContext = createContext(null);

export default function AuthProvider({ children }) {
    return <AuthContext.Provider value={{}}>{children}</AuthContext.Provider>;
}

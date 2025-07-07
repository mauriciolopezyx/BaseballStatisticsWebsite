import { createContext, useEffect, useState } from "react";

type authContext = {
  user: null | boolean,
  loading: boolean,
  refresh: () => Promise<void>,
  logout: () => Promise<void>
}
type authState = {
  user: null | boolean,
  loading: boolean
}
const AuthContext = createContext<authContext>({user: null, loading: true, refresh: async () => {}, logout: async() => {}})

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [state, setState] = useState<authState>({ user: null, loading: true });

  const refresh = async () => {
    console.log("Attempting to refresh/validate..")
    try {
        const response = await fetch("http://localhost:8080/auth/session", {
            method: "POST",
            credentials: "include"
        })
        
        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`)
        }
        
        const text = await response.text()
        console.log("Session response text:", text)
        
        setState({ user: true, loading: false })
    } catch (error) {
        console.error("Session validation error:", error)
        setState({ user: null, loading: false })
    }
  }

  const logout = async () => {
    try {
      console.log("Attempting to logout 1..")
        const response = await fetch("http://localhost:8080/auth/logout", {
          method: "POST",
          credentials: "include"
        })
        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`)
        }
        console.log("logout response:", response)
        console.log("Successful log out!")
    } catch (error) {
        console.error("Logout error:", error)
    } finally {
      setState({ user: null, loading: false })
    }
  }

  useEffect(() => { refresh() }, [])

  return (
    <AuthContext.Provider value={{ ...state, refresh, logout }}>
      {children}
    </AuthContext.Provider>
  )
}

export { AuthContext }
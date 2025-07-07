import React from "react"
import { Outlet, Navigate, useLocation, useOutletContext } from "react-router"
import { useContext } from "react"
import { AuthContext } from "./Authentication.tsx"

export default function PrivateRoute() {
    const location = useLocation()
    const outletContext = useOutletContext()
    const { user, loading } = useContext(AuthContext)
    
    if (loading) {
        return (
            <h1>Loading...</h1>
        )
    }

    if (!user) {
        return (
            <Navigate 
                to="/login" 
                state={{
                    message: "Please login to continue",
                    from: location.pathname
                }} 
                replace
            />)
    }
    return <Outlet context={outletContext} />
}
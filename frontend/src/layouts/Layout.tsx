import { useState, useEffect } from "react"
import { Outlet, useLocation } from "react-router"
import Header from "./Header.tsx"
import '../App.css'
import clsx from "clsx"
import type { ThemeOutletContext } from "../utilities/ThemeOutletContext.tsx"

export default function Layout() {

    const location = useLocation()

    const needsPageCentering = ["/register", "/login", "/verify", "/resend", "/player/config", "/password/reset"].includes(location.pathname)
    const classes = clsx(
        needsPageCentering && "flex",
        needsPageCentering && "justify-center",
        needsPageCentering && "items-center",
        needsPageCentering && "min-h-screen"
    )

    const presetTheme = localStorage.getItem("theme") ?? "light"
    const [darkMode, setDarkMode] = useState(presetTheme === "dark")

    useEffect(() => {
        const html = document.documentElement;
        if (darkMode) {
            html.classList.add("dark")
            localStorage.setItem("theme", "dark")
        } else {
            html.classList.remove("dark")
            localStorage.setItem("theme", "light")
        }
    }, [darkMode])

    function changeTheme() {
        setDarkMode(prev => !prev)
    }

    return (
        <div className="site-wrapper bg-background min-h-screen">
            <Header />
            <main className={classes}>
                <Outlet context={{darkTheme: darkMode, toggleTheme: changeTheme} satisfies ThemeOutletContext}/>
            </main>
        </div>
    )
}
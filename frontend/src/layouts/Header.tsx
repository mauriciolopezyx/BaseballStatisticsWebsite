import { Link, NavLink, useNavigate } from "react-router"
import { useState } from "react"
import clsx from "clsx"
import '../App.css'

export default function Header() {

    const [query, setQuery] = useState("")
    const navigate = useNavigate()

    const activeStyles = {
        textDecoration: "underline"
    }
    const inactiveStyles = {}

    const headerClasses = clsx("bg-teal-700", "text-white", "md:flex", "md:flex-row", "md:justify-between", "text-center", "font-black", "p-5")
    const navClasses = clsx("flex", "flex-row", "gap-5", "justify-center")
    const navLinkClasses = clsx("hover:motion-preset-fade", "motion-duration-300", "hover:bg-teal-800", "px-4")
    
    async function initiateSearch(e: React.FormEvent) {
        e.preventDefault()
        navigate(`/search/players?player=${query}&team=any`)
    }

    // {query && (
    //     <div className="absolute w-full bg-white border border-gray-300 rounded shadow z-10">
    //         <Link to="/willy-adames"><p className="px-2 py-1 text-sm text-gray-800">{query}</p></Link>
    //     </div>
    // )}

    return (
        <header className={headerClasses}>
            <Link className="text-xl px-4" to="/">Baseball Guru!</Link>
            <div className="flex md:gap-8 justify-center">
                <div className="relative">
                    <form onSubmit={initiateSearch}>
                        <input
                        type="text"
                        value={query}
                        onChange={(e) => setQuery(e.target.value)}
                        placeholder="Search…"
                        className="border border-black font-semibold text-black bg-white
                                    w-48 h-full px-2 hidden md:inline"
                        />
                    </form>
                </div>

                <nav className={navClasses}>
                    <NavLink
                        to="/"
                        className={navLinkClasses}
                        style={({ isActive }) => isActive ? activeStyles : inactiveStyles}
                    >
                        Home
                    </NavLink>
                    <NavLink
                        to="/search"
                        className={navLinkClasses}
                        style={({ isActive }) => isActive ? activeStyles : inactiveStyles}
                    >
                        Search
                    </NavLink>
                    <NavLink
                        to="/settings"
                        className={navLinkClasses}
                        style={({ isActive }) => isActive ? activeStyles : inactiveStyles}
                    >
                        Settings
                    </NavLink>
                </nav>
            </div>
        </header>
    )
}
import '../App.css'
import { IoIosSunny } from "react-icons/io"
import { FaMoon } from "react-icons/fa"
import { useOutletContext, Link, useNavigate } from "react-router"
import { useContext, useEffect, useState } from "react"
import { AuthContext } from "../layouts/Authentication.tsx"
import type { ThemeOutletContext } from "../utilities/ThemeOutletContext.tsx"
import clsx from "clsx"

type Profile = {
    username: string | null,
    email: string | null
}

export default function Settings() {

    const subSettingClasses = clsx("flex", "bg-settingslot", "rounded-lg", "justify-between", "items-center", "p-4", "font-bold", "text-xl", "text-paragraph")
    const darkLightModeClasses = clsx("p-2", "bg-slate-500", "text-white", "cursor-pointer", "rounded-lg", "text-4xl")
    
    const { darkTheme, toggleTheme } = useOutletContext<ThemeOutletContext>()
    const [profile, setProfile] = useState<Profile>({username: null, email: null})
    const context = useContext(AuthContext)
    const navigate = useNavigate()

    useEffect( () => {
        const profileFetch = async () => {
            try {
                const response = await fetch("http://localhost:8080/users/me", 
                    {
                        method: "GET",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        credentials: "include"
                    }
                )
                if (!response.ok) {
                    const payload = await response.text()
                    throw new Error(payload)
                    //throw new Error(`Response status: ${response.status}`)
                }
                const profileJson = await response.json()
                setProfile(profileJson)
            } catch (error: any) {
                console.error(error.message)
            }
        }
        profileFetch()
    }, [])

    async function attemptPasswordReset() {
        try {
            const response = await fetch("http://localhost:8080/users/me/haspassword", 
                {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    credentials: "include"
                }
            )
            if (!response.ok) {
                const payload = await response.text()
                throw new Error(payload)
                //throw new Error(`Response status: ${response.status}`)
            }
            const json = await response.json()
            navigate("/password/reset", { state: {hasPassword: json.hasPassword ?? false} })
        } catch (error: any) {
            console.error(error.message)
        }
    }

    function maskEmail(email: string) {
        const [local, domain] = email.split('@')
        if (!domain) return email
        return email[0] + '*'.repeat(local.length-1) + '@' + domain
    }

    return (
        <section className="mt-15 flex flex-col gap-10 justify-center items-center">
            <h1 className="text-headers font-black text-4xl">Manage your account</h1>
            <section className="w-full md:w-1/2 xl:w-1/3 bg-homeslot p-4 rounded-lg shadow-lg flex flex-col gap-4">
                <div className="text-center text-paragraph">
                    {profile.username && profile.email ? 
                    <>
                        <p><span className="font-bold">Account Username:</span> {profile.username}</p>
                        <p><span className="font-bold">Account Email:</span> {maskEmail(profile.email)}</p>
                    </>
                    : <h2 className="italic font-semibold">Loading...</h2> }
                </div>
                <div className={subSettingClasses}>
                    <p>Theme</p>
                    {darkTheme ? <FaMoon onClick={toggleTheme} className={darkLightModeClasses}/> : <IoIosSunny onClick={toggleTheme} className={darkLightModeClasses}/>}
                </div>
                <div className={subSettingClasses}>
                    <p>Forgot Password?</p>
                    <button className="bg-slate-500 py-2 px-8 text-white cursor-pointer rounded-lg hover:underline" onClick={attemptPasswordReset}>Reset</button>
                </div>
                <div className={subSettingClasses}>
                    <p>Add Player Entry</p>
                    <Link className="bg-slate-500 py-2 px-8 text-white cursor-pointer rounded-lg hover:underline" to="/player/config">Add</Link>
                </div>
                <button className="w-full bg-slate-500 py-2 text-white text-center rounded-lg font-bold italic cursor-pointer hover:underline" onClick={context.logout}>Log Out</button>

            </section>
        </section>
    )
}
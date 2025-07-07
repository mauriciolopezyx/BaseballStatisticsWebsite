import '../App.css'
import clsx from "clsx"
import React, { useState } from "react"
import { LuEye, LuEyeClosed } from "react-icons/lu"
import { useLocation } from "react-router"

export default function ResetPassword() {
    
    const location = useLocation()

    const mainClasses = clsx("flex", "flex-col", "justify-center", "items-center", "w-96", "bg-gray-100", "rounded-lg", "shadow-md", "p-8")
    const formClasses = clsx("flex", "flex-col", "justify-center", "items-center", "w-full")
    const titleClasses = clsx("w-full", "mb-1", "font-bold", "mb-4", "text-center")

    const [showOldPassword, setShowOldPassword] = useState(false)
    const [showNewPassword, setShowNewPassword] = useState(false)
    const [successMessage, setSuccessMessage] = useState<string|null>(null)

    const hasExistingPassword = location.state?.hasPassword
    const conditionalTitle = hasExistingPassword ? "Enter your current and newly chosen password:" : "Since you do not have an existing password, please create one now:"

    function toggleOldVisibility() {
        setShowOldPassword(prev => !prev)
    }
    function toggleNewVisibility() {
        setShowNewPassword(prev => !prev)
    }

    const [oldPassword, setOldPassword] = useState("")
    const [newPassword, setNewPassword] = useState("")

    const [status, setStatus] = useState("idle")
    const [error, setError] = useState(null)

    function handleOldChange(e: React.ChangeEvent<HTMLInputElement>) {
        const { value } = e.target
        setOldPassword(value)
    }
    function handleNewChange(e: React.ChangeEvent<HTMLInputElement>) {
        const { value } = e.target
        setNewPassword(value)
    }
    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault()
        setStatus("submitting")
        try {
            const response = await fetch("http://localhost:8080/users/me/resetpassword", 
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    credentials: "include",
                    body: JSON.stringify({
                        ...(hasExistingPassword && {oldPassword: oldPassword}),
                        newPassword: newPassword
                    })
                }
            )
            if (!response.ok) {
                const payload = await response.text()
                throw new Error(payload)
                //throw new Error(`Response status: ${response.status}`)
            }

            setError(null)
            console.log("Successfully reset password!")
            
            setSuccessMessage("Successfully reset your password!")
            setTimeout(() => {
                setSuccessMessage(null)
            }, 4000)

        } catch (error: any) {
            console.error(error.message)
            setError(error.message)
            setStatus("idle")
        }
    }

    return (
        <section className={mainClasses}>
            <h1 className="font-black text-2xl mb-4">Reset Password</h1>
            {error ? <h3 className="text-red-500 text-center font-semibold pb-2">{error}</h3> : null}
            {successMessage ? <h3 className="text-green-500 text-center font-semibold pb-2">{successMessage}</h3> : null}
            <h2 className={titleClasses}>{conditionalTitle}</h2>
            <form className={formClasses} onSubmit={handleSubmit}>

                {hasExistingPassword ? 
                <>
                    <label className="text-start w-full pb-1 font-semibold" htmlFor="oldPassword">Current Password</label>
                    <div className="w-full relative">
                        <input className="bg-white w-full border-1 border-black rounded-md p-2 mb-4" type={showOldPassword ? "text" : "password"} name="oldPassword" id="oldPassword" onChange={handleOldChange} value={oldPassword} required />
                        {!showOldPassword ? <LuEye className="absolute right-3 top-1/2 -translate-y-4 cursor-pointer" onClick={toggleOldVisibility} />
                        : <LuEyeClosed className="absolute right-3 top-1/2 -translate-y-4 cursor-pointer" onClick={toggleOldVisibility} />
                        }
                    </div>
                </>
                : null
                }


                <label className="text-start w-full pb-1 font-semibold" htmlFor="newPassword">New Password</label>
                <div className="w-full relative">
                    <input className="bg-white w-full border-1 border-black rounded-md p-2 mb-8" type={showNewPassword ? "text" : "password"} name="newPassword" id="newPassword" onChange={handleNewChange} value={newPassword} required />
                    {!showNewPassword ? <LuEye className="absolute right-3 top-1/3 -translate-y-3 cursor-pointer" onClick={toggleNewVisibility} />
                    : <LuEyeClosed className="absolute right-3 top-1/3 -translate-y-3 cursor-pointer" onClick={toggleNewVisibility} />
                    }
                </div>
                <button className="bg-teal-700 text-lg text-white shadow-md rounded-md py-2 font-bold w-full cursor-pointer" disabled={status === "submitting"}>Confirm</button>
            </form>

        </section>
    )
}
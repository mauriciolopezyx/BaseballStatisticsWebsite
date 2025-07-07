import '../App.css'
import clsx from "clsx"
import React, { useState } from "react"
import { LuEye, LuEyeClosed } from "react-icons/lu"
import { useLocation, useNavigate } from "react-router"

export default function Verify() {
    
    const location = useLocation()
    const navigate = useNavigate()
    const verificationEmail: string = location.state.email

    const mainClasses = clsx("flex", "flex-col", "justify-center", "items-center", "w-96", "bg-gray-100", "rounded-lg", "shadow-md", "p-8")
    const formClasses = clsx("flex", "flex-col", "justify-center", "items-center", "w-full")
    const labelClasses = clsx("w-full", "mb-1", "font-bold", "mb-4", "text-center")
    
    const [showPassword, setShowPassword] = useState(false);
    function toggleVisibility() {
        setShowPassword(prev => !prev)
    }

    const [verificationCode, setVerificationCode] = useState("")
    const [resendMessage, setResendMessage] = useState<string|null>(null)
    const [status, setStatus] = useState("idle")
    const [error, setError] = useState(null)

    function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
        const { value } = e.target
        setVerificationCode(value)
    }
    console.log("current cached email:", verificationEmail)
    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault()
        setStatus("submitting")
        try {
            const response = await fetch("http://localhost:8080/auth/verify", 
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    credentials: "include",
                    body: JSON.stringify({
                        email: verificationEmail,
                        verificationCode: verificationCode
                    })
                }
            )
            if (!response.ok) {
                const payload = await response.text()
                throw new Error(payload)
                //throw new Error(`Response status: ${response.status}`)
            }

            setError(null)
            console.log("Successfully verified user!")
            navigate("/login", { state: {verifySuccess: true}, replace: true })
        } catch (error: any) {
            console.error(error.message)
            setError(error.message)
            setStatus("idle")
        }
    }

    async function resendVerificationCode() {
        setStatus("submitting")
        try {
            const response = await fetch(`http://localhost:8080/auth/resend?email=${verificationEmail}`,
                {
                    method: "POST",
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

            setError(null)
            setResendMessage("Successfully resent verification code!")
            setTimeout(() => {
                setResendMessage(null)
            }, 4000)

        } catch (error: any) {
            console.error(error.message)
            setError(error.message)
        } finally {
            setStatus("idle")
        }
    }

    function maskEmail(email: string) {
        const [local, domain] = email.split('@')
        if (!domain) return email
        return email[0] + '*'.repeat(local.length-1) + '@' + domain
    }

    return (
        <section className={mainClasses}>
            <h1 className="font-black text-2xl mb-4">Verify</h1>
            {error ? <h3 className="text-red-500 text-center font-semibold pb-2">{error}</h3> : null}
            {resendMessage ? <h3 className="text-green-500 text-center font-semibold pb-2">{resendMessage}</h3> : null}
            <form className={formClasses} onSubmit={handleSubmit}>
                <label className={labelClasses} htmlFor="password">Enter the verification code that was just sent to {maskEmail(verificationEmail)}:</label>
                <div className="w-full relative">
                    <input className="bg-white w-full border-1 border-black rounded-md p-2 mb-8" type={showPassword ? "text" : "password"} name="password" id="password" onChange={handleChange} value={verificationCode} required />
                    {!showPassword ? <LuEye className="absolute right-3 top-1/3 -translate-y-3 cursor-pointer" onClick={toggleVisibility} />
                    : <LuEyeClosed className="absolute right-3 top-1/3 -translate-y-3 cursor-pointer" onClick={toggleVisibility} />
                    }
                </div>
                <button className="bg-teal-700 text-lg text-white shadow-md rounded-md py-2 font-bold w-full cursor-pointer" disabled={status === "submitting"}>Confirm</button>
            </form>

            <div className="bg-white border-1 border-black rounded-md w-full text-center mt-8 py-2">
                Didn't receive it? <div onClick={resendVerificationCode} className="font-bold inline cursor-pointer underline text-blue-500">Resend code</div>
            </div>

        </section>
    )
}
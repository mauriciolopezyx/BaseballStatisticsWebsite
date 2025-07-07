import '../App.css'
import clsx from "clsx"
import { useState, useContext } from "react"
import { LuEye, LuEyeClosed } from "react-icons/lu"
import { Link, useLocation, useNavigate } from "react-router"
import { FcGoogle } from "react-icons/fc"
import { AuthContext } from "../layouts/Authentication.tsx"

export default function Login() {
    
    const location = useLocation()
    const navigate = useNavigate()
    const { refresh } = useContext(AuthContext)

    const mainClasses = clsx("flex", "flex-col", "justify-center", "items-center", "w-96", "bg-gray-100", "rounded-lg", "shadow-md", "p-8")
    const formClasses = clsx("flex", "flex-col", "justify-center", "items-center", "w-full")
    const labelClasses = clsx("text-start", "w-full", "ml-4", "mb-1", "font-bold")
    const inputClasses = clsx("bg-white", "w-full", "border-1", "border-black", "rounded-md", "p-2", "mb-4")
    
    const [showPassword, setShowPassword] = useState(false);
    function toggleVisibility() {
        setShowPassword(prev => !prev)
    }

    const [loginFormData, setLoginFormData] = useState({ email: "", password: "" })
    const [status, setStatus] = useState("idle")
    const [error, setError] = useState(null)

    function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
        const { name, value } = e.target
        setLoginFormData(prev => ({
            ...prev,
            [name]: value
        }))
    }
    console.log(location)

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault()
        setStatus("submitting")

        try {
            const response = await fetch("http://localhost:8080/auth/login", 
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    credentials: "include",
                    body: JSON.stringify(loginFormData)
                }
            )
            if (!response.ok) {
                const payload = await response.text()
                throw new Error(payload)
                //throw new Error(`Response status: ${response.status}`)
            }

            const json = await response.json()
            console.log(json)
            setError(null)
            console.log("successfully logged in.")

            await refresh()
            const redirectTo = location.state?.from ?? "/"
            navigate(redirectTo, { replace: true })
        } catch (error: any) {
            console.error(error)
            setError(error.message)
            setStatus("idle")
        }

    }

    return (
        <section className={mainClasses}>
            { location.state?.message && <h3 className="text-red-500 text-center font-semibold mb-4">{location.state.message}</h3> }
            { location.state?.verifySuccess && <h3 className="text-green-500 text-center font-semibold mb-4">Successfully verified! Please login with your new credentials</h3>}
            {error ? <h3 className="text-red-500 text-center font-semibold pb-2">{error}</h3> : null}
            <h1 className="font-black text-2xl mb-4">Login</h1>
            <form className={formClasses} onSubmit={handleSubmit}>
                <label className={labelClasses} htmlFor="email">Email</label>
                <input className={inputClasses} type="text" name="email" id="email" onChange={handleChange} required value={loginFormData.email} />
                
                <label className={labelClasses} htmlFor="password">Password</label>
                <div className="w-full relative">
                    <input className="bg-white w-full border-1 border-black rounded-md p-2 mb-8" type={showPassword ? "text" : "password"} name="password" id="password" onChange={handleChange} value={loginFormData.password} required />
                    {!showPassword ? <LuEye className="absolute right-3 top-1/3 -translate-y-3 cursor-pointer" onClick={toggleVisibility} />
                    : <LuEyeClosed className="absolute right-3 top-1/3 -translate-y-3 cursor-pointer" onClick={toggleVisibility} />
                    }
                </div>
                <button className="bg-teal-700 text-lg text-white shadow-md rounded-md py-2 font-bold w-full cursor-pointer" disabled={status === "submitting"}>Confirm</button>
            </form>
            {status === "submitting" ? <h2 className="mt-4 font-bold motion-preset-fade motion-duration-300">Logging in...</h2> : null}

            <div className="w-full flex items-center my-6">
                <div className="flex-grow border-t border-gray-600"></div>
                <span className="mx-4 text-gray-600 text-sm">or</span>
                <div className="flex-grow border-t border-gray-600"></div>
            </div>

            <div className={formClasses}>
                <a className="w-full" href="http://localhost:8080/oauth2/authorization/google"><button className="bg-white w-full cursor-pointer text-lg shadow-md border-1 p-4 font-bold rounded-md flex justify-center gap-2 items-center"><FcGoogle />Login with Google</button></a>
            </div>

            <div className="bg-white border-1 border-black rounded-md w-full text-center mt-8 py-2">
                New to Baseball Guru? <Link className="font-bold underline text-blue-500" to="/register">Sign up</Link>
            </div>

        </section>
    )
}
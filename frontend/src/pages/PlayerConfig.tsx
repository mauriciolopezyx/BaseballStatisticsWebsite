import '../App.css'
import { useState } from "react"
import clsx from "clsx"

const statToFullName: Record<string, string> = {
    lg: "League",
    g: "Games",
    tb: "Total Bases",
    sh: "Sac Bunts",
    'DBL': "Doubles",
    'TPL': "Triples"
}

type StatsTemplate = {
        player: null | string,
        pos: null | string ,
        age: null | number,
        team: null | string,
        lg: null | string,
        war: null | number,
        g: null | number,
        pa: null | number,
        ab: null | number,
        r: null | number,
        h: null | number,
        dbl: null | number,
        tpl: null | number,
        hr: null | number,
        rbi: null | number,
        sb: null | number,
        cs: null | number,
        bb: null | number,
        so: null | number,
        ba: null | number,
        obp: null | number,
        slg: null | number,
        ops: null | number,
        tb: null | number,
        gidp: null | number,
        hbp: null | number,
        sh: null | number,
        sf: null | number,
        ibb: null | number,
        headshot: null | string
    }

export default function PlayerEntry() {

    const [templateStats, setTemplateStats] = useState<StatsTemplate>({
        player: null,
        pos: null,
        age: null,
        team: null,
        lg: null,
        war: null,
        g: null,
        pa: null,
        ab: null,
        r: null,
        h: null,
        dbl: null,
        tpl: null,
        hr: null,
        rbi: null,
        sb: null,
        cs: null,
        bb: null,
        so: null,
        ba: null,
        obp: null,
        slg: null,
        ops: null,
        tb: null,
        gidp: null,
        hbp: null,
        sh: null,
        sf: null,
        ibb: null,
        headshot: null
    })
    const [status, setStatus] = useState("idle")
    const [error, setError] = useState(null)
    const [successMessage, setSuccessMessage] = useState<string|null>(null)
    const stringFields = ["player", "pos", "team", "lg", "headshot"]

    function handleChange(e: React.ChangeEvent<HTMLInputElement>) {
        const { name, value } = e.target
        const isNumberField = !stringFields.includes(name)
        setTemplateStats(prev => ({
            ...prev,
            [name]: isNumberField ? (value ? parseFloat(value) : null) : (value ? value : null)
        }))
    }

    const statEntries = Object.entries(templateStats).map(([key, currentValue]) => {
        const entryClasses = clsx(key === "player" && "grow-2", key === "headshot" && "grow-2")
        return (
            <div key={key} className={entryClasses}>
                <label htmlFor={key} className="bg-playerbg block text-paragraph font-semibold">{statToFullName[key]?.toUpperCase() ?? key.toUpperCase()}</label>
                <input name={key} id={key} className="bg-green-100 text-center w-full"
                min="0" 
                value={currentValue ? currentValue : stringFields.includes(key) ? "" : "0"}
                onChange={handleChange}
                type={stringFields.includes(key) ? "text" : "number"} 
                required 
                />
            </div>
        )
    })

    async function handleSubmit(e: React.FormEvent) {
        e.preventDefault()
        setStatus("submitting")

        try {
            const response = await fetch("http://localhost:8080/stats/player/add", 
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    credentials: "include",
                    body: JSON.stringify(templateStats)
                }
            )
            if (!response.ok) {
                const payload = await response.text()
                throw new Error(payload)
                //throw new Error(`Response status: ${response.status}`)
            }

            setError(null)
            
            console.log("successfully added!")
            setSuccessMessage(`Successfully added ${templateStats.player}!`)
            setTimeout(() => {
                setSuccessMessage(null)
            }, 4000)

        } catch (error: any) {
            console.error(error)
            setError(error.message)
            setStatus("idle")
            setTimeout(() => {
                setError(null)
            }, 4000)
        }

    }

    return (
        <section className="mx-auto w-full md:w-3/4 xl:w-1/2 text-center" onSubmit={handleSubmit}>
            <h1 className="font-black text-headers text-3xl mb-2">Add a Player</h1>
            <h2 className="font-bold text-headers text-xl mb-8 italic">All Fields Required. Player must not already exist in database</h2>
            { successMessage ? <h3 className="text-green-500 font-semibold text-xl mb-6 pb-4">{successMessage}</h3> : null}
            { error ? <h3 className="text-red-500 font-semibold text-xl mb-6 pb-4">{error}</h3> : null}
            <form>
                <div className="flex flex-wrap gap-2 justify-center items-center">
                    {statEntries}
                </div>
                <button className="w-1/2 mt-8 bg-gray-600 py-2 text-white text-center rounded-lg font-bold cursor-pointer hover:underline">Confirm</button>
            </form>
        </section>
    )
}
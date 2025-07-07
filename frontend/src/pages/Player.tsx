import TeamInformation from "../assets/TeamInformation.tsx"
import { useState, useEffect, useMemo } from "react"
import { useParams, useLocation } from "react-router"
import clsx from "clsx"

import { FaRegBookmark } from "react-icons/fa" // empty
import { FaBookmark } from "react-icons/fa" // filled


const DECIMALS_BY_STAT: Record<string, number> = {
    WAR: 1,
    BA: 3,
    OBP: 3,
    SLG: 3,
    OPS: 3
}

type allStatCategories = {
    player: string,
    pos: string,
    headshot: string,
    team: string,
    war: number,
    id?: number,
    G: number,
    PA: number,
    AB: number,
    R: number,
    H: number,
    '2B': number,
    '3B': number,
    HR: number,
    RBI: number,
    SB: number,
    CS: number,
    BB: number,
    SO: number,
    BA: number,
    OBP: number,
    SLG: number,
    OPS: number,
    'OPS+': number,
    TB: number,
    GIDP: number,
    HBP: number,
    SH: number,
    SF: number,
    IBB: number
}
const mainStatCategories = ["WAR", "AB", "H", "HR", "BA", "R", "RBI", "SB"]
const statCategories = [
    "WAR", "G", "PA", "AB", "R", "H", "2B", "3B", "HR", "RBI", 
    "SB", "CS", "BB", "SO", "BA", "OBP", "SLG", "OPS", "OPS+", 
    "TB", "GIDP", "HBP", "SH", "SF", "IBB"
]  

export default function Player() {

    const { player } = useParams()
    const [completeStats, setCompleteStats] = useState<allStatCategories | null>(null)
    const [bookmarked, setBookmarked] = useState(false)
    const location = useLocation()
    const [status, setStatus] = useState("idle")

    useEffect( () => {
        const dataFetch = async () => {
            if (!player) { return }

            const plrList = player.split('-')
            plrList.pop()
            const playerName = plrList.join(' ')

            try {
                const response = await fetch(`http://localhost:8080/stats/player/request?player=${playerName}&playerId=${player.split('-').pop()}`, 
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

                const statsProfile = await response.json()
                setBookmarked(statsProfile.bookmarked)
                setCompleteStats(statsProfile)
            } catch (error: any) {
                console.error(error.message)
            }
        }
        dataFetch()
    }, [])

    const formatStat = (key: string, value: number | string) => {
        if (typeof(value) !== "number") return value;
        const places: number = DECIMALS_BY_STAT[key] ?? 0
        return value.toFixed(places)
    }

    const mainTableHeaderRows = completeStats ? Object.entries(completeStats).map(([key]) => {
        if (mainStatCategories.filter(current => current.toLowerCase() === key.toLowerCase()).length === 0) { return }
        return <th scope="col" className="w-1/8 px-2 py-1" key={key + "MTH"}>{key.toUpperCase()}</th>
    }) : <td>Loading...</td>
    const mainTableInfo = completeStats ? Object.entries(completeStats).map(([key, value]) => {
        if (mainStatCategories.filter(current => current.toLowerCase() === key.toLowerCase()).length === 0) { return }
        return <th scope="col" className="pb-1" key={key + "MTI"}>{formatStat(key.toUpperCase(), value)}</th>
    }) : <td>Loading...</td>

    const fullTableHeaderRows = completeStats ? Object.entries(completeStats).map(([key]) => {
        if (statCategories.filter(current => current.toLowerCase() === key.toLowerCase()).length === 0) { return }
        return <th scope="col" className="w-1/25 px-2 py-1 text-xs" key={key + "FTH"}>{key.toUpperCase()}</th>
    }) : <td>Loading...</td>
    const fullTableInfo = completeStats ? Object.entries(completeStats).map(([key, value]) => {
        if (statCategories.filter(current => current.toLowerCase() === key.toLowerCase()).length === 0) { return }
        return <td scope="col" className="pb-1 text-sm" key={key + "FTI"}>{formatStat(key.toUpperCase(), value)}</td>
    }) : <td>Loading...</td>

    //<img className="absolute w-500px opacity-25 z-0 top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2" src={logo} />

    const teamBannerClasses = clsx("bg-gray-200", "mx-auto", "w-full", "md:w-3/4", "xl:w-1/2", "text-center", "text-2xl", "py-3", "font-bold", "tracking-widest")
    const teamBannerStyles = {
        backgroundColor: completeStats ? TeamInformation[completeStats.team]?.secondary : "#FFFFFF",
        color: completeStats ? TeamInformation[completeStats.team]?.primary : "#FFFFFF"
    }

    function calculateTier(war: number): string[] {
        if (war >= 3) {
            return ["bg-green-400", "A"]
        } else if (war >= 2.5) {
            return ["bg-orange-600", "B"]
        } else if (war >= 2) {
            return ["bg-yellow-300", "C"]
        } else if (war >= 1.25) {
            return ["bg-amber-700", "D"]
        } else {
            return ["bg-red-700", "F"]
        }
    }
    const playerTier = completeStats ? calculateTier(completeStats.war)[1] : ""
    const playerTierBg = completeStats ? calculateTier(completeStats.war)[0] : ""

    const bookmarkClasses = "absolute top-4 left-2 text-headers fill-cyan-400 text-6xl cursor-pointer hover:motion-preset-shrink"
    
    async function toggleBookmark() {
        if (!completeStats || status === "submitting") { return }
        setStatus("submitting")
        try {
            const response = await fetch("http://localhost:8080/users/me/bookmarks/toggle", 
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    credentials: "include",
                    body: JSON.stringify({
                        player: completeStats.player,
                        headshot: completeStats.headshot,
                        playerId: completeStats.id,
                        endpoint: location.pathname
                    })
                }
            )
            if (!response.ok) {
                const payload = await response.text()
                throw new Error(payload)
                //throw new Error(`Response status: ${response.status}`)
            }

            const isBookmarked = await response.json()
            setBookmarked(isBookmarked)
        } catch (error: any) {
            console.error(error.message)
        } finally {
            setStatus("idle")
        }
    }

    return (
        <>  
            <section className="relative bg-playerbg h-96 overflow-hidden w-full md:w-3/4 xl:w-1/2 mx-auto">
                <div className="absolute bottom-4 left-40 z-2 text-headers">
                    <h2 className="text-4xl font-bold">{completeStats ? completeStats.pos : "Loading..."}</h2>
                    <h1 className="text-5xl font-black">{completeStats ? completeStats.player : "Loading..."}</h1>
                </div>
                {bookmarked ? <FaBookmark className={bookmarkClasses} onClick={toggleBookmark} /> : <FaRegBookmark className={bookmarkClasses} onClick={toggleBookmark} />}
                <span className={`absolute right-4 top-4 ${playerTierBg} z-2 h-1/2 w-48 rounded-full flex justify-center items-center text-5xl font-black text-white`}>{playerTier}</span>
                <img className="absolute bottom-4 left-4 z-1 border-1 border-black rounded-lg" src={completeStats ? completeStats.headshot : undefined} loading="lazy" />
            </section>
            <section style={teamBannerStyles} className={teamBannerClasses}>
                <h4>{completeStats ? TeamInformation[completeStats.team]?.name.toUpperCase() : ""}</h4>
            </section>
            <section className="mx-auto mt-4 w-full md:w-1/2 xl:w-1/3 text-center">
                <h3 className="font-black text-headers text-xl mb-2">Key Statistics</h3>
                <table className="text-white bg-gray-700 table-fixed w-full">
                    <thead>
                        <tr className="bg-black">
                            {mainTableHeaderRows}
                        </tr>
                    </thead>
                    <tbody>
                        <tr className="font-bold">
                            {mainTableInfo}
                        </tr>
                    </tbody>
                </table>
            </section>
            <section className="mx-auto mt-4 w-full md:w-3/4 xl:w-1/2 text-center">
                <h3 className="font-black text-headers text-xl mb-2">Full Statistics</h3>
                <div className="overflow-x-auto w-full">
                    <table className="text-white bg-gray-700 table-fixed w-full min-w-[950px]">
                        <thead>
                            <tr className="bg-black">
                                {fullTableHeaderRows}
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                {fullTableInfo}
                            </tr>
                        </tbody>
                    </table>
                </div>
            </section>

        </>
    )
}
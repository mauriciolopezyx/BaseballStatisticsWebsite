import '../App.css'
import { Link, useLocation, useSearchParams} from "react-router"
import { useEffect, useState } from "react"
import clsx from "clsx"
import TeamInformation from "../assets/TeamInformation.tsx"

type PlayerEntry = {
    player: string,
    endpoint: string
}

export default function SearchPlayer() {

    const location = useLocation()
    const [searchParams] = useSearchParams() // .get with player and team
    const [players, setPlayers] = useState([])

    const playersList = players.map((current: PlayerEntry) => {
        return <Link key={current.endpoint} className="hover:text-blue-500" to={`/players/${current.endpoint}`}>{current.player}</Link>
    })

    useEffect( () => {
        const searchFetch = async () => {
            try {
                const response = await fetch(`http://localhost:8080/stats/player/search?player=${searchParams.get("player")}&team=${searchParams.get("team")}`, 
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
                const playerList = await response.json()
                setPlayers(playerList)
            } catch (error: any) {
                console.error(error.message)
            }
        }
        searchFetch()
    }, [searchParams.get("player"), searchParams.get("team")])

    return (
        <>  
            <section className="flex flex-col mt-10 gap-4 justify-center items-center">
                <h1 className="text-headers font-black text-3xl">Search results for '{searchParams.get("player") ?? ""}'</h1>
                <section className="flex flex-col gap-2 text-2xl font-semibold underline tracking-wider text-paragraph">
                    {playersList}
                </section>
            </section>
        </>
    )
}
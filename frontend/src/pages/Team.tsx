import '../App.css'
import { Link, useParams } from "react-router"
import { useState, useEffect } from "react"
import TeamInformation from "../assets/TeamInformation.tsx"

type PlayerEntry = {
    player: string,
    endpoint: string
}

export default function Team() {

    // needs useParams for custom 'team' field that we specified in our routes (used to fetch + can toUpper it to get info from TeamInformation which contains full name anyways)
    const { team } = useParams()

    const [players, setPlayers] = useState([])
    
    const playersList = players.map((current: PlayerEntry) => {
        return <Link key={current.endpoint} className="hover:text-blue-500" to={`/players/${current.endpoint}`}>{current.player}</Link>
    })

    useEffect( () => {
        const searchFetch = async () => {
            try {
                const response = await fetch(`http://localhost:8080/stats/team/search?team=${team}`, 
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
    }, [team])

    return (
        <>  
            <section className="flex flex-col mt-10 gap-4 justify-center items-center">
                <h1 className="text-headers font-black text-3xl">All {team && TeamInformation[team.toUpperCase()] ? TeamInformation[team.toUpperCase()].name : ""} Players</h1>
                {players.length > 0 ? <section className="flex flex-col gap-2 text-2xl font-semibold underline tracking-wider text-paragraph">
                    {playersList}
                </section> : <h2 className="text-3xl text-headers">Loading...</h2>}
            </section>
        </>
    )
}
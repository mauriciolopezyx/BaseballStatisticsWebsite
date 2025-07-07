import '../App.css'
import { Link } from "react-router"
import { useState } from "react"
import clsx from "clsx"
import TeamInformation from "../assets/TeamInformation.tsx"

export default function Search() {

    const [hoveredTeam, setHoveredTeam] = useState<string | null>(null)

    const fullTableInfo = Object.entries(TeamInformation).map(([teamName, teamInfo]) => {
        const teamStyles = {
            backgroundColor: teamInfo?.secondary ?? "#FFFFFF",
            color: teamInfo?.primary ?? "#FFFFFF"
        }
        return (
            <Link key={teamName} to={`../teams/${teamName.toLowerCase()}`} relative="path" state={{team: teamInfo.name}}>
                <div style={teamStyles} key={teamName + "-team"} className="relative overflow-hidden h-full bg-gray-100 font-semibold flex justify-center items-center shadow-md rounded-md hover:motion-preset-seesaw motion-duration-500 hover:underline"
                onMouseEnter={ () => { setHoveredTeam(teamInfo.name) } }
                onMouseLeave={ () => { setHoveredTeam(null) } }
                >
                    <span className="z-1 tracking-widest">{teamName}</span>
                    {teamInfo.logo ? <img className="absolute opacity-33 z-0 w-[150px] max-w-none top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2" src={teamInfo.logo} loading="lazy"/> : null}
                </div>
            </Link>
        )
    })

    const optionalHeaderClasses = clsx("text-headers", "font-bold", "text-2xl", hoveredTeam ? "visible" : "invisible")

    return (
        <div className="flex flex-col gap-15 justify-center items-center min-h-screen">
            <h1 className={optionalHeaderClasses}>{hoveredTeam ? hoveredTeam : "Hidden"}</h1>
            <div className="grid grid-cols-6 grid-rows-5 md:gap-2 w-128 md:w-156 h-96 md:h-128">
                {fullTableInfo}
            </div>
        </div>
    )
}
import '../App.css'
import { useState, useEffect } from "react"
import { Link } from "react-router"
import clsx from "clsx"

type PlayerHistoryEntry = {
    player: string,
    headshot: string,
    endpoint: string
}

export default function Home() {

    const slotClasses = clsx("text-center", "text-headers", "gap-2", "w-28", "h-52", "p-2", "shadow-md", "bg-homeslot", "flex", "flex-col", "overflow-hidden", "rounded-lg", "justify-center")
    
    const [recentlyViewed, setRecentlyViewed] = useState<Array<PlayerHistoryEntry>>([])
    const [bookmarked, setBookmarked] = useState<Array<PlayerHistoryEntry>>([])

    const recentlyViewedSlots = Array.from({ length: 10 }, (_, i) => {
        if (i < recentlyViewed.length) {
          return (
            <Link to={"/players/" + recentlyViewed[i].endpoint}>
                <div key={recentlyViewed[i].player + "-rv"} className={clsx(slotClasses, "cursor-pointer")} >
                    {recentlyViewed[i]?.headshot ? <img className="min-h-0 rounded-lg" src={recentlyViewed[i].headshot} loading="lazy" /> : null}
                    <p className="text-sm font-semibold break-words">{recentlyViewed[i].player}</p>
                </div>
            </Link>
          )
        }
        return (
            <div key={`blank-${i}`} className={slotClasses}></div>
        )
    })

    const recentlyBookmarked = Array.from({ length: 10 }, (_, i) => {
        if (i < bookmarked.length) {
          return (
            <Link to={bookmarked[i].endpoint}>
                <div key={bookmarked[i].player + "-rb"} className={clsx(slotClasses, "cursor-pointer")}>
                    {bookmarked[i]?.headshot ? <img className="min-h-0 rounded-lg" src={bookmarked[i].headshot} loading="lazy"/> : null}
                    <p className="text-sm font-semibold break-words">{bookmarked[i].player}</p>
                </div>
            </Link>
          )
        }
        return (
            <div key={`blank-${i}`} className={slotClasses}></div>
        )
    })

    useEffect( () => {
        const dataFetch = async () => {
            try {
                const response = await fetch("http://localhost:8080/users/me/request/metadata", 
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
                const overview = await response.json()
                setRecentlyViewed(overview.recentlyViewed)
                setBookmarked(overview.bookmarked)
            } catch (error: any) {
                console.error(error.message)
            }
        }
        dataFetch()
    }, [])

    return (
        <>
            <section className="p-8 flex flex-col gap-8">
                <h1 className="text-headers text-5xl font-black">Home</h1>
                <section>
                    <h2 className="text-headers text-3xl font-bold mb-4">Recently Viewed</h2>
                    <section className="flex flex-wrap gap-4">
                        {recentlyViewedSlots}
                    </section>
                </section>
                <section>
                    <h2 className="text-headers text-3xl font-bold mb-4">Bookmarked Players</h2>
                    <section className="flex flex-wrap gap-4">
                        {recentlyBookmarked}
                    </section>
                </section>
            </section>
        </>
    )
}
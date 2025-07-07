import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter, Routes, Route, Link } from "react-router"
import Layout from "./layouts/Layout.tsx"
import Home from "./pages/Home.tsx"
import Search from "./pages/Search.tsx"
import Settings from "./pages/Settings.tsx"
import Register from "./pages/Register.tsx"
import Login from "./pages/Login.tsx"
import Player from "./pages/Player.tsx"
import Team from "./pages/Team.tsx"
import SearchPlayer from "./pages/SearchPlayer.tsx"
import ResetPassword from "./pages/ResetPassword.tsx"
import PrivateRoute from './layouts/PrivateRoute.tsx'
import Verify from "./pages/Verify.tsx"
import PlayerConfig from "./pages/PlayerConfig.tsx"
import { AuthProvider } from "./layouts/Authentication.tsx"

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>

          <Route path="register" element={<Register />} />
          <Route path="login" element={<Login />} />
          <Route path="verify" element={<Verify />} />

          <Route element={<PrivateRoute />}>
            <Route index element={<Home />} />
            <Route path="search" element={<Search />} />
            <Route path="settings" element={<Settings />} />

            <Route path="players/:player" element={<Player/>} />
            <Route path="teams/:team" element={<Team/>}/>

            <Route path="search/players" element={<SearchPlayer/>}/>
            <Route path="player/config" element={<PlayerConfig />}/>
            <Route path="password/reset" element={<ResetPassword />}/>
          </Route>

        </Route>
      </Routes>
    </BrowserRouter>
  )
}

createRoot(document.getElementById('root')!).render(
  <AuthProvider >
    <App />
  </AuthProvider>
)

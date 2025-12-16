// import { useState } from 'react'
// import reactLogo from './assets/react.svg'
// import viteLogo from '/vite.svg'
import NavBar from "./components/NavBar";
import Hero from "./components/Hero";
import Dashboard from "./components/Dashboard.jsx";


function App() {
  // const [count, setCount] = useState(0)

  return (
    <>
      <NavBar />
      <Hero />
      <Dashboard />
    </>
  )
}

export default App

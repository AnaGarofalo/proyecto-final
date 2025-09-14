import './App.css'
import { Route, Routes } from 'react-router-dom'
import Login from './screen/Login'
import Dashboard from './screen/Dashboard'
import { NavigationRoute } from './utils/NavigationUtils'

function App() {
  return (
    <Routes>
      <Route path={NavigationRoute.LOGIN} element={<Login />} />
      <Route path={NavigationRoute.DASHBOARD} element={<Dashboard />} />
    </Routes>
  )
}

export default App

import "./App.css";
import { Navigate, Route, Routes, useNavigate } from "react-router-dom";
import { useEffect } from "react";
import Login from "./screen/Login";
import Layout from "./components/Layout";
import ProtectedRoute from "./components/ProtectedRoute";
import { NavigationRoute } from "./utils/NavigationUtils";
import HomePage from "./screen/HomePage";
import EditPrompt from "./screen/EditPrompt";
import Document from "./screen/Document";
import ChatUsers from "./screen/ChatUsers";
import Users from "./screen/Users";
import Ticket from "./screen/Ticket";

function App() {
  const navigate = useNavigate();

  useEffect(() => {
    // Listener para el evento de logout automático
    const handleAutoLogout = () => {
      console.log('Auto logout event received - Redirecting to login');
      navigate(NavigationRoute.LOGIN);
    };

    window.addEventListener('autoLogout', handleAutoLogout);

    // Cleanup
    return () => {
      window.removeEventListener('autoLogout', handleAutoLogout);
    };
  }, [navigate]);

  return (
    <Routes>
      <Route path={NavigationRoute.LOGIN} element={<Login />} />
      <Route element={
        <ProtectedRoute>
          <Layout />
        </ProtectedRoute>
      }>
        {/* Todas estas rutas automáticamente protegidas */}
        <Route path={NavigationRoute.HOMEPAGE} element={<HomePage />} />
        <Route path={NavigationRoute.EDIT_PROMPT} element={<EditPrompt />} />
        <Route path={NavigationRoute.DOCUMENTS} element={<Document />} />
        <Route path={NavigationRoute.CHAT_USERS} element={<ChatUsers />} />
        <Route path={NavigationRoute.USERS} element={<Users />} />
        <Route path={NavigationRoute.TICKET} element={<Ticket />} />
        <Route path="*" element={<Navigate to="/homepage" replace />} />
      </Route>
    </Routes>
  );
}

export default App;

import "./App.css";
import { Route, Routes, useNavigate } from "react-router-dom";
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
      <Route element={<Layout />}>
        <Route path={NavigationRoute.HOMEPAGE} element={
          <ProtectedRoute>
            <HomePage />
          </ProtectedRoute>
        } />
        <Route path={NavigationRoute.EDIT_PROMPT} element={
          <ProtectedRoute>
            <EditPrompt />
          </ProtectedRoute>
        } />
        <Route path={NavigationRoute.DOCUMENTS} element={
          <ProtectedRoute>
            <Document />
          </ProtectedRoute>
        } />
        <Route path={NavigationRoute.CHAT_USERS} element={
          <ProtectedRoute>
            <ChatUsers />
          </ProtectedRoute>
        } />
        <Route path={NavigationRoute.USERS} element={
          <ProtectedRoute>
            <Users />
          </ProtectedRoute>
        } />
        <Route path={NavigationRoute.HOMEPAGE} element={<HomePage />} />
        <Route path={NavigationRoute.EDIT_PROMPT} element={<EditPrompt />} />
        <Route path={NavigationRoute.DOCUMENTS} element={<Document />} />
        <Route path={NavigationRoute.CHAT_USERS} element={<ChatUsers />} />
        <Route path={NavigationRoute.USERS} element={<Users />} />
      </Route>
    </Routes>
  );
}

export default App;

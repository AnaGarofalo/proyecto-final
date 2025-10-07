import "./App.css";
import { Route, Routes } from "react-router-dom";
import Login from "./screen/Login";
import Layout from "./components/Layout";
import { NavigationRoute } from "./utils/NavigationUtils";
import HomePage from "./screen/HomePage";
import EditPrompt from "./screen/EditPrompt";
import Document from "./screen/Document";
import ChatUsers from "./screen/ChatUsers";
import Users from "./screen/Users";

function App() {
  return (
    <Routes>
      <Route path={NavigationRoute.LOGIN} element={<Login />} />
      <Route element={<Layout />}>
        <Route path={NavigationRoute.HOMEPAGE} element={<HomePage />} />
        <Route path={NavigationRoute.EDITPROMPT} element={<EditPrompt />} />
        <Route path={NavigationRoute.DOCUMENTS} element={<Document />} />
        <Route path={NavigationRoute.CHATUSERS} element={<ChatUsers />} />
        <Route path={NavigationRoute.USERS} element={<Users />} />
      </Route>
    </Routes>
  );
}

export default App;

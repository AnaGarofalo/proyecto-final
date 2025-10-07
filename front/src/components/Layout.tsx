import { Box, Toolbar } from "@mui/material";
import Sidebar from "./Sidebar";
import Header from "./Header";
import { Outlet } from "react-router-dom";
import { Colors } from "../utils/Colors";

const Layout = () => {
  return (
    <Box sx={{ display: "flex" }}>
      <Header />
      <Sidebar />
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          bgcolor: Colors.SEPTENARY_WHITE,
          p: 3,
        }}
      >
        <Toolbar />
        <Outlet />
      </Box>
    </Box>
  );
};

export default Layout;

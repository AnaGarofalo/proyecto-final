import { Box, Toolbar } from "@mui/material";
import Sidebar from "./Sidebar";
import Header from "./Header";
import { Outlet } from "react-router-dom";
import { Colors } from "../utils/Colors";

export const DRAWER_WIDTH_PX = 240;
export const HEADER_HEIGHT_PX = 120;

const Layout = () => {
  return (
    <Box       
      sx={{ display: "flex" }}>
      <Header />
      <Sidebar />
      <Box
        component="main"
        sx={{
          flexGrow: 1,
          bgcolor: Colors.SEPTENARY_WHITE,
          width: `calc(100vw - ${DRAWER_WIDTH_PX}px)`,
          height: `calc(100vh - ${HEADER_HEIGHT_PX}px)`,
          marginTop: HEADER_HEIGHT_PX + "px", 
          p: 4, 
          overflow: "auto"
        }}
      >
        <Outlet />
      </Box>
    </Box>
  );
};

export default Layout;

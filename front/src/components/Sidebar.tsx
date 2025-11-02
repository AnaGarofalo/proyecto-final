import {
  Drawer,
  List,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Box,
} from "@mui/material";
import HomeIcon from "@mui/icons-material/HomeFilled";
import EditNoteIcon from "@mui/icons-material/EditNote";
import DescriptionIcon from "@mui/icons-material/Description";
import WhatsAppIcon from "@mui/icons-material/WhatsApp";
import PeopleIcon from "@mui/icons-material/People";
import LogoutIcon from "@mui/icons-material/Logout";
import TicketIcon from "@mui/icons-material/ConfirmationNumber";
import { useNavigate, useLocation } from "react-router-dom";
import { Colors } from "../utils/Colors";
import { NavigationRoute } from "../utils/NavigationUtils";
import { DRAWER_WIDTH_PX, HEADER_HEIGHT_PX } from "./Layout";
import BaseButton from "./base/BaseButton";
import ChatIcon from "@mui/icons-material/Chat";

const Sidebar = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const menuItems = [
    { text: "Inicio", icon: <HomeIcon />, path: NavigationRoute.HOMEPAGE },
    {
      text: "Editar Prompt",
      icon: <EditNoteIcon />,
      path: NavigationRoute.EDIT_PROMPT,
    },
    {
      text: "Documentos",
      icon: <DescriptionIcon />,
      path: NavigationRoute.DOCUMENTS,
    },
    {
      text: "Usuarios chat",
      icon: <WhatsAppIcon />,
      path: NavigationRoute.CHAT_USERS,
    },
    { text: "Usuarios", icon: <PeopleIcon />, path: NavigationRoute.USERS },
    { text: "Tickets", icon: <TicketIcon />, path: NavigationRoute.TICKET },
    { text: "Chat", icon: <ChatIcon />, path: NavigationRoute.CHAT }, 
  ];
  // Funcion para manejar el cierre de sesion

  const handleLogout = () => {
    // Eliminar token del localStorage
    localStorage.removeItem("access_token");

    // Redirigir al login usando la ruta correcta
    navigate(NavigationRoute.LOGIN);
  };

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: DRAWER_WIDTH_PX,
        flexShrink: 0,
        [`& .MuiDrawer-paper`]: {
          width: DRAWER_WIDTH_PX,
          backgroundColor: Colors.PRIMARY_DARK_BLUE,
          color: Colors.SEPTENARY_WHITE,
          boxSizing: "border-box",
        },
      }}
    >
      <Box sx={{ display: "flex", flexDirection: "column", height: "100%" }}>
        <Box sx={{ height: `${HEADER_HEIGHT_PX}px` }} />

        {/* Menú principal */}
        <List>
          {menuItems.map((item) => (
            <ListItemButton
              key={item.text}
              selected={location.pathname === item.path}
              onClick={() => navigate(item.path)}
              sx={{
                "&.Mui-selected": {
                  backgroundColor: Colors.PRIMARY_DARK_BLUE,
                },
                "&:hover": {
                  backgroundColor: Colors.SECONDARY_LIGHT_BLUE,
                },
              }}
            >
              <ListItemIcon sx={{ color: Colors.SEPTENARY_WHITE }}>
                {item.icon}
              </ListItemIcon>
              <ListItemText primary={item.text} />
            </ListItemButton>
          ))}
        </List>

        {/* Botón Cerrar sesión */}
        <Box sx={{ mt: "auto", mb: 2, px: 2 }}>
          <BaseButton
            variantType="logout"
            icon={<LogoutIcon />}
            onClick={handleLogout}
            fullWidth
          >
            Cerrar sesión
          </BaseButton>
        </Box>
      </Box>
    </Drawer>
  );
};

export default Sidebar;

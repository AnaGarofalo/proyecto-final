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
import { useNavigate, useLocation } from "react-router-dom";
import { Colors } from "../utils/Colors";
import { NavigationRoute } from "../utils/NavigationUtils";

const drawerWidth = 240;
const headerHeight = 120;

const Sidebar = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const menuItems = [
    { text: "Inicio", icon: <HomeIcon />, path: "/homepage" },
    { text: "Editar Prompt", icon: <EditNoteIcon />, path: "/editprompt" },
    { text: "Documentos", icon: <DescriptionIcon />, path: "/documents" },
    { text: "Usuarios chat", icon: <WhatsAppIcon />, path: "/chatusers" },
    { text: "Usuarios", icon: <PeopleIcon />, path: "/users" },
  ];
  // Funcion para manejar el cierre de sesion 

  const handleLogout = () => {
    // Eliminar token del localStorage
    localStorage.removeItem('access_token');

    // Redirigir al login usando la ruta correcta
    navigate(NavigationRoute.LOGIN);
  };

  return (
    <Drawer
      variant="permanent"
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        [`& .MuiDrawer-paper`]: {
          width: drawerWidth,
          backgroundColor: Colors.PRIMARY_DARK_BLUE,
          color: Colors.SEPTENARY_WHITE,
          boxSizing: "border-box",
        },
      }}
    >
      <Box sx={{ display: "flex", flexDirection: "column", height: "100%" }}>
        <Box sx={{ height: `${headerHeight}px` }} />

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
              <ListItemIcon sx={{ color: Colors.SEPTENARY_WHITE }}>{item.icon}</ListItemIcon>
              <ListItemText primary={item.text} />
            </ListItemButton>
          ))}
        </List>

        {/* Botón Cerrar sesión */}
        <Box sx={{ mt: "auto", mb: 2 }}>
          <ListItemButton
            onClick={handleLogout}
            sx={{
              backgroundColor: Colors.QUARTERNARY_DARK_GRAY,
              "&:hover": {
                backgroundColor: Colors.QUINARY_LIGHT_GRAY,
              },
            }}
          >
            <ListItemIcon sx={{ color: Colors.SEPTENARY_WHITE }}>
              <LogoutIcon />
            </ListItemIcon>
            <ListItemText primary="Cerrar sesión" />
          </ListItemButton>
        </Box>
      </Box>
    </Drawer>
  );
};

export default Sidebar;


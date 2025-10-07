import { AppBar, Toolbar, Typography, Box } from "@mui/material";
import nestleLogo from "../assets/logo/nestleLogo.png";
import { Colors } from "../utils/Colors";

// Constantes para mantener consistencia con el Sidebar
const drawerWidth = 240;
const headerHeight = 120;

const Header = () => {
  return (
    <AppBar
      position="fixed"
      sx={{
        backgroundColor: Colors.QUARTERNARY_DARK_GRAY,
        zIndex: (theme) => theme.zIndex.drawer + 1,
        width: `calc(100% - ${drawerWidth}px)`,
        ml: `${drawerWidth}px`,
        height: `${headerHeight}px`,
      }}
    >
      <Toolbar
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          minHeight: `${headerHeight}px !important`,
          px: 3,
        }}
      >
        <Typography
          variant="subtitle1"
          sx={{
            fontSize: 26,
            color: Colors.QUINARY_LIGHT_GRAY,
            fontWeight: 400,
          }}
        >
          {/* ACA PONER LA LOGICA PARA MOSTRAR AL USUARIO LOGUEADO */}
          Usuario: Homero J
        </Typography>

        <Box
          component="img"
          src={nestleLogo}
          alt="Nestlé"
          sx={{
            height: 82,
            objectFit: "contain",
          }}
        />
      </Toolbar>
    </AppBar>
  );
};

export default Header;

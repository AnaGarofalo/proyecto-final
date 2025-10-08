import { AppBar, Toolbar, Typography, Box } from "@mui/material";
import nestleLogo from "../assets/logo/nestleLogo.png";
import { Colors } from "../utils/Colors";
import { DRAWER_WIDTH_PX, HEADER_HEIGHT_PX } from "./Layout";

const Header = () => {
  return (
    <AppBar
      position="fixed"
      sx={{
        backgroundColor: Colors.QUARTERNARY_DARK_GRAY,
        zIndex: (theme) => theme.zIndex.drawer + 1,
        width: `calc(100% - ${DRAWER_WIDTH_PX}px)`,
        ml: `${DRAWER_WIDTH_PX}px`,
        height: `${HEADER_HEIGHT_PX}px`,
      }}
    >
      <Toolbar
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          minHeight: `${HEADER_HEIGHT_PX}px !important`,
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

import { AppBar, Toolbar, Typography, Box } from "@mui/material";
import { useState, useEffect } from "react";
import nestleLogo from "../assets/logo/nestleLogo.png";
import { Colors } from "../utils/Colors";
import { DRAWER_WIDTH_PX, HEADER_HEIGHT_PX } from "./Layout";

// Función para extraer el email del usuario desde el token JWT
const getUserEmailFromToken = (): string | null => {
  try {
    const token = localStorage.getItem('access_token');
    if (!token) return null;
    
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub; // 'sub' contiene el email en el JWT
  } catch (error) {
    console.error('Error al decodificar el token:', error);
    return null;
  }
};

const Header = () => {
  const [userEmail, setUserEmail] = useState<string | null>(null);

  useEffect(() => {
    // Extraer el email del token cuando se monta el componente
    const email = getUserEmailFromToken();
    setUserEmail(email);
  }, []);

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
          {/* Mostrar el email real del usuario logueado */}
          Usuario: {userEmail || 'Usuario'}
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

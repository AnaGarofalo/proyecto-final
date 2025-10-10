import { Box, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { Colors } from "../utils/Colors";
import BaseButton from "../components/base/BaseButton";

export default function DocumentSuccess() {
  const navigate = useNavigate();

  return (
    <Box
      sx={{
        height: "100vh",
        display: "flex",
        flexDirection: "column",
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: Colors.SEPTENARY_WHITE,
        gap: 3,
        transform: "translateY(-15%)",
      }}
    >
      <Typography
        variant="h4"
        sx={{ fontWeight: 400, color: Colors.QUARTERNARY_DARK_GRAY }}
      >
        Documento cargado con éxito
      </Typography>

      <BaseButton
        variantType="filled"
        onClick={() => navigate(-1)}
        sx={{
          px: 2.5,
          py: 1,
          width: "260px",         // ✅ ancho ajustado al texto
          alignSelf: "center",   // ✅ mantiene centrado
          fontWeight: 600,
          backgroundColor: Colors.PRIMARY_DARK_BLUE,
          color: Colors.SEPTENARY_WHITE,
          "&:hover": { backgroundColor: Colors.HOVER_BLUE },
        }}
      >
        Volver
      </BaseButton>
    </Box>
  );
}

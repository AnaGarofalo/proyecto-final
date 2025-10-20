import { Typography } from "@mui/material";
import { getUserEmailFromToken } from "../utils/JwtUtils";

export default function HomePage() {
  const userEmail = getUserEmailFromToken();

  return (
    <Typography variant="h5" sx={{ mt: 4 }}>
      ¡Bienvenido {userEmail ? userEmail : ""}!
    </Typography>
  );
}

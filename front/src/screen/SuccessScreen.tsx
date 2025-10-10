import { Box, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { Colors } from "../utils/Colors";
import BaseButton from "../components/base/BaseButton";

interface SuccessScreenProps {
  title: string;
}

export default function SuccessScreen({ title }: SuccessScreenProps) {
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
        {title}
      </Typography>

      <BaseButton
        variantType="filled"
        onClick={() => navigate(-1)}
        fullWidth={false}
        sx={{
          py: 1,
          alignSelf: "center",
        }}
      >
        Volver
      </BaseButton>
    </Box>
  );
}
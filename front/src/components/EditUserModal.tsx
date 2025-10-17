import { useState } from "react";
import { Box } from "@mui/material";
import BaseModal from "./base/BaseModal";
import BaseInput from "./base/BaseInput";
import { IconButton, InputAdornment } from "@mui/material";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import type { AppUserMinimalDTO } from "../model/AppUser";

interface EditUserModalProps {
  open: boolean;
  user: AppUserMinimalDTO | null;
  onClose: () => void;
  onSave: (email: string, password: string) => void;
}

export default function EditUserModal({ open, user, onClose, onSave }: EditUserModalProps) {
  const [email, setEmail] = useState(user?.email || "");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);

  const handleConfirm = () => {
    onSave(email, password);
    setPassword("");
  };

  const handleClose = () => {
    setEmail(user?.email || "");
    setPassword("");
    onClose();
  };

  return (
    <BaseModal
      open={open}
      onClose={handleClose}
      onConfirm={handleConfirm}
      title="Editar usuario"
      disableConfirm={!email || (email === user?.email && !password)}
    >
      <Box component="form">
        <BaseInput
          label="Correo electrónico"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          margin="normal"
          required
          fullWidth
          autoFocus
        />

        <BaseInput
          label="Nueva Contraseña"
          type={showPassword ? "text" : "password"}
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          margin="normal"
          fullWidth
          InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <IconButton
                  onClick={() => setShowPassword(!showPassword)}
                  edge="end"
                >
                  {showPassword ? <VisibilityOff /> : <Visibility />}
                </IconButton>
              </InputAdornment>
            ),
          }}
        />
      </Box>
    </BaseModal>
  );
}
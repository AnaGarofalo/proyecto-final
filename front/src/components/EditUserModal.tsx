import { useState, useEffect } from "react";
import { Box } from "@mui/material";
import BaseModal from "./base/BaseModal";
import BaseInput from "./base/BaseInput";
import { IconButton, InputAdornment } from "@mui/material";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import type { AppUserMinimalDTO } from "../model/AppUser";
import { useForm, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";
import { createUserSchema } from "../utils/PasswordUtils";

type EditUserFormData = z.infer<typeof createUserSchema>;

interface EditUserModalProps {
  open: boolean;
  user: AppUserMinimalDTO | null;
  onClose: () => void;
  onSave: (email: string, password: string) => void;
}

export default function EditUserModal({ open, user, onClose, onSave }: EditUserModalProps) {
  const [showPassword, setShowPassword] = useState(false);
  
  const { register, handleSubmit, reset, formState: { errors }, setValue } = useForm<EditUserFormData>({
    resolver: zodResolver(createUserSchema),
    defaultValues: {
      email: "",
      password: "",
    }
  });

  useEffect(() => {
    if (user) {
      setValue("email", user.email);
    }
  }, [user, setValue]);

  const handleFormSubmit: SubmitHandler<EditUserFormData> = (data) => {
    onSave(data.email, data.password || "");
    reset({ email: data.email, password: "" });
  };

  const handleClose = () => {
    reset();
    onClose();
  };

  return (
    <BaseModal
      open={open}
      onClose={handleClose}
      onConfirm={handleSubmit(handleFormSubmit)}
      title="Editar usuario"
    >
      <Box component="form">
        <BaseInput
          label="Email"
          type="email"
          {...register('email')}
          error={!!errors.email}
          errorMessage={errors.email?.message}
          margin="normal"
          required
          fullWidth
          autoFocus
        />

        <BaseInput
          label="Nueva Contraseña"
          type={showPassword ? "text" : "password"}
          {...register('password')}
          error={!!errors.password}
          errorMessage={errors.password?.message}
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
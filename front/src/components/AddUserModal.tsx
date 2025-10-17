import { Box } from "@mui/material";
import BaseButton from "./base/BaseButton";
import { useState } from "react";
import BaseModal from "./base/BaseModal";
import BaseInput from "./base/BaseInput";
import { IconButton, InputAdornment } from "@mui/material";
import Visibility from "@mui/icons-material/Visibility";
import VisibilityOff from "@mui/icons-material/VisibilityOff";
import { useForm, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { ToastUtil } from "../utils/ToastUtils";
import AppUserService from "../service/AppUserService";
import type { AppUserMinimalDTO } from "../model/AppUser";
import { z } from "zod";

const createUserSchema = z.object({
  email: z.string().email("Email inválido"),
  password: z.string()
    .regex(
      /^(?=.*[A-Z])(?=.*\d).{6,}$/,
      "La contraseña debe tener al menos 6 caracteres, una Mayúscula y un número"
    ),
});

type CreateUserFormData = z.infer<typeof createUserSchema>;

interface AddUserModalProps {
  onUserAdded: (user: AppUserMinimalDTO) => void;
  existingUsers?: AppUserMinimalDTO[];
}

export function AddUserModal({ onUserAdded, existingUsers = [] }: AddUserModalProps) {
  const [showModal, setShowModal] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const { register, handleSubmit, getValues, reset, formState: { errors } } = useForm<CreateUserFormData>({
    resolver: zodResolver(createUserSchema),
  });

  const createUser: SubmitHandler<CreateUserFormData> = async (data: CreateUserFormData) => {
    try {
      const emailToCreate = getValues('email');
      const existingUser = existingUsers.find(user => user.email === emailToCreate);

      if (existingUser) {
        ToastUtil.warning("El email ya está en uso");
        return;
      }

      const createdUser = await AppUserService.create(data);
      ToastUtil.success("Usuario creado exitosamente");
      onUserAdded(createdUser.data);
      reset();
      setShowModal(false);
    } catch (e) {
      ToastUtil.error("Error al crear usuario");
      console.error(e);
    }
  };

  const handleClose = () => {
    reset();
    setShowModal(false);
  };

  return (
    <Box sx={{
      width: "100%",
      padding: "24px",
      display: "flex",
      flexDirection: "row",
      justifyContent: "end"
    }}>
      <BaseButton fullWidth={false} onClick={() => setShowModal(true)}>
        Agregar Usuario
      </BaseButton>

      <BaseModal
        open={showModal}
        onClose={handleClose}
        title="Agregar usuario"
        onConfirm={handleSubmit(createUser)}
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
          />

          <BaseInput
            label="Contraseña"
            type={showPassword ? "text" : "password"}
            {...register('password')}
            error={!!errors.password}
            errorMessage={errors.password?.message}
            margin="normal"
            required
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
    </Box>
  );
}
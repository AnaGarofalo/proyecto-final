import React, { useState } from "react";
import { useForm, type SubmitHandler } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";

import BaseInput from "../components/base/BaseInput";
import BaseButton from "../components/base/BaseButton";
import { loginSchema, type AppUserLogin } from "../model/AppUserLogin";
import AppUserService from "../service/AppUserService";
import { ToastUtil } from "../utils/ToastUtils";
import { useNavigate } from "react-router-dom";
import { NavigationRoute } from "../utils/NavigationUtils";
import { Box, IconButton, InputAdornment } from "@mui/material";
import nestleLogo from "../assets/logo/nestleLogo.png";
import { GridVisibilityOffIcon } from "@mui/x-data-grid";

const Login: React.FC = () => {
  const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);
  
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<AppUserLogin>({
    resolver: zodResolver(loginSchema),
  });

  const login: SubmitHandler<AppUserLogin> = async (data: AppUserLogin) => {
    try {
      const res = await AppUserService.login(data);
      if (res.data.token) {
        localStorage.setItem("access_token", res.data.token);
      }
      ToastUtil.success("Login exitoso");
      navigate(NavigationRoute.HOMEPAGE);
    } catch (e) {
      ToastUtil.error("Error al iniciar sesión");
      console.error(e);
    }
  };

  return (
    <Box sx={{ width: "100vw" }}>
      <form
        onSubmit={handleSubmit(login)}
        style={{ maxWidth: 400, margin: "auto", padding: 20 }}
      >
        {/* Solo agregar el logo aquí */}
        <Box sx={{ textAlign: "center", mb: 2 }}>
          <img
            src={nestleLogo}
            alt="Nestlé"
            style={{ height: "80px", objectFit: "contain" }}
          />
        </Box>
        <BaseInput
          label="Email"
          type="email"
          {...register("email")}
          error={!!errors.email}
          errorMessage={errors.email?.message}
          margin="normal"
          required
        />

        <BaseInput
          label="Contraseña"
          type={showPassword ? "text" : "password"}
          {...register("password")}
          error={!!errors.password}
          errorMessage={errors.password?.message}
          margin="normal"
          required
                    InputProps={{
            endAdornment: (
              <InputAdornment position="end">
                <IconButton
                  onClick={() => setShowPassword(!showPassword)}
                  edge="end"
                >
                  {showPassword ? <GridVisibilityOffIcon /> : <GridVisibilityOffIcon />}
                </IconButton>
              </InputAdornment>
            ),
          }}
        />

        <BaseButton
          type="submit"
          disabled={isSubmitting}
          sx={{ mt: 2 }}
          fullWidth
        >
          {isSubmitting ? "Ingresando..." : "Ingresar"}
        </BaseButton>
      </form>
    </Box>
  );
};

export default Login;

import React from 'react'
import { useForm, type SubmitHandler } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'

import BaseInput from '../components/base/BaseInput'
import BaseButton from '../components/base/BaseButton'
import { loginSchema, type AppUserLogin } from '../model/AppUserLogin'
import AppUserService from '../service/AppUserService'
import { ToastUtil } from '../utils/ToastUtils'
import { useNavigate } from 'react-router-dom'
import { NavigationRoute } from '../utils/NavigationUtils'
import { Box } from '@mui/material'

const Login: React.FC = () => {
  const navigate = useNavigate();
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<AppUserLogin>({
    resolver: zodResolver(loginSchema),
  })

  const login: SubmitHandler<AppUserLogin> = async (data: AppUserLogin) => {
    try {
      const res = await AppUserService.login(data)
      if (res.data.token) {
        localStorage.setItem('access_token', res.data.token)
      }
      ToastUtil.success("Login exitoso");
      navigate(NavigationRoute.HOMEPAGE)
    } catch (e) {
      ToastUtil.error("Error al iniciar sesión");
      console.error(e);
    }
  }

  return (
    <Box sx={{ width: "100vw" }}>
      <form onSubmit={handleSubmit(login)} style={{ maxWidth: 400, margin: 'auto', padding: 20 }}>
        <BaseInput
          label="Email"
          type="email"
          {...register('email')}
          error={!!errors.email}
          errorMessage={errors.email?.message}
          margin="normal"
          required
        />

        <BaseInput
          label="Contraseña"
          type="password"
          {...register('password')}
          error={!!errors.password}
          errorMessage={errors.password?.message}
          margin="normal"
          required
        />

        <BaseButton type="submit" disabled={isSubmitting} sx={{ mt: 2 }}>
          {isSubmitting ? 'Ingresando...' : 'Ingresar'}
        </BaseButton>
      </form>
    </Box>
  )
}

export default Login
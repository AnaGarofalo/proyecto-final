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

const Login: React.FC = () => {
  const navigate = useNavigate();
  const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<AppUserLogin>({
    resolver: zodResolver(loginSchema),
  })

  const login: SubmitHandler<AppUserLogin> = (data: AppUserLogin) => {
    AppUserService.login(data)
      .then((res) => {
        navigate(NavigationRoute.DASHBOARD)
        ToastUtil.success("Login exitoso");
      })
      .catch((e) =>  ToastUtil.error(e.message))
  }

  return (
    <form onSubmit={handleSubmit(login)} style={{ maxWidth: 400, margin: 'auto', padding: 20 }}>
      <BaseInput
        label="Email"
        type="text"
        {...register('email')}
        error={!!errors.email}
        errorMessage={errors.email?.message}
        margin="normal"
      />

      <BaseInput
        label="Contraseña"
        type="password"
        {...register('password')}
        error={!!errors.password}
        errorMessage={errors.password?.message}
        margin="normal"
      />

      <BaseButton type="submit" disabled={isSubmitting} sx={{ mt: 2 }}>
        {isSubmitting ? 'Ingresando...' : 'Ingresar'}
      </BaseButton>
    </form>
  )
}

export default Login
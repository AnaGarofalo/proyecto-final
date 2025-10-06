import * as z from 'zod'

export interface AppUserLogin {
  email: string;
  password: string;
}

// Interfaz para la respuesta del login (token JWT)
export interface LoginResponse {
  token: string;
}

export const loginSchema = z.object({
  email: z.string().email('Email inválido'),
  password: z.string().min(6, 'La contraseña debe tener al menos 6 caracteres, una Mayuscula y un numero'),
})
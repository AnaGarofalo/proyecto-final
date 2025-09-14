import * as z from 'zod'


export interface AppUserLogin {
  email: string;
  password: string;
}

export const loginSchema = z.object({
  email: z.string().email('Email inválido'),
  password: z.string().min(6, 'La contraseña debe tener al menos 6 caracteres'),
})
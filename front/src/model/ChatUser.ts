import z from "zod";
import type { BaseModel } from "./BaseModel"

export interface ChatUser extends BaseModel{
  email: string;
  phoneNumber: string;
  blockedAt?: Date;
}

export interface CreateChatUser {
    email: string;
  phoneNumber: string;
}

export const chatUserSchema = z.object({
  email: z.string().email('Email inválido'),
  phoneNumber: z.string().regex(/^\+549\d{10}$/, 'Teléfono inválido, el formato esperado es: +5491101234567'),
})
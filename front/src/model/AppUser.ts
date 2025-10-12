import type { BaseModel } from "./BaseModel"

export interface AppUser extends BaseModel{
  email: string;
  blockedAt?: Date;
}

// DTO para crear/actualizar usuarios (requiere password)
export interface AppUserCreateDTO {
  email: string;
  password: string;
}

// DTO mínimo que devuelve el backend
export interface AppUserMinimalDTO {
  externalId: string;
  email: string;
  blockedAt?: Date;
}

// DTO para crear/actualizar usuarios (requiere password)
export interface AppUserCreateDTO {
  email: string;
  password: string;
}

// DTO mínimo que devuelve el backend
export interface AppUserMinimalDTO {
  externalId: string;
  email: string;
  blockedAt?: Date;
}
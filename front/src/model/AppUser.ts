import type { BaseModel } from "./BaseModel"

export interface AppUser extends BaseModel{
  email: string;
  blockedAt?: Date;
}
import type { BaseModel } from "./BaseModel"

export interface ChatUser extends BaseModel{
  email: string;
  phoneNumber: String;
  blockedAt?: Date;
}
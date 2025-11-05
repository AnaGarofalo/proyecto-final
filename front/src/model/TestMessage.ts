export enum MessageOrigin {
  USER = 'USER',
  BOT = 'BOT'
}

export interface Message {
  content: string;
  origin: MessageOrigin;
}

export interface ChatRequest {
  content: string;
}
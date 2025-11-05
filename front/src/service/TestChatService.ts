import apiClient from './axios';
import type { Message, ChatRequest } from '../model/TestMessage';

class ChatService {
  private static readonly BASE_URL = '/conversation-flow';

  // GET /conversation-flow/conversation - Obtener historial
  static async getConversationHistory(): Promise<{ data: Message[] }> {
    const response = await apiClient.get<Message[]>(`${this.BASE_URL}/conversation`);
    return { data: response.data };
  }

  // POST /conversation-flow/chat - Enviar mensaje
  static async sendMessage(message: ChatRequest): Promise<{ data: Message }> {
    const response = await apiClient.post<Message>(`${this.BASE_URL}/chat`, message);
    return { data: response.data };
  }
}

export default ChatService;
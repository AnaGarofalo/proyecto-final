import httpClient from './axios'

export default class SystemPromptService {
  static basePath = '/system-prompt'

  static get() {
    return httpClient.get<{ prompt: string; ticketEmail: string }>(this.basePath)
  }

  static patch(data: { prompt?: string; ticketEmail?: string }) {
    return httpClient.patch<{ prompt: string; ticketEmail: string }>(this.basePath, data)
  }
}

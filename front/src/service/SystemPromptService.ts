import httpClient from './axios'
import type { SystemPrompt } from '../model/SystemPrompt'

export default class SystemPromptService {
  static basePath = '/system-prompt'

  static get() {
    return httpClient.get<SystemPrompt>(this.basePath)
  }

  static patch(data: SystemPrompt) {
    return httpClient.patch<SystemPrompt>(this.basePath, data)
  }
}

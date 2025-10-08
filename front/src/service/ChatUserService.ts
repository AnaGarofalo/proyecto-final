import type { ChatUser } from '../model/ChatUser';
import httpClient from './axios'

export default class ChatUserService {
    static basePath = "/chat-user";

    static getAll() {
        return httpClient.get<ChatUser[]>(this.basePath)
    }

    static markAsBlocked(externalId: string) {
        return httpClient.put<ChatUser[]>(this.basePath + "/block/" + externalId)
    }

    static unblock(externalId: string) {
        return httpClient.put<ChatUser[]>(this.basePath + "/unblock/" + externalId)
    }

    static markAsDeleted(externalId: string) {
        return httpClient.delete<ChatUser[]>(this.basePath + "/" + externalId)
    }
}
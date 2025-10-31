import type { AppUserLogin, LoginResponse } from '../model/AppUserLogin';
import type { AppUserMinimalDTO } from '../model/AppUser';
import httpClient from './axios'

export default class AppUserService {
    static basePath = "/app-user";

    static login(data: AppUserLogin) {
        // Ahora el método está tipado para devolver LoginResponse (con el token)
        return httpClient.post<LoginResponse>(this.basePath + "/login", data)
    }

    static getAll() {
        return httpClient.get<AppUserMinimalDTO[]>(this.basePath)
    }

    static create(data: { email: string; password: string }) {
        return httpClient.post<AppUserMinimalDTO>(this.basePath, data)
    }

    static update(externalId: string, data: { email: string; password: string }) {
        return httpClient.put<AppUserMinimalDTO>(`${this.basePath}/${externalId}`, data)
    }

    static delete(externalId: string) {
        return httpClient.delete(`${this.basePath}/${externalId}`)
    }

    static markAsBlocked(externalId: string) {
        return httpClient.put<AppUserMinimalDTO>(this.basePath + "/block/" + externalId)
    }

    static unblock(externalId: string) {
        return httpClient.put<AppUserMinimalDTO>(this.basePath + "/unblock/" + externalId)
    }
}
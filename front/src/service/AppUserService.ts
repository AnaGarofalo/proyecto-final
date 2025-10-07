import type { AppUserLogin, LoginResponse } from '../model/AppUserLogin';
import httpClient from './axios'

export default class AppUserService {
    static basePath = "/app-user";

    static login(data: AppUserLogin) {
        // Ahora el método está tipado para devolver LoginResponse (con el token)
        return httpClient.post<LoginResponse>(this.basePath + "/login", data)
    }
}
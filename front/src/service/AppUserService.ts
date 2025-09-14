import { type AppUser } from '../model/AppUser'
import type { AppUserLogin } from '../model/AppUserLogin';
import httpClient from './axios'

export default class AppUserService {
    static basePath = "/app-user";

    static login(data: AppUserLogin) {
        return httpClient.post<AppUser>(this.basePath + "/login", data)
    }
}
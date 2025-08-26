import { environment } from "../../environments/environment";

const APP_URL = environment.app_url;

export function getJWTtoken() {
    const data = getCurrentUserData();
    return data.accessToken;
}

export function getCurrentUserData() {
    const data = localStorage.getItem('USER');
    if (data) {
        return JSON.parse(data);
    } else {
        logout();
    }
}

export function getHomePageRedirection(user: any) {
    if (user.role == "PATIENT") {
        return 'patient';
    } else {
        return 'doctor';
    }
}

export function logout() {
    localStorage.clear();
    window.location.href = APP_URL;
}
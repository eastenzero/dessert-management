import axios from "../utils/request"

export function register(user) {
    return axios.post("/auth/register", user)
}

export function login(user) {
    return axios.post("/auth/login", user)
}

export function logout() {
    return axios.post("/auth/logout")
}

import axios from "axios";
const API=axios.create({
    // baseURL:'http://localhost:8080',
    baseURL:process.env.BASE_API,
    timeout:2000
})

API.interceptors.request.use(config=>{
    const token = localStorage.getItem('token')
    if (token) {
        config.headers = config.headers || {}
        config.headers['Authorization'] = 'Bearer ' + token
    }
    return config
})

export default API
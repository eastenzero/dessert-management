import axios from "axios";
const API=axios.create({
    // baseURL:'http://localhost:8080',
    baseURL:process.env.BASE_API,
    timeout:2000
})
export default API
// import axios from "axios";
import axios from "../utils/request"
export function add(category){
    //return axios.post("http://localhost:8080/category",category.value)
    return axios.post("/category",category.value)
}

export function list(queryParams){ 
    return axios.get("/category",{params:queryParams.value})
}

export function edit(category){ 
    return axios.put("/category",category.value)
}

export function del(ids2del){ 
    return axios.delete("/category/"+ids2del)
}
 


 
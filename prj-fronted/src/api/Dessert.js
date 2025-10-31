import axios from "../utils/request"

export function list(queryParams){ 
    return axios.get("/dessert",{params:queryParams.value})
}

export function add(dessert){ 
    return axios.post("/dessert",dessert.value)
}

export function edit(dessert){ 
    return axios.put("/dessert",dessert.value)
}

export function del(ids2del){ 
    return axios.delete("/dessert/"+ids2del)
}
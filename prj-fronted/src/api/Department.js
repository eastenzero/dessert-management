import axios from "../utils/request"

export function add(department) {
    return axios.post("/department", department.value)
}

export function list(queryParams) {
    return axios.get("/department", { params: queryParams.value })
}

export function edit(department) {
    return axios.put("/department", department.value)
}

export function del(ids2del) {
    return axios.delete("/department/" + ids2del)
}

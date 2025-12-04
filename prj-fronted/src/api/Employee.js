import axios from "../utils/request"

export function add(employee) {
    return axios.post("/employee", employee.value)
}

export function list(queryParams) {
    return axios.get("/employee", { params: queryParams.value })
}

export function edit(employee) {
    return axios.put("/employee", employee.value)
}

export function del(ids2del) {
    return axios.delete("/employee/" + ids2del)
}

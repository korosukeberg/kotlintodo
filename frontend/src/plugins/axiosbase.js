import axiosBase from "axios"

const axios = axiosBase.create({
  baseURL: "http://localhost:8081"
})

export default axios;

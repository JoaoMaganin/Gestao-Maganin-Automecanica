import axios from "axios";

export const axiosInstance = axios.create({
    baseURL: process.env.NEXT_PUBLIC_BACKEND_URL_API
})

export class BaseService {

    url: string;

    constructor(url: string) {
        this.url = url;
    }

    listarTodos() {
        console.debug("chamando url: " + this.url);
        return axiosInstance.get(this.url);
    }

    procurarPorId(id: number) {
        return axiosInstance.get(this.url + "/" + id);
    }

    criar(obj: any) {
        return axiosInstance.post(this.url, obj);
    }

    atualizar(obj: any) {
        return axiosInstance.put(this.url, obj);
    }

    deletar(id: number) {
        return axiosInstance.delete(this.url + "/" + id);
    }
}
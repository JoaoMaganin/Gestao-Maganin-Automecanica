import axios from "axios";

export const axiosInstance = axios.create({
    baseURL: process.env.NEXT_PUBLIC_BACKEND_URL_API
})

export class BackupService {
    realizarBackup() {
        return axiosInstance.post("api/backup/upload");
    }
}
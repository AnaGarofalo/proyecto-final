import axios from 'axios'

const httpClient = axios.create({
  baseURL: 'http://localhost:8080', 
//   withCredentials: true, // si usás cookies o sesiones
})

// Interceptor de errores para devolver errores más sencillos
httpClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const message = error?.response?.data?.message || "Error desconocido";
    const status = error?.response?.status;

    // Podés lanzar un objeto más limpio:
    return Promise.reject({ message, status });
  }
);

export default httpClient;
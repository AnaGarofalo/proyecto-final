import axios from 'axios'

const httpClient = axios.create({
  baseURL: 'http://localhost:8080', 
//   withCredentials: true, // si usás cookies o sesiones
})

// Interceptor de request para agregar automáticamente el token
httpClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor de errores para devolver errores más sencillos
httpClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const message = error?.response?.data?.message || "Error desconocido";
    const status = error?.response?.status;

    // Si es error 401 (No autorizado), redirigir al login
    if (status === 401) {
      localStorage.removeItem('access_token');
      window.location.href = '/login'; // O usar navigate si tienes acceso al router
    }

    // Podés lanzar un objeto más limpio:
    return Promise.reject({ message, status });
  }
);

export default httpClient;
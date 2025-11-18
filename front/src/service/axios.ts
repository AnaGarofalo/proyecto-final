import axios from 'axios'

// Función para verificar si el token está expirado localmente
export const isTokenExpired = (token: string): boolean => {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    const currentTime = Date.now() / 1000;
    return payload.exp < currentTime;
  } catch {
    return true; 
  }
};

// Función para manejar logout automático
const handleAutoLogout = () => {
  localStorage.removeItem('access_token');
  
  // Disparar evento personalizado para que el App.tsx maneje la redirección
  window.dispatchEvent(new CustomEvent('autoLogout'));
  
  // Como fallback, si no estamos en login, redirigir directamente
  if (!window.location.pathname.includes('/login') && window.location.pathname !== '/') {
    window.location.href = '/';
  }
};

const httpClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
})

// Interceptor de request para agregar automáticamente el token
httpClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('access_token');
    if (token) {
      // Verificar si el token está expirado antes de enviarlo
      if (isTokenExpired(token)) {
        console.warn('Token expirado detectado localmente - Cerrando sesión');
        handleAutoLogout();
        return Promise.reject(new Error('Token expirado'));
      }
      
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

    // Si es error 401 (No autorizado) o 403 (Forbidden / Token expirado)
    if (status === 401 || status === 403) {
      console.warn('Token expirado o inválido (status: ' + status + ') - Cerrando sesión automáticamente');
      handleAutoLogout();
      
      // Mostrar mensaje más específico
      const tokenExpiredMessage = error?.response?.data?.error === 'invalid credentials' 
        ? message 
        : 'Tu sesión ha expirado. Por favor, inicia sesión nuevamente.';
        
      return Promise.reject({ message: tokenExpiredMessage, status, isTokenExpired: true });
    }

    // Podés lanzar un objeto más limpio:
    return Promise.reject({ message, status });
  }
);

export default httpClient;
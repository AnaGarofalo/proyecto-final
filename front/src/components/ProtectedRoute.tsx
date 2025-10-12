import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { NavigationRoute } from '../utils/NavigationUtils';
import { isTokenExpired } from '../service/axios';

interface ProtectedRouteProps {
  children: React.ReactNode;
}

const ProtectedRoute = ({ children }: ProtectedRouteProps) => {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('access_token');
    
    // Verificación básica: si no hay token
    if (!token) {
      console.warn('No hay token de acceso - Redirigiendo al login');
      navigate(NavigationRoute.LOGIN);
      return;
    }
    
    // Verificación de expiración: si el token está expirado
    if (isTokenExpired(token)) {
      console.warn('Token expirado - Eliminando token y redirigiendo al login');
      localStorage.removeItem('access_token');
      navigate(NavigationRoute.LOGIN);
      return;
    }
    
    // Si llegamos aquí, el usuario está autenticado correctamente
    console.log('Usuario autenticado - Acceso permitido');
  }, [navigate]);

  // Renderizar el contenido protegido si está autenticado
  return <>{children}</>;
};

export default ProtectedRoute;
/**
 * Extrae el email del usuario desde el token JWT almacenado en localStorage
 * @returns string | null - El email del usuario o null si no hay token o hay error
 */
export const getUserEmailFromToken = (): string | null => {
  try {
    const token = localStorage.getItem('access_token');
    if (!token) return null;
    
    const payload = JSON.parse(atob(token.split('.')[1]));
    return payload.sub; // 'sub' contiene el email en el JWT
  } catch (error) {
    console.error('Error al decodificar el token:', error);
    return null;
  }
};
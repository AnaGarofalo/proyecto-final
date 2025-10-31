import { z } from "zod";

// Categorías de caracteres
const uppercase = /\p{Lu}/u; // Mayúsculas unicode
const lowercase = /\p{Ll}/u; // Minúsculas unicode
const digit = /\d/;
const special = /[~!@#$%^&*_\-+=\\(){}[\]:;"'<>,.?/`]/;
const otherUnicodeAlpha = /\p{L}/u; // cualquier letra unicode (incluye asiáticos)
// Nota: después verificamos que no sea capturada ya como upper/lower


// Función que cuenta categorías
function countCategories(pw: string): number {
  let count = 0;

  if (uppercase.test(pw)) count++;
  if (lowercase.test(pw)) count++;
  if (digit.test(pw)) count++;
  if (special.test(pw)) count++;

  // “Unicode alfabético no upper/lower” (ej: caracteres asiáticos)
  if (otherUnicodeAlpha.test(pw) && !uppercase.test(pw) && !lowercase.test(pw)) count++;

  return count;
}

export const createUserSchema = z.object({
  email: z.string().email("Email inválido"),
  
  password:  z.string().
  min(10, "La contraseña debe tener al menos 10 caracteres").refine(
    (pw) => countCategories(pw) >= 3,
    "La contraseña debe incluir caracteres de al menos 3 de estas categorías: mayúsculas, minúsculas, dígitos, caracteres especiales, caracteres unicode."
  ),

})
import { createTheme } from '@mui/material/styles';
import { Colors } from './Colors';

declare module '@mui/material/Button' {
  interface ButtonPropsVariantOverrides {
    filled: true;
    outline: true;
    logout: true;
  }
}

const primary = Colors.PRIMARY_DARK_BLUE; // '#176B8C'
const primary200 = Colors.SECONDARY_LIGHT_BLUE; // '#7CA1BD'
const gray100 = Colors.TERTIARY_GRAY; // '#F0F0F0'
const gray300 = Colors.QUINARY_LIGHT_GRAY; // '#CFCFCF'
const gray600 = Colors.QUARTERNARY_DARK_GRAY; // '#5A5D5D'  
const red = Colors.SENARY_RED; // '#FF0004'

export const appTheme = createTheme({
  palette: {
    primary: { main: primary, light: primary200, contrastText: Colors.SEPTENARY_WHITE },
    error: { main: red },
    text: { primary: '#000', secondary: gray600 },
    grey: { 100: gray100, 300: gray300, 600: gray600 },
    background: { default: Colors.SEPTENARY_WHITE}
  },
  shape: { borderRadius: 10 },
  // Tipografías: deje valores por defecto
  components: {
    MuiButton: {
      styleOverrides: {
        root: { borderRadius: 10, paddingInline: 20, height: 44, textTransform: 'none', fontWeight: 600 }
      },
      variants: [
        { // Lleno (primario)
          props: { variant: 'filled' as any },
          style: {
            backgroundColor: primary,
            color: Colors.SEPTENARY_WHITE,
            '&:hover': { backgroundColor: Colors.HOVER_BLUE },
            boxShadow: 'none'
          }
        },
        { // Outline (blanco con borde azul)
          props: { variant: 'outline' as any },
          style: {
            backgroundColor: Colors.SEPTENARY_WHITE,
            color: primary,
            border: `2px solid ${primary}`,
            '&:hover': { backgroundColor: Colors.HOVER_WHITE}
          }
        },
        { // Logout (borde/texto gris)
          props: { variant: 'logout' as any },
          style: {
            backgroundColor: Colors.SEPTENARY_WHITE,
            color: gray600,
            border: `2px solid ${gray300}`,
            '&:hover': { backgroundColor: Colors.HOVER_WHITE_TWO}
          }
        }
      ]
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 10,
            background: Colors.SEPTENARY_WHITE,
          }
        }
      }
    }
  }
});

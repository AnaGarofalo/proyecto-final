import { createTheme } from '@mui/material/styles';

declare module '@mui/material/Button' {
  interface ButtonPropsVariantOverrides {
    filled: true;
    outline: true;
    logout: true;
  }
}

const primary = '#176B8C';
const primary200 = '#7CA1BD';
const gray100 = '#F0F0F0';
const gray300 = '#CFCFCF';
const gray600 = '#5A5D5D';
const red = '#FF0004';

export const appTheme = createTheme({
  palette: {
    primary: { main: primary, light: primary200, contrastText: '#fff' },
    error: { main: red },
    text: { primary: '#000', secondary: gray600 },
    grey: { 100: gray100, 300: gray300, 600: gray600 },
    background: { default: '#fff' }
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
            color: '#fff',
            '&:hover': { backgroundColor: '#125871' },
            boxShadow: 'none'
          }
        },
        { // Outline (blanco con borde azul)
          props: { variant: 'outline' as any },
          style: {
            backgroundColor: '#fff',
            color: primary,
            border: `2px solid ${primary}`,
            '&:hover': { backgroundColor: '#f4f8fb' }
          }
        },
        { // Logout (borde/texto gris)
          props: { variant: 'logout' as any },
          style: {
            backgroundColor: '#fff',
            color: gray600,
            border: `2px solid ${gray300}`,
            '&:hover': { backgroundColor: '#fafafa' }
          }
        }
      ]
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 10,
            background: '#fff'
          }
        }
      }
    }
  }
});

import { IconButton } from "@mui/material";
import { closeSnackbar, SnackbarProvider } from "notistack";
import type { PropsWithChildren } from "react";
import CloseIcon from '@mui/icons-material/Close';

export const ToastProvider = ({ children }: PropsWithChildren) => {
  return (
    <SnackbarProvider
      maxSnack={3}
      autoHideDuration={3000}
      anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
      hideIconVariant
      preventDuplicate
      action={(snackbarId) => (
        <IconButton
          aria-label="close"
          color="inherit"
          size="small"
          onClick={() => closeSnackbar(snackbarId)}
        >
          <CloseIcon fontSize="small" />
        </IconButton>
      )}
    >
      {children}
    </SnackbarProvider>
  );
};
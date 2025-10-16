import { type SnackbarKey, enqueueSnackbar } from "notistack";

export class ToastUtil {
  static success(message: string): SnackbarKey {
    return enqueueSnackbar(message, { variant: "success" });
  }

  static error(message: string): SnackbarKey {
    return enqueueSnackbar(message, { variant: "error" });
  }

  static warning(message: string): SnackbarKey {
    return enqueueSnackbar(message, { variant: "warning" });
  }

  static info(message: string): SnackbarKey {
    return enqueueSnackbar(message, { variant: "info" });
  }

  static tokenExpired(): SnackbarKey {
    return enqueueSnackbar('Token expirado', { variant: "warning" });
  }
}
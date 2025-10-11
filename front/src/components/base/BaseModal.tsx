import React from "react";
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  IconButton,
  Typography,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import BaseButton from "./BaseButton";
import { Colors } from "../../utils/Colors";

type BaseModalProps = {
  open: boolean;
  title?: string;
  onClose: () => void;
  onConfirm?: () => void;
  onCancel?: () => void;
  confirmText?: string;
  cancelText?: string;
  disableConfirm?: boolean;
  loading?: boolean;
  children: React.ReactNode;
};

const BaseModal: React.FC<BaseModalProps> = ({
  open,
  title,
  onClose,
  onConfirm,
  onCancel,
  confirmText = "Confirmar",
  cancelText = "Cancelar",
  disableConfirm,
  loading,
  children,
}) => {
  return (
    <Dialog
      open={open}
      onClose={onClose}
      fullWidth
      maxWidth="sm"
      PaperProps={{
        sx: { borderRadius: 2, p: 1 },
      }}
    >
      {title && (
        <DialogTitle sx={{ pr: 6 }}>
          <Typography
            variant="h6"
            fontWeight={400}
            color={Colors.QUARTERNARY_DARK_GRAY}
          >
            {title}
          </Typography>
          <IconButton
            onClick={onClose}
            aria-label="cerrar"
            sx={{ position: "absolute", right: 8, top: 8 }}
          >
            <CloseIcon />
          </IconButton>
        </DialogTitle>
      )}

      <DialogContent dividers sx={{ px: 3, py: 2 }}>
        {children}
      </DialogContent>

      <DialogActions sx={{ p: 2.5, gap: 1.5 }}>
        <BaseButton
          variantType="outline"
          color="secondary"
          onClick={onCancel ?? onClose}
        >
          {cancelText}
        </BaseButton>

        <BaseButton
          variantType="filled"
          onClick={onConfirm}
          disabled={disableConfirm || loading}
        >
          {confirmText}
        </BaseButton>
      </DialogActions>
    </Dialog>
  );
};

export default BaseModal;

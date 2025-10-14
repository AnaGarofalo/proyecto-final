import React from "react";
import { Box, Typography, IconButton } from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import BaseModal from "./base/BaseModal";
import { Colors } from "../utils/Colors";

interface DocumentUploadModalProps {
  open: boolean;
  selectedFiles: File[];
  setSelectedFiles: React.Dispatch<React.SetStateAction<File[]>>;
  onClose: () => void;
  onCancel: () => void;
  onConfirm: () => void;
  disabled?: boolean;
}

const DocumentUploadModal: React.FC<DocumentUploadModalProps> = ({
  open,
  selectedFiles,
  setSelectedFiles,
  onClose,
  onCancel,
  onConfirm,
  disabled = false,
}) => {
  return (
    <BaseModal
      open={open}
      title="Cargar documentos"
      onClose={onClose}
      onCancel={onCancel}
      onConfirm={onConfirm}
      confirmText={disabled ? "Cargando..." : "Confirmar"}
      cancelText="Cancelar"
      disableConfirm={disabled}
    >
      {/* Área drag & drop */}
      <Box
        sx={{
          border: `1px solid ${Colors.QUINARY_LIGHT_GRAY}`,
          borderRadius: "8px",
          backgroundColor: Colors.SEPTENARY_WHITE,
          height: 120,
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          cursor: "pointer",
          textAlign: "center",
          transition: "background-color 0.2s ease",
          "&:hover": { backgroundColor: Colors.TERTIARY_GRAY },
        }}
        onDragOver={(e) => {
          e.preventDefault();
          e.currentTarget.style.backgroundColor = Colors.TERTIARY_GRAY;
        }}
        onDragLeave={(e) => {
          e.preventDefault();
          e.currentTarget.style.backgroundColor = Colors.SEPTENARY_WHITE;
        }}
        onDrop={(e) => {
          e.preventDefault();
          e.currentTarget.style.backgroundColor = Colors.SEPTENARY_WHITE;
          const files = Array.from(e.dataTransfer.files);
          if (!disabled && files.length > 0)
            setSelectedFiles((prev) => [...prev, ...files]);
        }}
        onClick={() =>
          !disabled && document.getElementById("fileInput")?.click()
        }
      >
        <Typography
          variant="body1"
          sx={{
            color: Colors.QUARTERNARY_DARK_GRAY,
            fontSize: 16,
          }}
        >
          {disabled
            ? "Subiendo documentos..."
            : "Agregue o suelte sus documentos aquí"}
        </Typography>
      </Box>

      {/* Input oculto */}
      <input
        id="fileInput"
        type="file"
        multiple
        style={{ display: "none" }}
        onChange={(e) =>
          setSelectedFiles((prev) =>
            e.target.files ? [...prev, ...Array.from(e.target.files)] : prev
          )
        }
        disabled={disabled}
      />

      {/* Lista de archivos seleccionados */}
      {selectedFiles.length > 0 && (
        <Box
          sx={{
            mt: 2,
            display: "flex",
            flexDirection: "column",
            gap: 1,
            maxHeight: 150,
            overflowY: "auto",
            opacity: disabled ? 0.6 : 1,
            pointerEvents: disabled ? "none" : "auto",
          }}
        >
          {selectedFiles.map((file, index) => (
            <Box
              key={index}
              sx={{
                display: "flex",
                alignItems: "center",
                justifyContent: "space-between",
                border: `1px solid ${Colors.TERTIARY_GRAY}`,
                borderRadius: "8px",
                px: 2,
                py: 1,
                backgroundColor: Colors.SEPTENARY_WHITE,
              }}
            >
              <Typography
                variant="body2"
                color="text.secondary"
                sx={{ wordBreak: "break-all" }}
              >
                {file.name}
              </Typography>
              <IconButton
                size="small"
                onClick={() =>
                  setSelectedFiles((prev) => prev.filter((_, i) => i !== index))
                }
                sx={{
                  color: Colors.SENARY_RED,
                  "&:hover": { backgroundColor: Colors.TERTIARY_GRAY },
                }}
              >
                <CloseIcon fontSize="small" />
              </IconButton>
            </Box>
          ))}
        </Box>
      )}
    </BaseModal>
  );
};

export default DocumentUploadModal;

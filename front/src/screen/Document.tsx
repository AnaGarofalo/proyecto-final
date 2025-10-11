import React, { useState, useEffect } from "react";
import { Box, IconButton, Typography } from "@mui/material";
import DeleteIcon from "@mui/icons-material/DeleteOutline";
import CloseIcon from "@mui/icons-material/Close";
import BaseButton from "../components/base/BaseButton";
import BaseModal from "../components/base/BaseModal";
import { Colors } from "../utils/Colors";
import DocumentService from "../service/DocumentService";
import { BaseTable, type Column } from "../components/base/BaseTable";
import type { Document as DocModel } from "../model/Document";
import { ToastUtil } from "../utils/ToastUtils";

const Documents: React.FC = () => {
  const [openModal, setOpenModal] = useState(false);
  const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
  const [documents, setDocuments] = useState<DocModel[]>([]);

  // Cargar documentos al montar el componente
  useEffect(() => {
    (async () => {
      try {
        const data = await DocumentService.getDocuments();
        setDocuments(data);
      } catch (error) {
        console.error(error);
        ToastUtil.error("Error al cargar los documentos");
      }
    })();
  }, []);

  // Subida de documentos
  const handleUpload = async () => {
    if (selectedFiles.length === 0) {
      ToastUtil.warning("Seleccioná al menos un archivo antes de subirlo");
      return;
    }

    try {
      const uploadedDocs: DocModel[] = [];

      for (const file of selectedFiles) {
        const uploaded = await DocumentService.uploadDocument(file);
        uploadedDocs.push(uploaded);
      }

      setDocuments((prev) => [...prev, ...uploadedDocs]);
      setOpenModal(false);
      setSelectedFiles([]);
      ToastUtil.success("Documentos cargados con éxito");
    } catch (error) {
      console.error(error);
      ToastUtil.error("Ocurrió un error al subir los documentos");
    }
  };

  // Columnas de la tabla
  const columns: Column<DocModel>[] = [
    { field: "fileName", label: "Archivo", flex: 1.5 },
    {
      field: "createdAt",
      label: "Fecha Carga",
      flex: 1,
      render: (value) =>
        value ? new Date(value).toLocaleDateString("es-AR") : "-",
    },
    {
      field: "uploadedBy",
      label: "Usuario",
      flex: 1,
      render: (value) => value || "-",
    },
    {
      label: "Borrar",
      align: "center",
      width: 100,
      render: () => (
        <IconButton sx={{ color: Colors.QUARTERNARY_DARK_GRAY }}>
          <DeleteIcon />
        </IconButton>
      ),
    },
  ];

  return (
    <Box
      sx={{
        flexGrow: 1,
        px: 3,
        backgroundColor: Colors.SEPTENARY_WHITE,
        height: "100vh",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        overflow: "hidden",
      }}
    >
      {/* Barra de búsqueda */}
      <BaseTable<DocModel>
        columns={columns}
        rows={documents}
        searchFields={["fileName"]}
        searchPlaceholder="Buscar por nombre de archivo"
        pageSize={10}
        sx={{
          width: "100%",
          maxWidth: "1200px",
          mx: "auto",
          borderRadius: "8px",
          overflow: "hidden",
        }}
      />

      {/* Botón “Cargar documento” */}
      <Box
        sx={{
          position: "fixed",
          bottom: 32,
          right: 64,
          zIndex: 2,
        }}
      >
        <BaseButton
          variantType="filled"
          onClick={() => setOpenModal(true)}
          sx={{
            px: 4,
            py: 1.5,
            fontWeight: 600,
            backgroundColor: Colors.PRIMARY_DARK_BLUE,
            color: Colors.SEPTENARY_WHITE,
            "&:hover": { backgroundColor: Colors.HOVER_BLUE },
          }}
        >
          Cargar documento
        </BaseButton>
      </Box>

      {/* Modal de carga múltiple */}
      <BaseModal
        open={openModal}
        title="Cargar documentos"
        onClose={() => {
          setOpenModal(false);
          setSelectedFiles([]);
        }}
        onCancel={() => {
          setOpenModal(false);
          setSelectedFiles([]);
        }}
        onConfirm={handleUpload}
        confirmText="Confirmar"
        cancelText="Cancelar"
      >
        <Box
          sx={{
            border: `1px solid ${Colors.QUINARY_LIGHT_GRAY}`,
            borderRadius: "8px",
            backgroundColor: `${Colors.SEPTENARY_WHITE}`,
            height: 120,
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            cursor: "pointer",
            textAlign: "center",
            transition: "background-color 0.2s ease",
            "&:hover": { backgroundColor: `${Colors.TERTIARY_GRAY}` },
          }}
          onDragOver={(e) => {
            e.preventDefault();
            e.currentTarget.style.backgroundColor = `${Colors.TERTIARY_GRAY}`;
          }}
          onDragLeave={(e) => {
            e.preventDefault();
            e.currentTarget.style.backgroundColor = `${Colors.SEPTENARY_WHITE}`;
          }}
          onDrop={(e) => {
            e.preventDefault();
            e.currentTarget.style.backgroundColor = `${Colors.SEPTENARY_WHITE}`;
            const files = Array.from(e.dataTransfer.files);
            if (files.length > 0)
              setSelectedFiles((prev) => [...prev, ...files]);
          }}
          onClick={() => document.getElementById("fileInput")?.click()}
        >
          <Typography
            variant="body1"
            sx={{
              color: Colors.QUARTERNARY_DARK_GRAY,
              fontSize: 16,
            }}
          >
            Agregue o suelte sus documentos aquí
          </Typography>
        </Box>

        {/* Input oculto de carga de archivos */}
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
                  backgroundColor: `${Colors.SEPTENARY_WHITE}`,
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
                    setSelectedFiles((prev) =>
                      prev.filter((_, i) => i !== index)
                    )
                  }
                  sx={{
                    color: Colors.SENARY_RED,
                    "&:hover": { backgroundColor: `${Colors.TERTIARY_GRAY}` },
                  }}
                >
                  <CloseIcon fontSize="small" />
                </IconButton>
              </Box>
            ))}
          </Box>
        )}
      </BaseModal>
    </Box>
  );
};

export default Documents;

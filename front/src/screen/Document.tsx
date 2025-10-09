import React, { useState, useEffect } from "react";
import { Box, IconButton, Typography } from "@mui/material";
import DeleteIcon from "@mui/icons-material/DeleteOutline";
import BaseButton from "../components/base/BaseButton";
import BaseModal from "../components/base/BaseModal";
import { Colors } from "../utils/Colors";
import { getDocuments, uploadDocument } from "../service/DocumentService";
import { BaseTable, type Column } from "../components/base/BaseTable";

interface DocumentItem {
  id: number;
  fileName: string;
  createdAt?: string;
  usuario?: string;
}

const Documents: React.FC = () => {
  const [openModal, setOpenModal] = useState(false);
  const [selectedFile, setSelectedFile] = useState<File | null>(null);
  const [documents, setDocuments] = useState<DocumentItem[]>([]);

  //Cargar documentos desde el backend
  useEffect(() => {
    const fetchDocs = async () => {
      try {
        const data = await getDocuments();

        // 🧹 Sanitizar datos por seguridad
        const safeData = data.map((doc: any) => ({
          id: doc.id,
          fileName: String(doc.fileName ?? ""),
          createdAt: doc.createdAt ?? "",
          usuario: String(doc.usuario ?? ""),
        }));

        setDocuments(safeData);
      } catch (error) {
        console.error("Error al obtener documentos:", error);
      }
    };
    fetchDocs();
  }, []);

  //Subir archivo nuevo
  const handleUpload = async () => {
    if (!selectedFile) {
      alert("Por favor seleccioná un archivo antes de subirlo.");
      return;
    }

    try {
      const uploaded = await uploadDocument(selectedFile);
      setDocuments((prev) => [
        ...prev,
        {
          id: uploaded.id,
          fileName: uploaded.fileName,
          createdAt: uploaded.createdAt,
          usuario: uploaded.usuario ?? "",
        },
      ]);
      setOpenModal(false);
      setSelectedFile(null);
    } catch (err) {
      console.error("Error al subir documento:", err);
      alert("Ocurrió un error al subir el archivo.");
    }
  };

  // 🔹 Definir columnas de la tabla
  const columns: Column<DocumentItem>[] = [
    { field: "fileName", label: "Archivo", flex: 1.5 },
    {
      field: "createdAt",
      label: "Fecha de carga",
      flex: 1,
      render: (value) =>
        value ? new Date(value).toLocaleDateString("es-AR") : "-",
    },
    {
      field: "usuario",
      label: "Usuario",
      flex: 1,
      render: (value) => value || "-",
    },
    {
      label: "Borrar",
      align: "center",
      width: 100,
      render: (_, row) => (
        <IconButton
          sx={{ color: Colors.QUARTERNARY_DARK_GRAY }}
          onClick={() => console.log("🗑️ eliminar", row.id)}
        >
          <DeleteIcon />
        </IconButton>
      ),
    },
  ];

  return (
    <Box
      sx={{
        flexGrow: 1,
        pt: 0,
        px: 3,
        backgroundColor: Colors.SEPTENARY_WHITE,
        minHeight: "calc(100vh - 120px)",
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
      }}
    >
      {/* Contenedor centrado de tabla */}
      <Box
        sx={{
          width: "100%",
          maxWidth: "1200px",
          mx: "auto",
          overflow: "hidden",
          borderRadius: "8px",
        }}
      >
        {/* Barra de búsqueda + encabezado fijos */}
        <Box sx={{ flexShrink: 0 }}>
          <BaseTable<DocumentItem>
            columns={columns}
            rows={documents}
            searchFields={["fileName", "usuario"]}
            searchPlaceholder="Buscar por nombre o usuario..."
            pageSize={10}
          />
        </Box>
      </Box>

      {/* Botón fijo para subir documento */}
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

      {/* Modal de carga */}
      <BaseModal
        open={openModal}
        title="Cargar documento"
        onClose={() => setOpenModal(false)}
        onConfirm={handleUpload}
        confirmText="Confirmar"
        cancelText="Cancelar"
      >
        <Box sx={{ display: "flex", flexDirection: "column", gap: 2 }}>
          <Typography variant="body1">
            Agregue o suelte sus documentos aquí.
          </Typography>
          <input
            type="file"
            onChange={(e) => setSelectedFile(e.target.files?.[0] || null)}
          />
          {selectedFile && (
            <Typography variant="body2" color="text.secondary">
              Archivo seleccionado: {selectedFile.name}
            </Typography>
          )}
        </Box>
      </BaseModal>
    </Box>
  );
};

export default Documents;

import React from "react";
import { Box, IconButton } from "@mui/material";
import DeleteIcon from "@mui/icons-material/DeleteOutline";
import { BaseTable, type Column } from "./base/BaseTable";
import { Colors } from "../utils/Colors";
import type { Document as DocModel } from "../model/Document";

interface DocumentTableProps {
  documents: DocModel[];
}

const DocumentTable: React.FC<DocumentTableProps> = ({ documents }) => {
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
      sortable: false, // Desactiva la ordenación para esta columna
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
        width: "100%",
        maxWidth: "1200px",
        mx: "auto",
        borderRadius: "8px",
        overflow: "hidden",
      }}
    >
      <BaseTable<DocModel>
        columns={columns}
        rows={documents}
        searchFields={["fileName"]}
        searchPlaceholder="Buscar por nombre de archivo"
        pageSize={5}
      />
    </Box>
  );
};

export default DocumentTable;

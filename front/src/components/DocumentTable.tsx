import React, { useState } from "react";
import { Box } from "@mui/material";
import { BaseTable, type Column } from "./base/BaseTable";
import type { Document as DocModel } from "../model/Document";
import DocumentRemovalModal from "./DocumentRemovalModal";
import DeleteIcon from '@mui/icons-material/Delete';
import BaseIconButton from './base/BaseIconButton';

interface DocumentTableProps {
  documents: DocModel[];
  afterDelete: (deleted: DocModel) => void;
}

const DocumentTable: React.FC<DocumentTableProps> = ({ documents, afterDelete }) => {
  const [documentToDelete, setDocumentToDelete] = useState<DocModel | null>(null);
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
  align: "left",  
  width: 100,
  sortable: false,
  render: (_value, row) => (
    <BaseIconButton 
      onClick={() => setDocumentToDelete(row)}
      icon={<DeleteIcon />}
    />
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
      {documentToDelete && (
        <DocumentRemovalModal
          open={true}
          selectedDocument={documentToDelete}
          closeModal={() => setDocumentToDelete(null)}
          afterDelete={afterDelete}
        />
      )}
    </Box>
  );
};

export default DocumentTable;

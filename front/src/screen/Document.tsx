import React, { useState, useEffect } from "react";
import { Box } from "@mui/material";
import BaseButton from "../components/base/BaseButton";
import { Colors } from "../utils/Colors";
import { ToastUtil } from "../utils/ToastUtils";
import DocumentService from "../service/DocumentService";
import type { Document as DocModel } from "../model/Document";
import DocumentTable from "../components/DocumentTable";
import DocumentUploadModal from "../components/DocumentUploadModal";

const Documents: React.FC = () => {
  const [openModal, setOpenModal] = useState(false);
  const [selectedFiles, setSelectedFiles] = useState<File[]>([]);
  const [documents, setDocuments] = useState<DocModel[]>([]);

  useEffect(() => {
    (async () => {
      try {
        const res = await DocumentService.getDocuments();
        setDocuments(res.data);
      } catch (error) {
        console.error(error);
        ToastUtil.error("Error al cargar los documentos");
      }
    })();
  }, []);

  const handleUpload = async () => {
    if (selectedFiles.length === 0) {
      ToastUtil.warning("Seleccioná al menos un archivo antes de subirlo");
      return;
    }

    try {
      const uploadedDocs: DocModel[] = [];
      for (const file of selectedFiles) {
        const res = await DocumentService.uploadDocument(file);
        uploadedDocs.push(res.data);
      }
      setDocuments((prev) => [...prev, ...uploadedDocs]);
      setOpenModal(false);
      setSelectedFiles([]);
      ToastUtil.success("Documentos cargados con éxito");
    } catch {
      ToastUtil.error("Ocurrió un error al subir los documentos");
    }
  };

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
      <DocumentTable documents={documents} />

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

      <DocumentUploadModal
        open={openModal}
        selectedFiles={selectedFiles}
        setSelectedFiles={setSelectedFiles}
        onClose={() => {
          setOpenModal(false);
          setSelectedFiles([]);
        }}
        onCancel={() => {
          setOpenModal(false);
          setSelectedFiles([]);
        }}
        onConfirm={handleUpload}
      />
    </Box>
  );
};

export default Documents;

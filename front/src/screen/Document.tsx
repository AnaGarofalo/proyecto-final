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
  const [isUploading, setIsUploading] =
    useState(
      false
    ); /* Esto bloquea el botón Confirmar cuando se esta subiendo un documento (si no permitia subir varias veces el mismo archivo 
  si se hacia click muy rapido*/

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

    setIsUploading(true); // Indica que se está subiendo un documento (bloquea el botón de Confirmar)

    try {
      const uploadedDocs = await DocumentService.uploadDocuments(selectedFiles);

      setDocuments((prev) => [...uploadedDocs.data, ...prev]);
      setOpenModal(false);
      setSelectedFiles([]);
      ToastUtil.success("Documentos cargados con éxito");
    } catch {
      ToastUtil.error("Ocurrió un error al subir los documentos");
    } finally {
      setIsUploading(false); // Resetea el estado de subida (desbloquea el botón de Confirmar)
    }
  };

  const afterDelete = (deleted: DocModel) => setDocuments(current => current.filter(doc => doc.externalId !== deleted.externalId));


  return (
    <>
      <Box
        sx={{
          flexGrow: 1,
          px: 3,
          backgroundColor: Colors.SEPTENARY_WHITE,
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          overflow: "hidden",
        }}
      >
        <DocumentTable documents={documents} afterDelete={afterDelete} />

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

        {/* Modal de carga de documentos */}
        <DocumentUploadModal
          open={openModal}
          selectedFiles={selectedFiles}
          setSelectedFiles={setSelectedFiles}
          onClose={() => {
            if (!isUploading) {
              setOpenModal(false);
              setSelectedFiles([]);
            }
          }}
          onCancel={() => {
            if (!isUploading) {
              setOpenModal(false);
              setSelectedFiles([]);
            }
          }}
          onConfirm={handleUpload}
          disabled={isUploading} // Bloquea el botón Confirmar mientras se está subiendo un documento
        />
      </Box>
    </>
  );
};

export default Documents;

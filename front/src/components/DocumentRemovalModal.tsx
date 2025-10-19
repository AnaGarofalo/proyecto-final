import React, { useState } from "react";
import BaseModal from "./base/BaseModal";
import type { Document as DocModel } from "../model/Document";
import { ToastUtil } from "../utils/ToastUtils";
import DocumentService from "../service/DocumentService";
import { Typography } from "@mui/material";

interface DocumentRemovalModalProps {
  open: boolean;
  selectedDocument: DocModel;
  closeModal: () => void;
  afterDelete: (document: DocModel) => void;
}

const DocumentRemovalModal: React.FC<DocumentRemovalModalProps> = ({
  open,
  selectedDocument,
  closeModal,
  afterDelete,
}) => {
  const [isDeleting, setIsDeleting] = useState(false);

  async function deleteDocument() {
    setIsDeleting(true);
    try {
      const response = await DocumentService.deleteDocument(
        selectedDocument.externalId
      );
      afterDelete(response.data);
      ToastUtil.success("Documento eliminado exitosamente");
      closeModal();
    } catch (error) {
      ToastUtil.error("No se pudo eliminar el archivo");
      console.error(error);
    } finally {
      setIsDeleting(false);
    }
  }

  return (
    <BaseModal
      open={open}
      title="Eliminar documento"
      onClose={closeModal}
      onConfirm={deleteDocument}
      confirmText={isDeleting ? "Eliminando..." : "Confirmar"}
      cancelText="Cancelar"
      disableConfirm={isDeleting}
    >
      <Typography>
        ¿Está seguro de que desea eliminar al documento{" "}
        {selectedDocument.fileName}?
      </Typography>
    </BaseModal>
  );
};

export default DocumentRemovalModal;

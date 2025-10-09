import axios from "axios";

const API_URL = "http://localhost:8080/documents";

export const getDocuments = async () => {
  const response = await axios.get(API_URL);
  return response.data;
};

// 🔹 Nuevo: subir archivo
export const uploadDocument = async (file: File) => {
  const formData = new FormData();
  formData.append("file", file);

  const response = await axios.post(`${API_URL}/upload`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

  return response.data;
};

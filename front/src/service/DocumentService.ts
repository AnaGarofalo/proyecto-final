import httpClient from "./axios";
import type { Document } from "../model/Document";

export default class DocumentService {
  static basePath = "/documents";

  static getDocuments() {
    return httpClient.get<Document[]>(this.basePath);
  }

  static uploadDocument(file: File) {
    const formData = new FormData();
    formData.append("file", file);

    return httpClient.post<Document>(`${this.basePath}/upload`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
  }
}

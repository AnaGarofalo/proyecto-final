import httpClient from "./axios";
import type { Document } from "../model/Document";

export default class DocumentService {
  static basePath = "/documents";

  static getDocuments() {
    return httpClient.get<Document[]>(this.basePath);
  }

  static uploadDocuments(files: File[]) {
    const formData = new FormData();
    files.forEach(file => {
      formData.append("files", file);
    });

    return httpClient.post<Document[]>(`${this.basePath}/upload`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
  }

    static deleteDocument(externalId: string) {
    return httpClient.delete<Document>(`${this.basePath}/${externalId}`);
  }
}

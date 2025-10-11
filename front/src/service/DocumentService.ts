import httpClient from "./axios";
import type { Document } from "../model/Document";

export default class DocumentService {
  static basePath = "/documents";

  static async getDocuments(): Promise<Document[]> {
    const res = await httpClient.get<Document[]>(this.basePath);
    return res.data;
  }

  static async uploadDocument(file: File): Promise<Document> {
    const formData = new FormData();
    formData.append("file", file);

    const res = await httpClient.post<Document>(
      `${this.basePath}/upload`,
      formData,
      { headers: { "Content-Type": "multipart/form-data" } }
    );
    return res.data;
  }
}

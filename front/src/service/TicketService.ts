import httpClient from "./axios";
import type { Ticket } from "../model/Ticket";

export default class TicketService {
  static basePath = "/tickets";

  static getTickets() {
    return httpClient.get<Ticket[]>(this.basePath);
  }
}

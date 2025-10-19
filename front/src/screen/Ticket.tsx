import React, { useState, useEffect } from "react";
import { Box } from "@mui/material";
import { Colors } from "../utils/Colors";
import { ToastUtil } from "../utils/ToastUtils";
import TicketService from "../service/TicketService";
import type { Ticket as TicketModel } from "../model/Ticket";
import TicketTable from "../components/TicketTable";

const Tickets: React.FC = () => {
  const [tickets, setTickets] = useState<TicketModel[]>([]);

  useEffect(() => {
    (async () => {
      try {
        const res = await TicketService.getTickets();
        setTickets(res.data);
      } catch (error) {
        console.error(error);
        ToastUtil.error("Error al cargar los tickets");
      }
    })();
  }, []);

  return (
    <>
      {/* Ocultar "Rows per page" */}
      <style>{`
        .MuiTablePagination-selectLabel,
        .MuiTablePagination-select,
        .MuiTablePagination-selectIcon {
          display: none !important;
        }
      `}</style>

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
        <TicketTable tickets={tickets} />

        <Box
          sx={{
            position: "fixed",
            bottom: 32,
            right: 64,
            zIndex: 2,
          }}
        ></Box>
      </Box>
    </>
  );
};

export default Tickets;

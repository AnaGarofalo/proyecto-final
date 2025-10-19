import React from "react";
import { BaseTable, type Column } from "./base/BaseTable";
import type { Ticket as TicketModel } from "../model/Ticket";
import Box from "@mui/material/Box";
import Tooltip from "@mui/material/Tooltip";

interface TicketTableProps {
  tickets: TicketModel[];
}

const TicketTable: React.FC<TicketTableProps> = ({ tickets }) => {
  const columns: Column<TicketModel>[] = [
    {
      field: "uploadedBy",
      label: "Usuario",
      flex: 1,
      render: (value) => value || "-",
    },
    {
      field: "createdAt",
      label: "Fecha Creación",
      flex: 1,
      render: (value) =>
        value ? new Date(value).toLocaleDateString("es-AR") : "-",
    },
    {
      field: "content",
      label: "Contenido",
      flex: 1.5,
      render: (value) => (
        <Tooltip title={value || "-"} arrow placement="left">
          <span
            style={{
              display: "inline-block",
              maxWidth: "300px",
              overflow: "hidden",
              textOverflow: "ellipsis",
              whiteSpace: "nowrap",
              cursor: "default",
            }}
          >
            {value || "-"}
          </span>
        </Tooltip>
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
      <BaseTable<TicketModel>
        columns={columns}
        rows={tickets}
        searchFields={["uploadedBy", "content"]}
        searchPlaceholder="Buscar por usuario o contenido"
        pageSize={10}
      />
    </Box>
  );
};

export default TicketTable;

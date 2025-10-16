import React from "react";
import { Box } from "@mui/material";
import { GridDeleteIcon } from "@mui/x-data-grid";
import { BaseTable, type Column } from "./base/BaseTable";
import { BaseSwitch } from "./base/BaseSwitch";
import type { ChatUser } from "../model/ChatUser";
import { Colors } from "../utils/Colors";

interface ChatUserTableProps {
  chatUsers: ChatUser[];
  onToggleBlocked: (isBlocked: boolean, externalId: string) => void;
  onDelete: (user: ChatUser) => void;
}

const ChatUserTable: React.FC<ChatUserTableProps> = ({
  chatUsers,
  onToggleBlocked,
  onDelete,
}) => {
  const columns: Column<ChatUser>[] = [
    { field: "email", label: "Email", width: 200 },
    { field: "phoneNumber", label: "Teléfono" },
    {
      field: "blockedAt",
      label: "Activo",
      render: (value, row) => (
        <BaseSwitch
          checked={!value}
          onChange={() => onToggleBlocked(value, row.externalId)}
        />
      ),
    },
    {
      label: "Borrar",
      sortable: false,
      render: (_value, row) => (
        <GridDeleteIcon
          onClick={() => onDelete(row)}
          style={{ cursor: "pointer", color: Colors.QUARTERNARY_DARK_GRAY }}
        />
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
      <BaseTable<ChatUser>
        columns={columns}
        rows={chatUsers}
        searchFields={["email", "phoneNumber"]}
        searchPlaceholder="Buscar por correo electrónico o número de celular"
        pageSize={6}
      />
    </Box>
  );
};

export default ChatUserTable;

import { Box } from "@mui/material";
import type { AppUserMinimalDTO } from "../model/AppUser";
import { BaseTable, type Column } from "./base/BaseTable";
import BaseIconButton from "./base/BaseIconButton";
import DeleteIcon from "@mui/icons-material/Delete";
import EditIcon from "@mui/icons-material/Edit";
import { BaseSwitch } from "./base/BaseSwitch";

interface AppUserTableProps {
  appUsers: AppUserMinimalDTO[];
  onToggleBlocked: (isBlocked: boolean, externalId: string) => void;
  onDelete: (user: AppUserMinimalDTO) => void;
  onEditUser: (user: AppUserMinimalDTO) => void;
}

export default function AppUsersTable({
  appUsers,
  onToggleBlocked,
  onDelete,
  onEditUser,
}: AppUserTableProps) {
  const columns: Column<AppUserMinimalDTO>[] = [
    {
      field: "email",
      label: "Correo electrónico",
      flex: 1,
    },
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
      label: "Acciones",
      width: 150,
      render: (_, row) => (
        <Box display="flex" gap={1}>
          <BaseIconButton onClick={() => onEditUser(row)} icon={<EditIcon />} />
          <BaseIconButton onClick={() => onDelete(row)} icon={<DeleteIcon />} />
        </Box>
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
      <BaseTable
        columns={columns}
        rows={appUsers}
        searchFields={["email"]}
        searchPlaceholder="Buscar por correo electrónico"
      />
    </Box>
  );
}

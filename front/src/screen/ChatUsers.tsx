import { useEffect, useState } from "react"
import type { ChatUser } from "../model/ChatUser"
import { ToastUtil } from "../utils/ToastUtils";
import { BaseTable, type Column } from "../components/base/BaseTable";
import ChatUserService from "../service/ChatUserService";
import { GridDeleteIcon } from "@mui/x-data-grid";
import { BaseSwitch } from "../components/base/BaseSwitch";
import BaseModal from "../components/base/BaseModal";
import { Typography } from "@mui/material";
import { AddChatUserModal } from "../components/AddChatUserModal";


export default function ChatUsers() {
  const [chatUsers, setChatUsers] = useState<ChatUser[]>([]);
  const [userToDelete, setUserToDelete] = useState<ChatUser | null>(null);

  function addUser(chatUser: ChatUser) {
    setChatUsers(currentChatUsers => [chatUser, ...currentChatUsers])
  }

  async function toggleBlocked(isBlocked: boolean, externalId: string) {
    try {
      const response = isBlocked ? await ChatUserService.unblock(externalId) : await ChatUserService.markAsBlocked(externalId);
      setChatUsers(currentChatUsers => currentChatUsers.map(chatUser =>
        chatUser.externalId === externalId
          ? { ...chatUser, ...response.data }
          : chatUser
      ))
    } catch (error) {
      console.error(error);
      ToastUtil.error("No se pudo bloquear al usario")
    }
  }

  async function deleteChatUser(externalId: string) {
    try {
      await ChatUserService.markAsDeleted(externalId);
      setChatUsers(current => current.filter((chatUser) => externalId !== chatUser.externalId))
      setUserToDelete(null);
    } catch (error) {
      console.error(error);
      ToastUtil.error("No se pudo borrar al usario")
    }
  }

  const columns: Column<ChatUser>[] = [
    { field: 'email', label: 'Email', width: 100 },
    { field: 'phoneNumber', label: 'Teléfono' },
    {
      field: 'blockedAt',
      label: 'Activo',
      render: (value, row) => <BaseSwitch checked={!value} onChange={() => toggleBlocked(value, row.externalId)} />,
    },
    {
      label: 'Borrar',
      render: (_value, row) => <GridDeleteIcon onClick={() => setUserToDelete(row)} />
    },
  ];

  useEffect(() => {
    try {
      ChatUserService.getAll().then(response => setChatUsers(response.data));
    } catch (e) {
      console.error(e);
      ToastUtil.error("Error al cargar los usuarios del chat")
    }
  }, [])

  return <>
    <BaseTable
        columns={columns}
        rows={chatUsers}
        searchFields={['email', 'phoneNumber']}
        searchPlaceholder="Buscar por correo electrónico o número de celular"
      />
      <BaseModal
        open={userToDelete !== null}
        onClose={() => setUserToDelete(null)}
        onConfirm={() => deleteChatUser(userToDelete?.externalId ?? "")}
        title="Eliminar usuario"
        children={<Typography>¿Está seguro de que desea eliminar al usuario?</Typography>}
      />
      <AddChatUserModal existingUsers={chatUsers} addUser={addUser}/>
    </> 

}
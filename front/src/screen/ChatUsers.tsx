import { useEffect, useState } from "react";
import type { ChatUser } from "../model/ChatUser";
import { ToastUtil } from "../utils/ToastUtils";
import ChatUserService from "../service/ChatUserService";
import BaseModal from "../components/base/BaseModal";
import { Typography, Box } from "@mui/material";
import { AddChatUserModal } from "../components/AddChatUserModal";
import ChatUsersTable from "../components/ChatUsersTable";
import { Colors } from "../utils/Colors";

export default function ChatUsers() {
  const [chatUsers, setChatUsers] = useState<ChatUser[]>([]);
  const [userToDelete, setUserToDelete] = useState<ChatUser | null>(null);

  function addUser(chatUser: ChatUser) {
    setChatUsers((current) => [chatUser, ...current]);
  }

  async function toggleBlocked(isBlocked: boolean, externalId: string) {
    try {
      const response = isBlocked
        ? await ChatUserService.unblock(externalId)
        : await ChatUserService.markAsBlocked(externalId);

      setChatUsers((current) =>
        current.map((chatUser) =>
          chatUser.externalId === externalId
            ? { ...chatUser, ...response.data }
            : chatUser
        )
      );
    } catch (error) {
      console.error(error);
      ToastUtil.error("No se pudo bloquear al usuario");
    }
  }

  async function deleteChatUser(externalId: string) {
    try {
      await ChatUserService.markAsDeleted(externalId);
      setChatUsers((current) =>
        current.filter((chatUser) => chatUser.externalId !== externalId)
      );
      setUserToDelete(null);
    } catch (error) {
      console.error(error);
      ToastUtil.error("No se pudo borrar al usuario");
    }
  }

  useEffect(() => {
    ChatUserService.getAll()
      .then((res) => setChatUsers(res.data))
      .catch((err) => {
        console.error(err);
        ToastUtil.error("Error al cargar los usuarios del chat");
      });
  }, []);

  return (
  <>
    <ChatUsersTable
      chatUsers={chatUsers}
      onToggleBlocked={toggleBlocked}
      onDelete={setUserToDelete}
    />

    {/* Texto aclaratorio */}
    <Box sx={{ mt: 2, mb: 3 }}>
      <Typography 
        variant="body2" 
        sx={{ 
          color: Colors.QUARTERNARY_DARK_GRAY,
          fontFamily: 'Poppins, sans-serif',
          fontSize: '0.875rem',
          fontStyle: 'italic'
        }}
      >
        Los usuarios se crean automáticamente cuando un empleado le escribe al bot y se autentica con un email válido.
      </Typography>
    </Box>

    <BaseModal
      open={userToDelete !== null}
      onClose={() => setUserToDelete(null)}
      onConfirm={() => deleteChatUser(userToDelete?.externalId ?? "")}
      title="Eliminar usuario"
    >
      <Typography>¿Está seguro de que desea eliminar al usuario?</Typography>
    </BaseModal>

      <AddChatUserModal existingUsers={chatUsers} addUser={addUser} />
    </>
    );
  }
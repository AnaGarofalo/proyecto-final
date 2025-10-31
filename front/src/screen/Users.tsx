import { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  CircularProgress
} from '@mui/material';
import AppUserService from '../service/AppUserService';
import type { AppUserMinimalDTO } from '../model/AppUser';
import EditUserModal from '../components/EditUserModal';
import '../components/base/base-table.css';
import { ToastUtil } from '../utils/ToastUtils';
import BaseModal from '../components/base/BaseModal';
import { AddUserModal } from '../components/AddUserModal';
import AppUsersTable from '../components/AppUsersTable';

export default function Users() {
  const [users, setUsers] = useState<AppUserMinimalDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [userToDelete, setUserToDelete] = useState<AppUserMinimalDTO | null>(null);
  const [userToEdit, setUserToEdit] = useState<AppUserMinimalDTO | null>(null);

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      setLoading(true);
      const response = await AppUserService.getAll();
      setUsers(response.data);
    } catch (error) {
      console.error('Error loading users:', error);
      ToastUtil.error('Error al cargar usuarios');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteUser = async (user: AppUserMinimalDTO) => {
    try {
      await AppUserService.delete(user.externalId);
      await loadUsers();
      ToastUtil.success('Usuario eliminado exitosamente');
      setUserToDelete(null);
    } catch (error) {
      console.error('Error deleting user:', error);
      ToastUtil.error('Error al eliminar usuario');
    }
  };

  const handleEditUser = async (email: string, password: string) => {
    if (!userToEdit) return;

    try {
      const updatedUser = await AppUserService.update(userToEdit.externalId, { email, password });
      setUsers(users.map(user =>
        user.externalId === userToEdit.externalId ? updatedUser.data : user
      ));
      ToastUtil.success('Usuario actualizado exitosamente');
      setUserToEdit(null);
    } catch (error) {
      console.error('Error updating user:', error);
      ToastUtil.error('Error al actualizar usuario');
    }
  };

  async function toggleBlocked(isBlocked: boolean, externalId: string) {
    try {
      const response = isBlocked
        ? await AppUserService.unblock(externalId)
        : await AppUserService.markAsBlocked(externalId);

      setUsers((current) =>
        current.map((appUser) =>
          appUser.externalId === externalId
            ? { ...appUser, ...response.data }
            : appUser
        )
      );
    } catch (error) {
      console.error(error);
      ToastUtil.error("No se pudo bloquear al usuario");
    }
  }

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="50vh">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <AppUsersTable
        appUsers={users}
        onDelete={setUserToDelete} 
        onEditUser={setUserToEdit}
        onToggleBlocked={toggleBlocked}/>

      <BaseModal
        open={userToDelete !== null}
        onClose={() => setUserToDelete(null)}
        onConfirm={() => userToDelete && handleDeleteUser(userToDelete)}
        title="Eliminar usuario"
        confirmText="Eliminar"
      >
        <Typography>
          ¿Está seguro de que desea eliminar al usuario {userToDelete?.email}?
        </Typography>
      </BaseModal>

      <EditUserModal
        open={userToEdit !== null}
        user={userToEdit}
        onClose={() => setUserToEdit(null)}
        onSave={handleEditUser}
      />

      <AddUserModal
        onUserAdded={(user) => {
          setUsers([...users, user]);
        }}
        existingUsers={users}
      />
    </Box>
  );
}
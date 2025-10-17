import { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  CircularProgress
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import { Colors } from '../utils/Colors';
import BaseIconButton from '../components/base/BaseIconButton';
import AppUserService from '../service/AppUserService';
import type { AppUserMinimalDTO } from '../model/AppUser';
import EditUserModal from '../components/EditUserModal';
import '../components/base/base-table.css';
import { ToastUtil } from '../utils/ToastUtils';
import { BaseTable, type Column } from '../components/base/BaseTable';
import BaseModal from '../components/base/BaseModal';
import { AddUserModal } from '../components/AddUserModal';

export default function Users() {
  const [users, setUsers] = useState<AppUserMinimalDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [userToDelete, setUserToDelete] = useState<AppUserMinimalDTO | null>(null);
  const [userToEdit, setUserToEdit] = useState<AppUserMinimalDTO | null>(null);

  const columns: Column<AppUserMinimalDTO>[] = [
    { 
      field: 'email', 
      label: 'Correo electrónico', 
      flex: 1 
    },
    {
      label: 'Acciones',
      width: 150,
      render: (_, row) => (
        <Box display="flex" gap={1}>
          <BaseIconButton 
            onClick={() => setUserToEdit(row)}  
            icon={<EditIcon />}
          />
          <BaseIconButton 
            onClick={() => setUserToDelete(row)}  
            icon={<DeleteIcon />}
          />
        </Box>
      ),
    },
  ];

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

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="50vh">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h4" fontWeight={700} mb={3} color={Colors.PRIMARY_DARK_BLUE}>
        Usuarios
      </Typography>

      <AddUserModal
        onUserAdded={(user) => {
          setUsers([...users, user]);
          ToastUtil.success('Usuario agregado correctamente');
        }}
        existingUsers={users}
      />

      <BaseTable
        columns={columns}
        rows={users}
        searchFields={['email']}
        searchPlaceholder="Buscar por correo electrónico"
      />

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
    </Box>
  );
}
import { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  CircularProgress
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import BaseIconButton from '../components/base/BaseIconButton';
import AppUserService from '../service/AppUserService';
import type { AppUserMinimalDTO } from '../model/AppUser';
import '../components/base/base-table.css';
import { ToastUtil } from '../utils/ToastUtils';
import { BaseTable, type Column } from '../components/base/BaseTable';
import BaseModal from '../components/base/BaseModal';

export default function Users() {
  const [users, setUsers] = useState<AppUserMinimalDTO[]>([]);
  const [loading, setLoading] = useState(true);
  const [userToDelete, setUserToDelete] = useState<AppUserMinimalDTO | null>(null);

  const columns: Column<AppUserMinimalDTO>[] = [
    { 
      field: 'email', 
      label: 'Correo electrónico', 
      flex: 1 
    },
    {
      label: 'Borrar',
      width: 100,
      render: (_, row) => (
        <BaseIconButton 
          onClick={() => setUserToDelete(row)}  
          icon={<DeleteIcon />}
        />
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

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="50vh">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      

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
    </Box>
  );
}
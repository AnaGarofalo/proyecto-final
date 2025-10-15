import { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  CircularProgress
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import { Colors } from '../utils/Colors';
import BaseIconButton from '../components/base/BaseIconButton';
import AppUserService from '../service/AppUserService';
import type { AppUserMinimalDTO } from '../model/AppUser';
import '../components/base/base-table.css';
import { ToastUtil } from '../utils/ToastUtils';
import { BaseTable, type Column } from '../components/base/BaseTable';

export default function Users() {
  const [users, setUsers] = useState<AppUserMinimalDTO[]>([]);
  const [loading, setLoading] = useState(true);

 
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
        onClick={() => handleDeleteUser(row)}
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
    if (window.confirm(`¿Estás seguro de que quieres eliminar al usuario ${user.email}?`)) {
      try {
        await AppUserService.delete(user.externalId);
        await loadUsers();
        ToastUtil.success('Usuario eliminado exitosamente');
      } catch (error) {
        console.error('Error deleting user:', error);
        ToastUtil.error('Error al eliminar usuario');
      }
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

      <BaseTable
  columns={columns}
  rows={users}
  searchFields={['email']}
  searchPlaceholder="Buscar por correo electrónico"
/>
    </Box>
  );
}

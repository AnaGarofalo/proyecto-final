import { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  CircularProgress,
  Paper
} from '@mui/material';
import { DataGrid, type GridColDef } from '@mui/x-data-grid';
import DeleteIcon from '@mui/icons-material/Delete';
import { Colors } from '../utils/Colors';
import BaseIconButton from '../components/base/BaseIconButton';
import AppUserService from '../service/AppUserService';
import type { AppUserMinimalDTO } from '../model/AppUser';
import '../components/base/base-table.css';
import { ToastUtil } from '../utils/ToastUtils';

export default function Users() {
  const [users, setUsers] = useState<AppUserMinimalDTO[]>([]);
  const [loading, setLoading] = useState(true);

  // Configuración de columnas para DataGrid
  const columns: GridColDef[] = [
    {
      field: 'email',
      headerName: 'Correo electrónico',
      flex: 1,
      renderCell: (params) => (
        <Box display="flex" alignItems="center" gap={1} className="custom-cell">
          <Typography>{params.value}</Typography>
        </Box>
      ),
    },
    {
      field: 'actions',
      headerName: 'Borrar',
      width: 100,
      sortable: false,
      renderCell: (params) => (
        <Box className="custom-cell" justifyContent="center">
          <BaseIconButton 
            variant="delete"
            onClick={() => handleDeleteUser(params.row)}
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

      {/* Tabla de usuarios con DataGrid */}
      <Paper className="base-table-paper">
        <DataGrid
          rows={users}
          columns={columns}
          loading={loading}
          getRowId={(row) => row.externalId}
          className="base-table-data-grid"
          disableRowSelectionOnClick
          hideFooterPagination
          hideFooterSelectedRowCount
          hideFooter
          autoHeight
          localeText={{
            noRowsLabel: 'No hay usuarios registrados'
          }}
        />
      </Paper>
    </Box>
  );
}

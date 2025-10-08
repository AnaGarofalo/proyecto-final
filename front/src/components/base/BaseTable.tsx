import React from 'react';
import { Box, Paper } from '@mui/material';
import { DataGrid, type GridColDef } from '@mui/x-data-grid';
import './base-table.css';

export interface Column<T> {
  field?: keyof T;
  label: string;
  align?: 'left' | 'right' | 'center';
  render?: (value: any, row: T) => React.ReactNode;
  sortable?: boolean;
  flex?: number;
  width?: number;
}

interface BaseTableProps<T> {
  columns: Column<T>[];
  rows: T[];
  pageSize?: number;
  autoHeight?: boolean;
  getRowId?: (row: T) => string | number;
}

export function BaseTable<T>({
  columns,
  rows,
  getRowId,
  pageSize = 15,
  autoHeight = true,
}: BaseTableProps<T>) {
  const gridColumns: GridColDef[] = columns.map((col) => ({
    field: col.field as string,
    headerName: col.label,
    flex: col.flex ?? 1,
    width: col.width,
    sortable: col.sortable ?? true,
    align: col.align as any,
    headerAlign: col.align as any,
    renderCell: (params) => {
      const value = params.row[col.field];
      return col.render ? (
        <Box className="custom-cell">
          {col.render(value, params.row)}
        </Box>
      ) : (
        String(value ?? '')
      );
    },
  }));

  return (
    <Paper className="base-table-paper" elevation={0}>
      <DataGrid
        className="base-table-data-grid"
        autoHeight={autoHeight}
        rows={rows}
        columns={gridColumns}
        getRowId={getRowId ?? ((row) => (row as any).id ?? JSON.stringify(row))}
        pageSizeOptions={[10, 15, 25, 50]}
        initialState={{
          pagination: { paginationModel: { pageSize, page: 0 } },
        }}
        disableRowSelectionOnClick
        localeText={{
          noRowsLabel: 'No hay datos disponibles',
        }}
      />
    </Paper>
  );
}

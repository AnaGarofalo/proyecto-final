import React, { useEffect, useState } from 'react';
import { Box, Paper } from '@mui/material';
import { DataGrid, type GridColDef } from '@mui/x-data-grid';
import './base-table.css';
import BaseInput from './BaseInput';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import z from 'zod';

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
  searchFields?: (keyof T)[];
  searchPlaceholder?: string;
}

interface SearchForm {
  value: string;
}

export const searchSchema = z.object({
  value: z.string(),
})

export function BaseTable<T>({
  columns,
  rows,
  getRowId,
  pageSize = 10,
  autoHeight = true,
  searchFields = [],
  searchPlaceholder,
}: BaseTableProps<T>) {
  const [filteredRows, setFilteredRows] = useState<T[]>([]);
  const { register, watch } = useForm<SearchForm>({
    resolver: zodResolver(searchSchema),
    defaultValues: {
      value: ""
    }
  })

  function getFilteredRows(originalArray: T[]) {
    return originalArray.filter(row => searchFields.some(field => (row[field] as string).includes(searchValue)));
  }

  const searchValue = watch('value');
  
  useEffect(() => {
    if (searchValue.length && searchFields.length) {
      setFilteredRows(getFilteredRows(rows))
    } else {
      setFilteredRows(rows);
    }
  }, [rows, searchValue])


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
      {!!searchFields.length && 
        <form>
          <BaseInput
            {...register('value')}
            placeholder={searchPlaceholder ?? ""} />
          
        </form>
      }
      <DataGrid
        className="base-table-data-grid"
        autoHeight={autoHeight}
        rows={filteredRows}
        columns={gridColumns}
        getRowId={getRowId ?? ((row) => (row as any).id ?? JSON.stringify(row))}
        pageSizeOptions={[5, 10, 25, 50]}
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

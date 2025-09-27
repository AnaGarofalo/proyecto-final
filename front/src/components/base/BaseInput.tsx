import { TextField, InputAdornment, type TextFieldProps } from '@mui/material'
import React from 'react'

type BaseInputProps = TextFieldProps & {
  label?: string
  errorMessage?: string
  icon?: React.ReactNode
}

const BaseInput: React.FC<BaseInputProps> = ({
  label,
  error,
  errorMessage,
  icon,
  ...props
}) => {
  return (
    <TextField
      fullWidth
      variant="outlined"
      label={label}
      error={error}
      helperText={error ? errorMessage : ''}
      InputProps={{
        startAdornment: icon ? (
          <InputAdornment position="start">{icon}</InputAdornment>
        ) : undefined,
      }}
      sx={{
        '& .MuiOutlinedInput-root': {
          borderRadius: 2,
          '& fieldset': {
            borderColor: '#CFCFCF', // gris por default
          },
          '&:hover fieldset': {
            borderColor: '#176B8C', // azul al hover
          },
          '&.Mui-focused fieldset': {
            borderColor: '#176B8C', // azul al focus
          },
        },
        '& .MuiInputLabel-root.Mui-focused': {
          color: '#176B8C',
        },
      }}
      {...props}
    />
  )
}

export default BaseInput
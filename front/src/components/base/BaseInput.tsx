import { TextField, InputAdornment, type TextFieldProps } from '@mui/material'
import React from 'react'
import { Colors } from '../../utils/Colors'

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
            borderColor: Colors.QUINARY_LIGHT_GRAY
          },
          '&:hover fieldset': {
            borderColor: Colors.PRIMARY_DARK_BLUE,
          },
          '&.Mui-focused fieldset': {
            borderColor: Colors.PRIMARY_DARK_BLUE, 
          },
        },
        '& .MuiInputLabel-root.Mui-focused': {
          color: Colors.PRIMARY_DARK_BLUE, 
        },
      }}
      {...props}
    />
  )
}

export default BaseInput
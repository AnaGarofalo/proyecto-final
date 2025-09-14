import { TextField, type TextFieldProps } from '@mui/material'
import React from 'react'

type BaseInputProps = TextFieldProps & {
  label?: string
  errorMessage?: string
}

const BaseInput: React.FC<BaseInputProps> = ({
  label,
  error,
  errorMessage,
  ...props
}) => {
  return (
    <TextField
      fullWidth
      variant="outlined"
      label={label}
      error={error}
      helperText={error ? errorMessage : ''}
      {...props}
    />
  )
}

export default BaseInput;
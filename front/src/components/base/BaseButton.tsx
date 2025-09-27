import { Button, type ButtonProps } from '@mui/material'
import React from 'react'

type BaseButtonVariant = 'filled' | 'outline' | 'logout'

interface Props extends Omit<ButtonProps, 'variant' | 'startIcon'> {
  variantType?: BaseButtonVariant
  icon?: React.ReactNode
}

const BaseButton: React.FC<Props> = ({ variantType = 'filled', icon, children, ...rest }) => {
  let muiVariant: 'contained' | 'outlined' | 'text' = 'contained'
  let sx: object = {}

  switch (variantType) {
    case 'filled':
      muiVariant = 'contained'
      sx = {
        bgcolor: '#176B8C',
        color: '#fff',
        '&:hover': { bgcolor: '#125871' }
      }
      break
    case 'outline':
      muiVariant = 'outlined'
      sx = {
        borderColor: '#176B8C',
        color: '#176B8C',
        '&:hover': { bgcolor: '#f4f8fb' }
      }
      break
    case 'logout':
      muiVariant = 'outlined'
      sx = {
        borderColor: '#CFCFCF',
        color: '#5A5D5D',
        '&:hover': { bgcolor: '#fafafa' }
      }
      break
  }

  return (
    <Button
      variant={muiVariant}
      startIcon={icon}
      fullWidth
      sx={{ borderRadius: 2, fontWeight: 600, textTransform: 'none', ...sx }}
      {...rest}
    >
      {children}
    </Button>
  )
}

export default BaseButton
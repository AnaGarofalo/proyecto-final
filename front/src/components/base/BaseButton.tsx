import { Button, type ButtonProps } from '@mui/material'
import React from 'react'
import { Colors } from '../../utils/Colors'

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
        bgcolor: Colors.PRIMARY_DARK_BLUE,
        color: Colors.SEPTENARY_WHITE,
        '&:hover': { bgcolor: Colors.HOVER_BLUE }
      }
      break
    case 'outline':
      muiVariant = 'outlined'
      sx = {
        borderColor: Colors.PRIMARY_DARK_BLUE,
        color: Colors.PRIMARY_DARK_BLUE,
        '&:hover': { bgcolor: Colors.HOVER_WHITE }
      }
      break
    case 'logout':
      muiVariant = 'outlined'
      sx = {
        borderColor: Colors.QUINARY_LIGHT_GRAY,
        color: Colors.QUARTERNARY_DARK_GRAY,
        '&:hover': { bgcolor: Colors.HOVER_WHITE_TWO }
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
import React from 'react';
import { IconButton, type IconButtonProps } from '@mui/material';
import { Colors } from '../../utils/Colors';

type BaseIconButtonVariant = 'edit' | 'delete' | 'close' | 'primary' | 'secondary';

interface BaseIconButtonProps extends Omit<IconButtonProps, 'color'> {
  variant?: BaseIconButtonVariant;
  icon: React.ReactNode;
}

const BaseIconButton: React.FC<BaseIconButtonProps> = ({ 
  variant = 'primary',
  icon,
  size = 'medium',
  ...rest 
}) => {
  let sx: object = {};

  switch (variant) {
    case 'edit':
      sx = {
        color: Colors.PRIMARY_DARK_BLUE,
        '&:hover': {
          backgroundColor: Colors.HOVER_WHITE,
          color: Colors.HOVER_BLUE
        }
      };
      break;
    case 'delete':
      sx = {
        color: Colors.QUARTERNARY_DARK_GRAY,
        '&:hover': {
          backgroundColor: Colors.HOVER_WHITE_TWO,
          color: '#d32f2f' // Rojo para delete
        }
      };
      break;
    case 'close':
      sx = {
        color: Colors.QUARTERNARY_DARK_GRAY,
        '&:hover': {
          backgroundColor: Colors.HOVER_WHITE_TWO
        }
      };
      break;
    case 'primary':
      sx = {
        color: Colors.PRIMARY_DARK_BLUE,
        '&:hover': {
          backgroundColor: Colors.HOVER_WHITE
        }
      };
      break;
    case 'secondary':
      sx = {
        color: Colors.QUARTERNARY_DARK_GRAY,
        '&:hover': {
          backgroundColor: Colors.HOVER_WHITE_TWO
        }
      };
      break;
  }

  return (
    <IconButton
      size={size}
      sx={sx}
      {...rest}
    >
      {icon}
    </IconButton>
  );
};

export default BaseIconButton;
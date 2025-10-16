import React from 'react';
import { IconButton, type IconButtonProps } from '@mui/material';
import { Colors } from '../../utils/Colors';

interface BaseIconButtonProps extends Omit<IconButtonProps, 'color'> {
  icon: React.ReactNode;
}

const BaseIconButton: React.FC<BaseIconButtonProps> = ({ 
  icon,
  size = 'medium',
  ...rest 
}) => {
  return (
    <IconButton
      size={size}
      sx={{
        color: Colors.QUARTERNARY_DARK_GRAY,
        '&:hover': {
          backgroundColor: Colors.HOVER_WHITE_TWO
        }
      }}
      {...rest}
    >
      {icon}
    </IconButton>
  );
};

export default BaseIconButton;
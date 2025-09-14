import { Button, type ButtonProps } from '@mui/material'
import React from 'react'

const BaseButton: React.FC<ButtonProps> = (props) => {
  return (
    <Button variant="contained" fullWidth {...props} />
  )
}

export default BaseButton
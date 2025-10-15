import { Box, Typography } from '@mui/material'
import BaseButton from '../components/base/BaseButton'
import { useNavigate } from 'react-router-dom'
import { NavigationRoute } from '../utils/NavigationUtils'
import { Colors } from '../utils/Colors'

export default function EditPromptConfirmation() {
  const navigate = useNavigate()

  return (
    <Box sx={{ textAlign: 'center', pt: 8 }}>
      <Typography variant="h6" sx={{ color: Colors.QUARTERNARY_DARK_GRAY, mb: 4 }}>
        Los cambios fueron guardados con éxito
      </Typography>

      <BaseButton variantType="filled" onClick={() => navigate(NavigationRoute.EDIT_PROMPT)}>
        Volver
      </BaseButton>
    </Box>
  )
}

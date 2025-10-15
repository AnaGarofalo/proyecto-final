import { useEffect, useState, type ChangeEvent } from 'react'
import { Box, Typography, TextField } from '@mui/material'
import { useNavigate } from 'react-router-dom'
import { NavigationRoute } from '../utils/NavigationUtils'
import BaseButton from '../components/base/BaseButton'
import BaseInput from '../components/base/BaseInput'
import { Colors } from '../utils/Colors'
import SystemPromptService from '../service/SystemPromptService'
import { ToastUtil } from '../utils/ToastUtils'

export default function EditPrompt() {
  const [loading, setLoading] = useState(false)
  const [saving, setSaving] = useState(false)
  const [prompt, setPrompt] = useState('')
  const [ticketEmail, setTicketEmail] = useState('')
  const navigate = useNavigate()

  useEffect(() => {
    setLoading(true)
    setTimeout(() => {
      setPrompt(
        'Debes brindar información referida al área de Facilities basado en la información cargada etc etc etc...'
      )
      setTicketEmail('helpdesk@nestle.com')
      setLoading(false)
    }, 250)
  }, [])

  const onSave = async () => {
    setSaving(true)
    try {
      const res = await SystemPromptService.patch({ prompt, ticketEmail })
      const ok = (res && (res.status === 200 || res.status === undefined))
      if (res?.data) {
        setPrompt(res.data.prompt)
        setTicketEmail(res.data.ticketEmail)
      }
      if (ok) {
        ToastUtil.success('Prompt actualizado correctamente')
        navigate(NavigationRoute.EDIT_PROMPT_CONFIRMATION)
        return
      }
    } catch (e: unknown) {
      const msg = (e as { message?: string })?.message || 'Error al actualizar'
      ToastUtil.error(msg)
    } finally {
      setSaving(false)
    }
  }

  return (
    <Box sx={{ padding: 4 }}>
      <Typography variant="h6" sx={{ mb: 2 }}>
        Prompt del sistema:
      </Typography>

      <Box
        sx={{
          background: '#000',
          color: Colors.PRIMARY_DARK_BLUE,
          borderRadius: 2,
          boxShadow: '0 8px 20px rgba(0,0,0,0.25)',
          p: 2,
          mb: 1,
        }}
      >
        <TextField
          multiline
          minRows={8}
          fullWidth
          value={prompt}
          onChange={(e) => setPrompt(e.target.value)}
          variant="standard"
          InputProps={{
            disableUnderline: true,
            style: { color: '#00FF41', fontFamily: 'monospace', fontSize: 14 },
          }}
        />
      </Box>

      <Typography variant="caption" color="textSecondary" sx={{ display: 'block', mb: 3 }}>
        Cuando se genere un ticket, enviarlo al mail:
      </Typography>

      <Box sx={{ maxWidth: 600, mb: 3 }}>
        <BaseInput
          label="Email de ticket"
          type="email"
          value={ticketEmail}
          onChange={(e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => setTicketEmail(e.target.value)}
        />
      </Box>

      <BaseButton variantType="filled" onClick={onSave} disabled={saving || loading}>
        {saving ? 'Guardando...' : 'Confirmar cambios'}
      </BaseButton>
    </Box>
  )
}

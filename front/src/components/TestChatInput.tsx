import React, { useState } from 'react';
import { Box } from '@mui/material';
import { Colors } from '../utils/Colors';
import BaseInput from './base/BaseInput';
import BaseButton from './base/BaseButton';

interface ChatInputProps {
  onSendMessage: (message: string) => void;
  disabled?: boolean;
}

const ChatInput: React.FC<ChatInputProps> = ({ onSendMessage, disabled = false }) => {
  const [message, setMessage] = useState('');

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (message.trim() && !disabled) {
      onSendMessage(message);
      setMessage('');
    }
  };

  const handleKeyPress = (e: React.KeyboardEvent) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSubmit(e);
    }
  };

  return (
    <Box sx={{ p: 2, borderTop: `1px solid ${Colors.QUINARY_LIGHT_GRAY}` }}>
      <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', gap: 1, alignItems: 'flex-end' }}>
        <BaseInput
          placeholder="Escribe tu mensaje aquí"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          onKeyPress={handleKeyPress}
          disabled={disabled}
          multiline
          maxRows={4}
          maxLength={200}
          sx={{ flexGrow: 1 }}
        />
        <BaseButton
          type="submit"
          disabled={!message.trim() || disabled}
          sx={{ minWidth: 'auto', px: 2 }}
        >
          Enviar
        </BaseButton>
      </Box>
    </Box>
  );
};

export default ChatInput;
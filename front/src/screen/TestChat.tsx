import { useState, useEffect } from 'react';
import { Box, Typography, CircularProgress } from '@mui/material';
import { Colors } from '../utils/Colors';
import { ToastUtil } from '../utils/ToastUtils';
import TestChatService from '../service/TestChatService';
import { MessageOrigin, type Message } from '../model/TestMessage';
import ChatMessageList from '../components/TestChatMessageList';
import ChatInput from '../components/TestChatInput';

export default function Chat() {
  const [messages, setMessages] = useState<Message[]>([]);
  const [loading, setLoading] = useState(true);
  const [sending, setSending] = useState(false);

  // Cargar historial al montar el componente
  useEffect(() => {
    loadConversationHistory();
  }, []);

  const loadConversationHistory = async () => {
    try {
      setLoading(true);
      const response = await TestChatService.getConversationHistory();
      setMessages(response.data);
    } catch (error) {
      console.error('Error loading conversation history:', error);
      ToastUtil.error('Error al cargar el historial del chat');
    } finally {
      setLoading(false);
    }
  };

  const handleSendMessage = async (content: string) => {
    if (!content.trim()) return;

    // Agregar mensaje del usuario inmediatamente
    const userMessage: Message = {
      content: content.trim(),
      origin: MessageOrigin.USER
    };
    setMessages(prev => [...prev, userMessage]);

    try {
      setSending(true);
      // Enviar mensaje al backend
      const response = await TestChatService.sendMessage({ content: content.trim() });
      
      // Agregar respuesta del bot
      setMessages(prev => [...prev, response.data]);
    } catch (error) {
      console.error('Error sending message:', error);
      ToastUtil.error('Error al enviar mensaje');
    } finally {
      setSending(false);
    }
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" height="50vh">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ height: '100%', display: 'flex', flexDirection: 'column' }}>
      <Typography variant="h4" fontWeight={700} mb={3} color={Colors.PRIMARY_DARK_BLUE}>
        Chat
      </Typography>

      <Box sx={{ 
        flexGrow: 1, 
        display: 'flex', 
        flexDirection: 'column',
        backgroundColor: Colors.SEPTENARY_WHITE,
        borderRadius: 2,
        overflow: 'hidden'
      }}>
        <ChatMessageList messages={messages} />
        <ChatInput onSendMessage={handleSendMessage} disabled={sending} />
      </Box>
    </Box>
  );
}
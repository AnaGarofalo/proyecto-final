import React, { useEffect, useRef } from 'react';
import { Box, Typography } from '@mui/material';
import { Colors } from '../utils/Colors';
import type { Message } from '../model/TestMessage';
import { MessageOrigin } from '../model/TestMessage';

interface ChatMessageListProps {
    messages: Message[];
}

const ChatMessageList: React.FC<ChatMessageListProps> = ({ messages }) => {
    const messagesEndRef = useRef<HTMLDivElement>(null);

    // Auto-scroll al último mensaje
    useEffect(() => {
        messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
    }, [messages]);

    if (messages.length === 0) {
        return (
            <Box sx={{
                flexGrow: 1,
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                p: 3
            }}>
                <Typography color={Colors.QUARTERNARY_DARK_GRAY} fontStyle="italic">
                    No hay mensajes. ¡Comienza una conversación!
                </Typography>
            </Box>
        );
    }

    return (
        <Box sx={{
            flexGrow: 1,
            overflow: 'auto',
            p: 2,
            display: 'flex',
            flexDirection: 'column',
            gap: 2
        }}>
            {messages.map((message, index) => (
                <Box
                    key={index}
                    sx={{
                        display: 'flex',
                        justifyContent: message.origin === MessageOrigin.USER ? 'flex-end' : 'flex-start'
                    }}
                >
                    <Box
                        sx={{
                            maxWidth: '70%',
                            p: 2,
                            borderRadius: 2,
                            backgroundColor: message.origin === MessageOrigin.USER
                                ? Colors.PRIMARY_DARK_BLUE
                                : Colors.TERTIARY_GRAY,
                            color: message.origin === MessageOrigin.USER
                                ? Colors.SEPTENARY_WHITE
                                : Colors.QUARTERNARY_DARK_GRAY
                        }}
                    >
                        <Typography variant="body1" sx={{ wordBreak: 'break-word' }}>
                            {message.content}
                        </Typography>
                    </Box>
                </Box>
            ))}
            <div ref={messagesEndRef} />
        </Box>
    );
};

export default ChatMessageList;
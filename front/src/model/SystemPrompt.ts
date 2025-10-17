import * as z from 'zod'

export interface SystemPrompt {
  prompt: string
  ticketEmail: string
}

export const systemPromptSchema = z.object({
  prompt: z.string(),
  ticketEmail: z.string().email('Email inválido'),
})

export type SystemPromptType = z.infer<typeof systemPromptSchema>

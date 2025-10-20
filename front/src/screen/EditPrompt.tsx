import { useEffect } from "react";
import { zodResolver } from "@hookform/resolvers/zod";
import { systemPromptSchema } from "../model/SystemPrompt";
import type { SystemPrompt } from "../model/SystemPrompt";
import { Box, Typography, TextField } from "@mui/material";
import { useForm } from "react-hook-form";
import BaseButton from "../components/base/BaseButton";
import BaseInput from "../components/base/BaseInput";
import { Colors } from "../utils/Colors";
import SystemPromptService from "../service/SystemPromptService";
import { ToastUtil } from "../utils/ToastUtils";

export default function EditPrompt() {
  const { register, handleSubmit, reset, formState } = useForm<SystemPrompt>({
    resolver: zodResolver(systemPromptSchema),
    defaultValues: { prompt: "", ticketEmail: "" },
  });

  const onSave = async (data: SystemPrompt) => {
    try {
      const res = await SystemPromptService.patch({
        prompt: data.prompt,
        ticketEmail: data.ticketEmail,
      });
      if (res?.data) {
        reset({ prompt: res.data.prompt, ticketEmail: res.data.ticketEmail });
      }
      ToastUtil.success("Prompt actualizado correctamente");
    } catch (e) {
      ToastUtil.error("Error al actualizar el prompt del sistema");
      console.error(e);
    }
  };

  useEffect(() => {
    SystemPromptService.get()
      .then((res) => {
        const data = res.data as SystemPrompt;
        reset({
          prompt: data.prompt ?? "",
          ticketEmail: data.ticketEmail ?? "",
        });
      })
      .catch(() => {
        ToastUtil.error("Error al cargar el prompt del sistema");
      });
  }, [reset]);

  return (
    <Box sx={{ padding: 4 }}>
      <Typography variant="h6" sx={{ mb: 2 }}>
        Prompt del sistema:
      </Typography>

      <Box
        sx={{
          background: "#000",
          color: Colors.PRIMARY_DARK_BLUE,
          borderRadius: 2,
          boxShadow: "0 8px 20px rgba(0,0,0,0.25)",
          p: 2,
          mb: 1,
        }}
      >
        <TextField
          multiline
          minRows={8}
          fullWidth
          {...register("prompt")}
          variant="standard"
          InputProps={{
            disableUnderline: true,
            style: {
              color: Colors.TERMINAL_TEXT_GREEN,
              fontFamily: "monospace",
              fontSize: 14,
            },
          }}
        />
      </Box>

      <Typography
        variant="caption"
        color="textSecondary"
        sx={{ display: "block", mb: 3 }}
      >
        Cuando se genere un ticket, enviarlo al mail:
      </Typography>

      <Box sx={{ maxWidth: 600, mb: 3 }}>
        <BaseInput
          label="Email de ticket"
          type="email"
          InputLabelProps={{ shrink: true }}
          {...register("ticketEmail")}
        />
      </Box>

      <form onSubmit={handleSubmit(onSave)}>
        <BaseButton type="submit" variantType="filled">
          {formState.isSubmitting ? "Guardando..." : "Confirmar cambios"}
        </BaseButton>
      </form>
    </Box>
  );
}

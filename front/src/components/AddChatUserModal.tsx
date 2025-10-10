import { Box } from "@mui/material";
import BaseButton from "./base/BaseButton";
import { useState } from "react";
import BaseModal from "./base/BaseModal";
import BaseInput from "./base/BaseInput";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, type SubmitHandler } from "react-hook-form";
import { ToastUtil } from "../utils/ToastUtils";
import { useNavigate } from "react-router-dom";
import ChatUserService from "../service/ChatUserService";
import { chatUserSchema, type CreateChatUser } from "../model/ChatUser";
import { NavigationRoute } from "../utils/NavigationUtils";

export function AddChatUserModal() {
    const [showModal, setShowModal] = useState(false);
    const navigate = useNavigate();

    const { register, handleSubmit, formState: { errors } } = useForm<CreateChatUser>({
        resolver: zodResolver(chatUserSchema),
    })

    const createUser: SubmitHandler<CreateChatUser> = async (data: CreateChatUser) => {
        try {
            await ChatUserService.create(data)
            navigate(NavigationRoute.CHAT_USERS_SUCCESS)
        } catch (e) {
            ToastUtil.error("Error al crear usuario");
            console.error(e);
        }
    }

    return <Box sx={{
        width: "100%",
        padding: "24px",
        display: "flex",
        flexDirection: "row",
        justifyContent: "end"
    }}>
        <BaseButton fullWidth={false} onClick={() => setShowModal(true)}>Agregar Usuario</BaseButton>
        <BaseModal
            open={showModal}
            onClose={() => setShowModal(false)}
            title="Agregar usuario"
            onConfirm={handleSubmit(createUser)}
            children={
                <form>
                    <BaseInput
                        label="Email"
                        type="email"
                        {...register('email')}
                        error={!!errors.email}
                        errorMessage={errors.email?.message}
                        margin="normal"
                        required
                    />

                    <BaseInput
                        label="Teléfono"
                        {...register('phoneNumber')}
                        error={!!errors.phoneNumber}
                        errorMessage={errors.phoneNumber?.message}
                        margin="normal"
                        required
                    />
                </form>
            }
        />
    </Box>
}
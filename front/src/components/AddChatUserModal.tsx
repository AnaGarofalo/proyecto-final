import { Box } from "@mui/material";
import BaseButton from "./base/BaseButton";
import { useState } from "react";
import BaseModal from "./base/BaseModal";
import BaseInput from "./base/BaseInput";
import { zodResolver } from "@hookform/resolvers/zod";
import { useForm, type SubmitHandler } from "react-hook-form";
import { ToastUtil } from "../utils/ToastUtils";
import ChatUserService from "../service/ChatUserService";
import { chatUserSchema, type ChatUser, type CreateChatUser } from "../model/ChatUser";

interface AddChatUserModalProps {
    existingUsers: ChatUser[];
    addUser: (chatUser: ChatUser) => void;
}

export function AddChatUserModal({ existingUsers, addUser }: AddChatUserModalProps) {
    const [showModal, setShowModal] = useState(false);

    const { register, handleSubmit, getValues, formState: { errors } } = useForm<CreateChatUser>({
        resolver: zodResolver(chatUserSchema),
    })

    const createUser: SubmitHandler<CreateChatUser> = async (data: CreateChatUser) => {
        try {
            const toCreatePhoneNumber = getValues('phoneNumber');
            const existingUser = existingUsers.find(chatUser => chatUser.phoneNumber === toCreatePhoneNumber);

            if (existingUser) {
                ToastUtil.warning("El teléfono ya está en uso");
                return;
            }
            
            const createdUser = await ChatUserService.create(data);
            ToastUtil.info("Usuario creado exitosamente");
            addUser(createdUser.data);
            setShowModal(false);
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
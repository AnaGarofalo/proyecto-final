# 🤖 Proyecto Final: Asistente Versátil Digital Nestlé

Integrantes - GRUPO 5 - 2° 2° C Almagro - Instituto ORT Argentina:

- Ana María Garófalo
- Clara Franzoni
- Luis Acosta
- Lucas Hochman
- Christian Benito
- **Docente:** Jorge Velurtas

---

## 📄 Introducción

Este proyecto es una aplicación de gestión que integra un chatbot a través de **WhatsApp** con capacidades de **IA** para responder consultas de usuarios, generar tickets y enviarlos por email si es necesario, y permite la gestión de usuarios y documentos.

---

## 🛠️ Tecnologías Utilizadas

El proyecto está construido como una arquitectura de microservicios utilizando las siguientes tecnologías:

### Backend (Servidor y API)

| Categoría                   | Tecnologías                                                          |
| :-------------------------- | :------------------------------------------------------------------- |
| **Lenguaje y Framework**    | Java 17+, Spring Boot (Spring Data JPA, Spring Security, Spring Web) |
| **Construcción**            | Maven                                                                |
| **Comunicación**            | API REST                                                             |
| **Inteligencia Artificial** | Integración con OpenAI API (modelos de lenguaje)                     |
| **Mensajería**              | Integración con WhatsApp Business API                                |
| **Email**                   | Javamail                                                             |
| **Seguridad**               | JSON Web Tokens (JWT) para autenticación                             |

### Frontend (Interfaz de Usuario)

| Categoría               | Tecnologías                                           |
| :---------------------- | :---------------------------------------------------- |
| **Lenguaje y Librería** | React, TypeScript                                     |
| **Tooling/Bundler**     | Vite                                                  |
| **Estilos**             | Material UI                                           |
| **Servidor Web**        | Nginx (utilizado para servir la aplicación en Docker) |

### Infraestructura y Base de Datos

| Categoría         | Tecnologías             |
| :---------------- | :---------------------- |
| **Orquestación**  | Docker y Docker Compose |
| **Base de Datos** | PostgreSQL              |

---

## 🔑 Variables de Entorno Requeridas

Las variables de entorno son esenciales para la configuración de la aplicación, especialmente para los servicios externos como la Base de Datos, OpenAI y WhatsApp. Estas variables suelen cargarse a través de archivos `.env`.

### Variables del Backend (Spring Boot)

_Ubicación requerida: `/.env`_

| Variable                     | Descripción                                                | Componente Afectado      |
| :--------------------------- | :--------------------------------------------------------- | :----------------------- |
| `SPRING_DATASOURCE_URL`      | URL de conexión (`jdbc:postgresql://db:5432/nestle_db`)    | PostgreSQL / Spring Boot |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de datos.                               | PostgreSQL / Spring Boot |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña del usuario de la base de datos.                | PostgreSQL / Spring Boot |
| `JWT_SECRET`                 | Clave secreta fuerte utilizada para firmar los tokens JWT. | Spring Security          |
| `OPENAI_API_KEY`             | Clave de API para el servicio de OpenAI.                   | OpenAI Config            |
| `WHATSAPP_API_TOKEN`         | Token de acceso de la API de WhatsApp Business.            | WhatsApp Client          |
| `WHATSAPP_PHONE_ID`          | Identificador del número de teléfono de WhatsApp.          | WhatsApp Client          |
| `WHATSAPP_VERIFY_TOKEN`      | Token de verificación para el webhook de WhatsApp.         | WhatsApp Webhook         |
| `SPRING_MAIL_HOST`           | Host del servidor de correo saliente (SMTP).               | Email Service            |
| `SPRING_MAIL_PORT`           | Puerto del servidor SMTP.                                  | Email Service            |
| `SPRING_MAIL_USERNAME`       | Correo electrónico de la cuenta de envío de tickets.       | Email Service            |
| `SPRING_MAIL_PASSWORD`       | Contraseña o App Password de la cuenta de correo.          | Email Service            |

### Variables del Frontend (React/Vite)

_Ubicación requerida: `/front/.env`_

| Variable            | Descripción                               | Componente Afectado |
| :------------------ | :---------------------------------------- | :------------------ |
| `VITE_API_BASE_URL` | URL base del Backend para las peticiones. | Axios / Frontend    |

---

## 🚀 Puesta en Marcha

Sigue estos pasos para levantar el proyecto:

1.  **Clonar el repositorio:**

    ```bash
    git clone [https://github.com/AnaGarofalo/proyecto-final.git](https://github.com/AnaGarofalo/proyecto-final.git)
    cd proyecto-final
    ```

2.  **Configurar Variables:**
    Asegúrate de crear los archivos `.env` con las credenciales necesarias en las carpetas del backend y frontend según la tabla de arriba.

3.  **Ejecutar con Docker:**
    Levanta la base de datos, el backend y el frontend con un solo comando:
    ```bash
    docker-compose up --build
    ```

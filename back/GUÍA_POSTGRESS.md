# 🧠 Guía de Instalación y Configuración de PostgreSQL + pgvector (Windows)

## 🪟 1. Instalar PostgreSQL

1. Descargar desde: [https://www.postgresql.org/download/windows/](https://www.postgresql.org/download/windows/)
2. Instalar con los siguientes componentes:
   - ✅ PostgreSQL Server  
   - ✅ pgAdmin 4  
   - ✅ Command Line Tools
3. Puerto: **5432**
4. Definir contraseña para el usuario **postgres**.

> Al finalizar, abrir **Stack Builder**.

---

## 🧩 2. Instalar la extensión **pgvector**

1. En **Stack Builder**, elegir la versión instalada (ej. *PostgreSQL 16*).
2. Buscar y seleccionar **pgvector** en “Add-ons, tools and utilities”.
3. Instalar.

---

## 🧰 3. Conectarse a PostgreSQL

Abrir **SQL Shell (psql)** y conectarse con:

```
Usuario: postgres
Contraseña: (la definida durante la instalación)
```

---

## 🏗️ 4. Crear las bases de datos

```sql
CREATE DATABASE proyectofinal;
CREATE DATABASE proyectofinal_test;
CREATE DATABASE proyectofinal_rag;
```

---

## 👤 5. Crear el usuario del proyecto

```sql
CREATE USER proyectofinal WITH PASSWORD 'proyectofinal';
```

---

## 🔐 6. Dar privilegios al usuario

```sql
GRANT ALL PRIVILEGES ON DATABASE proyectofinal TO proyectofinal;
GRANT ALL PRIVILEGES ON DATABASE proyectofinal_test TO proyectofinal;
GRANT ALL PRIVILEGES ON DATABASE proyectofinal_rag TO proyectofinal;
```

---

## 🧱 7. Permisos sobre los esquemas

```sql
\c proyectofinal
GRANT ALL PRIVILEGES ON SCHEMA public TO proyectofinal;

\c proyectofinal_test
GRANT ALL PRIVILEGES ON SCHEMA public TO proyectofinal;

\c proyectofinal_rag
GRANT ALL PRIVILEGES ON SCHEMA public TO proyectofinal;
```

---

## 🧬 8. Habilitar la extensión vector (solo RAG)

```sql
CREATE EXTENSION vector;
```

---

## ✅ 9. Verificar instalación

En **pgAdmin 4**, verificar:

- Bases: `proyectofinal`, `proyectofinal_test`, `proyectofinal_rag`
- Usuario: `proyectofinal`
- Extensión `vector` instalada en `proyectofinal_rag`

```sql
\dx
```

Debería aparecer:

```
 Name   | Version | Schema | Description
--------+----------+--------+-------------
 vector | 0.5.0    | public | Open-source vector similarity search for PostgreSQL
```

---

## 🧠 Resumen

| Concepto | Valor |
|-----------|--------|
| Puerto | **5432** |
| Usuario admin | `postgres` |
| Usuario app | `proyectofinal` |
| Bases | `proyectofinal`, `proyectofinal_test`, `proyectofinal_rag` |
| Extensión | `pgvector` en `proyectofinal_rag` |
| Contraseña ejemplo | `proyectofinal` |

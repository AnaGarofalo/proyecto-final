-- ============================================
-- 🏗️ Crear bases de datos del proyecto
-- ============================================

CREATE DATABASE proyectofinal;
CREATE DATABASE proyectofinal_test;
CREATE DATABASE proyectofinal_rag;

-- ============================================
-- 👤 Crear usuario de aplicación
-- ============================================

DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'proyectofinal') THEN
      CREATE USER proyectofinal WITH PASSWORD 'proyectofinal';
   END IF;
END
$$;

-- ============================================
-- 🔐 Dar privilegios sobre las bases de datos
-- ============================================

GRANT ALL PRIVILEGES ON DATABASE proyectofinal TO proyectofinal;
GRANT ALL PRIVILEGES ON DATABASE proyectofinal_test TO proyectofinal;
GRANT ALL PRIVILEGES ON DATABASE proyectofinal_rag TO proyectofinal;

-- ============================================
-- 🧱 Dar permisos sobre los esquemas
-- ============================================

\connect proyectofinal
GRANT ALL PRIVILEGES ON SCHEMA public TO proyectofinal;

\connect proyectofinal_test
GRANT ALL PRIVILEGES ON SCHEMA public TO proyectofinal;

\connect proyectofinal_rag
GRANT ALL PRIVILEGES ON SCHEMA public TO proyectofinal;

-- ============================================
-- 🧬 Habilitar la extensión pgvector (solo RAG)
-- ============================================

CREATE EXTENSION IF NOT EXISTS vector;

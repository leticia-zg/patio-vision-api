-- Cria um usuário admin inicial se não existir (email fixo)
INSERT INTO pvuser (email, name, password, role, avatar_url)
SELECT 'admin@local', 'Administrador', '$2a$10$1Tm8pJ0VgBpP/2vPqEUiwOpNqTkmG1M7BktgUT2Hzk3e4DPgXJPgm', 'ROLE_ADMIN', null
WHERE NOT EXISTS (SELECT 1 FROM pvuser WHERE email = 'admin@local');
-- senha em texto plano: admin123
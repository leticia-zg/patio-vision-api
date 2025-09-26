-- Add password and role columns to pvuser for form-based authentication
ALTER TABLE pvuser
    ADD COLUMN password VARCHAR(255);

ALTER TABLE pvuser
    ADD COLUMN role VARCHAR(50) DEFAULT 'ROLE_USER';

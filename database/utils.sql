-- Exibir tudo
SELECT * FROM project111.url;

-- Total
SELECT COUNT(*) FROM project111.url;

-- Exibir expirados
SELECT * FROM project111.url WHERE expires_at < now();

-- Exirar todos
UPDATE project111.url SET expires_at = now() - INTERVAL '1 DAY';



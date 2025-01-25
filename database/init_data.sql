SET client_encoding TO utf8;

CREATE SCHEMA IF NOT EXISTS "project111";

CREATE TABLE project111.user (
    id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(0) NOT NULL,
    CONSTRAINT user_email_uk UNIQUE (email),
    PRIMARY KEY (id)
);

CREATE TABLE project111.url (
    hash VARCHAR(6),
    target_url VARCHAR NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    expires_at DATE NOT NULL,
    user_id UUID NOT NULL,
    PRIMARY KEY (hash)
);

ALTER TABLE IF EXISTS project111.url
    ADD CONSTRAINT url_user_id_fk FOREIGN KEY (user_id) REFERENCES project111.user;
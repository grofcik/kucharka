-- Kucharka Database Schema
-- PostgreSQL 17

-- Drop tables if they exist (for fresh start)
DROP TABLE IF EXISTS zasoba CASCADE;
DROP TABLE IF EXISTS jedalnicek_polozka CASCADE;
DROP TABLE IF EXISTS jedalnicek CASCADE;
DROP TABLE IF EXISTS recept_ingrediencia CASCADE;
DROP TABLE IF EXISTS recept CASCADE;
DROP TABLE IF EXISTS ingrediencia CASCADE;
DROP TABLE IF EXISTS kategoria CASCADE;

-- Kategoria - kategorie receptov
CREATE TABLE kategoria (
    id BIGSERIAL PRIMARY KEY,
    nazov VARCHAR(100) NOT NULL UNIQUE
);

-- Ingrediencia - ciselnik surovín
CREATE TABLE ingrediencia (
    id BIGSERIAL PRIMARY KEY,
    nazov VARCHAR(100) NOT NULL UNIQUE,
    jednotka VARCHAR(10) NOT NULL CHECK (jednotka IN ('G', 'ML', 'KS', 'LYZICA', 'STIPKA')),
    oddelenie VARCHAR(30) NOT NULL DEFAULT 'INE'
);

-- Recept - hlavicka receptu
CREATE TABLE recept (
    id BIGSERIAL PRIMARY KEY,
    nazov VARCHAR(200) NOT NULL,
    postup TEXT,
    cas_pripravy_min INT CHECK (cas_pripravy_min > 0),
    pocet_porcii INT NOT NULL CHECK (pocet_porcii > 0),
    kategoria_id BIGINT REFERENCES kategoria(id) ON DELETE SET NULL
);

-- Recept_Ingrediencia - spojovacia tabulka M:N
CREATE TABLE recept_ingrediencia (
    recept_id BIGINT NOT NULL REFERENCES recept(id) ON DELETE CASCADE,
    ingrediencia_id BIGINT NOT NULL REFERENCES ingrediencia(id) ON DELETE RESTRICT,
    mnozstvo NUMERIC(10,2) NOT NULL CHECK (mnozstvo > 0),
    PRIMARY KEY (recept_id, ingrediencia_id)
);

-- Jedalnicek - tyzdenny jedalnicek
CREATE TABLE jedalnicek (
    id BIGSERIAL PRIMARY KEY,
    nazov VARCHAR(100) NOT NULL,
    datum_od DATE NOT NULL
);

-- Jedalnicek_Polozka - polozky v jedalniciku
CREATE TABLE jedalnicek_polozka (
    id BIGSERIAL PRIMARY KEY,
    jedalnicek_id BIGINT NOT NULL REFERENCES jedalnicek(id) ON DELETE CASCADE,
    recept_id BIGINT NOT NULL REFERENCES recept(id) ON DELETE RESTRICT,
    den SMALLINT NOT NULL CHECK (den BETWEEN 1 AND 7),
    pocet_porcii INT NOT NULL CHECK (pocet_porcii > 0)
);

-- Zasoba - co mam doma (spajza)
CREATE TABLE zasoba (
    ingrediencia_id BIGINT PRIMARY KEY REFERENCES ingrediencia(id) ON DELETE CASCADE,
    mnozstvo NUMERIC(10,2) NOT NULL CHECK (mnozstvo >= 0)
);

-- Indexes for better performance
CREATE INDEX idx_recept_kategoria ON recept(kategoria_id);
CREATE INDEX idx_recept_ingrediencia_recept ON recept_ingrediencia(recept_id);
CREATE INDEX idx_recept_ingrediencia_ingrediencia ON recept_ingrediencia(ingrediencia_id);
CREATE INDEX idx_jedalnicek_polozka_jedalnicek ON jedalnicek_polozka(jedalnicek_id);
CREATE INDEX idx_jedalnicek_polozka_recept ON jedalnicek_polozka(recept_id);
CREATE INDEX idx_zasoba_ingrediencia ON zasoba(ingrediencia_id);

-- Grant permissions
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO kucharka;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO kucharka;

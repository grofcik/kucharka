-- Kucharka Sample Data
-- Ukazkove data pre testovanie

-- Kategorie
INSERT INTO kategoria (nazov) VALUES 
    ('Polievka'),
    ('Hlavne jedlo'),
    ('Priloha'),
    ('Dezert'),
    ('Salat'),
    ('Snihdane');

-- Ingrediencie
INSERT INTO ingrediencia (nazov, jednotka, oddelenie) VALUES 
    -- Zelenina, ovocie
    ('Zemiaky', 'G', 'ZELENINA_OVOCIE'),
    ('Cibula', 'KS', 'ZELENINA_OVOCIE'),
    ('Mrkva', 'G', 'ZELENINA_OVOCIE'),
    ('Paprika', 'KS', 'ZELENINA_OVOCIE'),
    ('Paradajka', 'KS', 'ZELENINA_OVOCIE'),
    ('Jablko', 'KS', 'ZELENINA_OVOCIE'),
    
    -- Mliecne vyrobky
    ('Mlieko', 'ML', 'MLIECNE'),
    ('Maslo', 'G', 'MLIECNE'),
    ('Syr', 'G', 'MLIECNE'),
    
    -- Pecivo
    ('Chleb', 'KS', 'PECIVO'),
    ('Rohlik', 'KS', 'PECIVO'),
    
    -- Maso
    ('Kuracie maso', 'G', 'MASO'),
    ('Hovadzie maso', 'G', 'MASO'),
    
    -- Ostatne
    ('Soľ', 'LYZICA', 'KORENENIE'),
    ('Cukor', 'LYZICA', 'KORENENIE'),
    ('Olej', 'ML', 'OLEJE_TUKY'),
    ('Muka', 'G', 'SYPKINY'),
    ('Vajce', 'KS', 'MLIECNE'),
    ('Caj', 'STIPKA', 'NAPOJE');

-- Recepty
INSERT INTO recept (nazov, postup, cas_pripravy_min, pocet_porcii, kategoria_id) VALUES 
    -- Zemiakova kasa
    ('Zemiaková kaša', 
     'Zemiaky ošúpeme, nakrájame na kocky a uvaríme v osolenej vode. Odlejem a rozmačkáme. Pridáme maslo a mlieko, dohladká rozmiešame. Ochutíme solou.',
     30, 4, 
     (SELECT id FROM kategoria WHERE nazov = 'Hlavne jedlo')),
    
    -- Kuracie pragre
    ('Kuracie pragre',
     'Kuracie maso nakrájame na pragre, osolíme, opepříme. Obalíme v muke, rozšľahanom vajci a v struhankach. Vyprážame v oleji z oboch strán do zlatena.',
     45, 4,
     (SELECT id FROM kategoria WHERE nazov = 'Hlavne jedlo')),
    
    -- Zemiakovy salat
    ('Zemiakový salát',
     'Uvarené zemiaky necháme vychladnúť, nakrájame na kocky. Pridáme nakrájanú cibulu, papriku, paradojku. Ochutíme solou, olejom a octom.',
     20, 6,
     (SELECT id FROM kategoria WHERE nazov = 'Salat')),
    
    -- Jablkovy zahlev
    ('Jablkový záhlev',
     'Jablká olúpeme, vykrájame jadrovník a nakrájame na plátky. Vložíme do hrnca s vodou, cukrom a skoricou. Varíme 15 minút. Podávame teplé.',
     25, 4,
     (SELECT id FROM kategoria WHERE nazov = 'Dezert')),
    
    -- Hovadzia polievka
    ('Hovadzia polievka',
     'Hovadzie maso uvaríme v osolenej vode s celou cibulou, mrkvou a korením. Neskôr pridáme nakrájanú zeleninu. Podávame s petržlenom.',
     60, 4,
     (SELECT id FROM kategoria WHERE nazov = 'Polievka'));

-- Recept Ingrediencie (pre Zemiakovu kasu)
INSERT INTO recept_ingrediencia (recept_id, ingrediencia_id, mnozstvo) VALUES 
    ((SELECT id FROM recept WHERE nazov = 'Zemiaková kaša'), (SELECT id FROM ingrediencia WHERE nazov = 'Zemiaky'), 1000),
    ((SELECT id FROM recept WHERE nazov = 'Zemiaková kaša'), (SELECT id FROM ingrediencia WHERE nazov = 'Mlieko'), 200),
    ((SELECT id FROM recept WHERE nazov = 'Zemiaková kaša'), (SELECT id FROM ingrediencia WHERE nazov = 'Maslo'), 50),
    ((SELECT id FROM recept WHERE nazov = 'Zemiaková kaša'), (SELECT id FROM ingrediencia WHERE nazov = 'Soľ'), 1);

-- Recept Ingrediencie (pre Kuracie pragre)
INSERT INTO recept_ingrediencia (recept_id, ingrediencia_id, mnozstvo) VALUES 
    ((SELECT id FROM recept WHERE nazov = 'Kuracie pragre'), (SELECT id FROM ingrediencia WHERE nazov = 'Kuracie maso'), 800),
    ((SELECT id FROM recept WHERE nazov = 'Kuracie pragre'), (SELECT id FROM ingrediencia WHERE nazov = 'Vajce'), 2),
    ((SELECT id FROM recept WHERE nazov = 'Kuracie pragre'), (SELECT id FROM ingrediencia WHERE nazov = 'Muka'), 150),
    ((SELECT id FROM recept WHERE nazov = 'Kuracie pragre'), (SELECT id FROM ingrediencia WHERE nazov = 'Olej'), 200),
    ((SELECT id FROM recept WHERE nazov = 'Kuracie pragre'), (SELECT id FROM ingrediencia WHERE nazov = 'Soľ'), 1);

-- Recept Ingrediencie (pre Zemiakovy salat)
INSERT INTO recept_ingrediencia (recept_id, ingrediencia_id, mnozstvo) VALUES 
    ((SELECT id FROM recept WHERE nazov = 'Zemiakový salát'), (SELECT id FROM ingrediencia WHERE nazov = 'Zemiaky'), 1000),
    ((SELECT id FROM recept WHERE nazov = 'Zemiakový salát'), (SELECT id FROM ingrediencia WHERE nazov = 'Cibula'), 1),
    ((SELECT id FROM recept WHERE nazov = 'Zemiakový salát'), (SELECT id FROM ingrediencia WHERE nazov = 'Paprika'), 2),
    ((SELECT id FROM recept WHERE nazov = 'Zemiakový salát'), (SELECT id FROM ingrediencia WHERE nazov = 'Paradajka'), 3),
    ((SELECT id FROM recept WHERE nazov = 'Zemiakový salát'), (SELECT id FROM ingrediencia WHERE nazov = 'Olej'), 100),
    ((SELECT id FROM recept WHERE nazov = 'Zemiakový salat'), (SELECT id FROM ingrediencia WHERE nazov = 'Soľ'), 1);

-- Recept Ingrediencie (pre Jablkovy zahlev)
INSERT INTO recept_ingrediencia (recept_id, ingrediencia_id, mnozstvo) VALUES 
    ((SELECT id FROM recept WHERE nazov = 'Jablkový záhlev'), (SELECT id FROM ingrediencia WHERE nazov = 'Jablko'), 8),
    ((SELECT id FROM recept WHERE nazov = 'Jablkový záhlev'), (SELECT id FROM ingrediencia WHERE nazov = 'Cukor'), 5),
    ((SELECT id FROM recept WHERE nazov = 'Jablkový záhlev'), (SELECT id FROM ingrediencia WHERE nazov = 'Caj'), 1);

-- Recept Ingrediencie (pre Hovadziu polievku)
INSERT INTO recept_ingrediencia (recept_id, ingrediencia_id, mnozstvo) VALUES 
    ((SELECT id FROM recept WHERE nazov = 'Hovadzia polievka'), (SELECT id FROM ingrediencia WHERE nazov = 'Hovadzie maso'), 500),
    ((SELECT id FROM recept WHERE nazov = 'Hovadzia polievka'), (SELECT id FROM ingrediencia WHERE nazov = 'Zemiaky'), 400),
    ((SELECT id FROM recept WHERE nazov = 'Hovadzia polievka'), (SELECT id FROM ingrediencia WHERE nazov = 'Mrkva'), 200),
    ((SELECT id FROM recept WHERE nazov = 'Hovadzia polievka'), (SELECT id FROM ingrediencia WHERE nazov = 'Cibula'), 1),
    ((SELECT id FROM recept WHERE nazov = 'Hovadzia polievka'), (SELECT id FROM ingrediencia WHERE nazov = 'Soľ'), 1);

-- Jedalnicek
INSERT INTO jedalnicek (nazov, datum_od) VALUES 
    ('Prvy tyzden - November', '2026-11-03'),
    ('Druhy tyzden - November', '2026-11-10');

-- Jedalnicek Polozky (Prvy tyzden)
INSERT INTO jedalnicek_polozka (jedalnicek_id, recept_id, den, pocet_porcii) VALUES 
    ((SELECT id FROM jedalnicek WHERE nazov = 'Prvy tyzden - November'), (SELECT id FROM recept WHERE nazov = 'Hovadzia polievka'), 1, 4),
    ((SELECT id FROM jedalnicek WHERE nazov = 'Prvy tyzden - November'), (SELECT id FROM recept WHERE nazov = 'Kuracie pragre'), 2, 4),
    ((SELECT id FROM jedalnicek WHERE nazov = 'Prvy tyzden - November'), (SELECT id FROM recept WHERE nazov = 'Zemiaková kaša'), 3, 4),
    ((SELECT id FROM jedalnicek WHERE nazov = 'Prvy tyzden - November'), (SELECT id FROM recept WHERE nazov = 'Zemiakový salát'), 4, 6),
    ((SELECT id FROM jedalnicek WHERE nazov = 'Prvy tyzden - November'), (SELECT id FROM recept WHERE nazov = 'Jablkový záhlev'), 5, 4);

-- Zasoba (spajza - co mam doma)
INSERT INTO zasoba (ingrediencia_id, mnozstvo) VALUES 
    ((SELECT id FROM ingrediencia WHERE nazov = 'Zemiaky'), 2000),
    ((SELECT id FROM ingrediencia WHERE nazov = 'Mlieko'), 500),
    ((SELECT id FROM ingrediencia WHERE nazov = 'Maslo'), 200),
    ((SELECT id FROM ingrediencia WHERE nazov = 'Soľ'), 10),
    ((SELECT id FROM ingrediencia WHERE nazov = 'Cibula'), 5),
    ((SELECT id FROM ingrediencia WHERE nazov = 'Mrkva'), 300),
    ((SELECT id FROM ingrediencia WHERE nazov = 'Olej'), 500),
    ((SELECT id FROM ingrediencia WHERE nazov = 'Muka'), 1000),
    ((SELECT id FROM ingrediencia WHERE nazov = 'Vajce'), 10),
    ((SELECT id FROM ingrediencia WHERE nazov = 'Jablko'), 20),
    ((SELECT id FROM ingrediencia WHERE nazov = 'Cukor'), 20);

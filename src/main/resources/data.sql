INSERT INTO localizacao (bairro, cidade, uf) VALUES
  ('Centro', 'São Francisco do Sul', 'SC'),
  ('Floresta', 'Joinville', 'SC');

INSERT INTO pet (
  pelo, id_localizacao, porte, raca, tipo, sexo, categoria, vacinacao, castracao
) VALUES
  (1, 1, 1, 1, 1, 1, 1, 1, 1),
  (2, 2, 2, 2, 2, 2, 2, 2, 2),
  (3, 1, 3, 6, 3, 3, 3, 3, 3);

INSERT INTO registro (id_pet, situacao, data) VALUES
  (1, 1, '2018-01-01T01:00:00'),
  (1, 2, '2018-02-01T02:00:00'),
  (1, 3, '2018-03-01T03:00:00'),
  (1, 2, '2018-02-03T02:00:00'),
  (1, 3, '2018-02-03T03:00:00');

INSERT INTO pet_cor (PET_ID, CORES) VALUES
  (1, 1),
  (1, 2),
  (1, 3);
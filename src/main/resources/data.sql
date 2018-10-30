INSERT INTO localizacao (bairro, cidade, uf) VALUES
  ('Centro', 'São Francisco do Sul', 'SC'),
  ('Floresta', 'Joinville', 'SC');

INSERT INTO pet (porte, tipo, categoria, comprimento_pelo, sexo, raca, id_localizacao) VALUES
  (1, 1, 1, 1, 1, 1, 1),
  (2, 2, 2, 4, 2, 2, null),
  (3, 3, 3, 3, 1, 3, null);

INSERT INTO registro (id_pet, situacao, data) VALUES
  (1, 1, '2018-01-01T01:00:00'),
  (1, 2, '2018-02-01T02:00:00'),
  (1, 3, '2018-03-01T03:00:00'),
  (2, 2, '2018-02-03T02:00:00'),
  (3, 3, '2018-02-03T03:00:00');

INSERT INTO pet_cor (PET_ID, CORES) VALUES
  (1, 1),
  (1, 2),
  (3, 3);
INSERT INTO LOCALIZACAO (BAIRRO, CIDADE, UF) VALUES
('Jardim São Jorge', 'Londrina', 'Paraná'),
('Vila Olinda', 'Rondonópolis', 'Mato Grosso'),
('Parque Dois Irmãos', 'Fortaleza', 'Ceará'),
('Cafezinho', 'Ji-Paraná', 'Rondônia'),
('Seis de Agosto', 'Rio Branco', 'Acre'),
('Serrinha', 'Fortaleza', 'Ceará'),
('Monte Verde', 'Florianópolis', 'Santa Catarina'),
('Santa Maria', 'Aracaju', 'Sergipe');

INSERT INTO USUARIO (CREATED_DATE, LAST_MODIFIED_DATE, DATA_NASCIMENTO, EMAIL, FACEBOOK_USER_ID, NOME, SENHA, SEXO, LOCALIZACAO_ID, EMAIL_PUBLICO) VALUES
('2018-02-23 13:18:25', '2018-08-24 17:24:53', '1970-12-10', 'dchristophle0@example.com', null, 'Danny Christophle', '$2a$10$YIqp1w.m/4brTM9vfXyuc.qCB/e4UkF8WMUh9b2ySspSXzCZMlZLS', 1, 1, 1),
('2018-02-18 03:33:23', '2018-08-18 06:33:23', '1988-10-12', 'gnusche1@example.com', null, 'Gualterio Nusche', '$2a$10$2tTVuJQAyEhyy77MHniEGOEyJQave6ezht17Q8AOU5QPZqvtoul5e', 1, 2, 1),
('2018-02-26 05:31:25', '2018-09-26 14:58:10', null, 'dwortt2@example.com', null, 'Dennie Wortt', '$2a$10$bC0R9ksjYhd//5m10ciPluxpjOUT75bYa6ya/rZ1qiARaZGB.yinC', 3, 3, 0),
('2018-02-13 06:12:17', '2018-07-16 06:44:07', '2008-11-05', 'sconnal3@example.com', null, 'Salomi Connal', '$2a$10$V4ej2VaXtDLgEM0dQ6Va3ucibkJ07T38.rcZ/Iz61c48MgRm0ODv6', 2, 4, 1),
('2018-09-10 17:24:53', '2018-10-20 14:04:17', '2010-05-21', 'dpfleger4@example.com', null, 'Dulce Pfleger', '$2a$10$EK7Wd/5kTniJ0wFl01zkVuAU.Ja5yu9LvCd9/8SCwsG.5mr4Li2ou', 3, 5, 1),
('2018-02-10 14:58:33', '2018-05-25 14:26:17', '1985-05-02', 'loakwood5@example.com', null, 'Lela Oakwood', '$2a$10$7zn19kqTV09C0e8shv8wBOmCrFQ2GMPyItBvvjniW3O8Enf30yING', 3, 6, 0),
('2018-01-18 08:59:56', '2018-04-18 17:12:06', null, 'kjoye6@example.com', null, 'Kitti Joye', '$2a$10$NIv/3CWCh4t7RzEgbFeWT./hIEgFQEtmHKgz5YyXQKK0ezIpRwh9K', 3, 7, 1),
('2018-01-16 17:04:14', '2018-05-16 19:37:22', '1995-12-29', 'egalliver7@example.com', null, 'Euphemia Galliver', '$2a$10$K33sDIo6WQ6gjWMwJddzL.xnmTHudr5.ckpG5Z29.80wLSJ7bJWim', 2, 8, 1),
('2018-02-17 22:37:19', '2018-03-17 23:33:03', null, 'okira0@example.com', null, 'Onfroi Kira', '$2a$10$lI8FFVHeiVyCBEV3dQCmSOrHAOsJRYTDC23rcSVNDh3usclJCk54S', 3, null, 0),
('2018-02-14 08:03:55', '2018-06-14 22:18:13', '2018-03-09', 'nhathwood1@example.com', null, 'Nicolle Hathwood', '$2a$10$Qm003NFCbAfG8B0sn.Y1luc3VbDtTCF/ktolGkYw3d.kNDZ5/y106', 1, null, 1);

INSERT INTO PUBLIC.TELEFONE (NUMERO, IS_WHATSAPP, USUARIO_ID) VALUES
('(16) 92289-2031', false, 9),
('(19) 98809-1455', true, 6),
('(56) 95060-5313', false, 9),
('(42) 95372-5735', false, 6),
('(47) 96989-9372', false, 8),
('(65) 95242-2496', true, 7),
('(81) 95777-3154', false, 1),
('(07) 98649-8202', false, 3),
('(09) 94314-8080', false, 1),
('(07) 90680-6466', false, 6),
('(06) 98014-9824', false, 9),
('(71) 96478-3863', false, 8),
('(89) 92399-8864', false, 7),
('(00) 93304-3338', false, 2),
('(91) 97019-7068', false, 1),
('(04) 91985-1627', true, 1),
('(69) 91676-4020', true, 10),
('(25) 99643-1119', false, 4),
('(30) 91833-1602', false, 10),
('(74) 96177-0719', false, 10),
('(07) 94580-1775', false, 2),
('(61) 91223-1970', false, 1),
('(90) 99053-4818', false, 2),
('(16) 91185-9877', false, 3),
('(54) 97647-3597', true, 10),
('(38) 98090-9231', false, 2),
('(75) 97837-0110', false, 10),
('(87) 92225-9566', false, 4),
('(74) 97368-6463', false, 4),
('(36) 98710-8248', false, 8),
('(69) 95786-1364', true, 7),
('(17) 93059-8685', false, 5),
('(80) 99955-9718', false, 3),
('(21) 91990-4222', false, 4),
('(84) 98945-3989', true, 7),
('(66) 95651-5080', false, 3),
('(64) 95515-5092', true, 3),
('(26) 94608-2857', false, 5),
('(18) 99920-1902', true, 9),
('(99) 96356-1420', true, 10),
('(02) 96517-7477', false, 6),
('(50) 92794-1789', false, 10);


INSERT INTO PET (CREATED_DATE, LAST_MODIFIED_DATE, CASTRACAO, CATEGORIA, COMPRIMENTO_PELO, DESCRICAO, NOME, PORTE, RACA, SEXO, TIPO, VACINACAO, DATA_NOTIFICACAO_DE_INATIVIDADE, ID_LOCALIZACAO, ID_USUARIO) VALUES
('2018-10-25 18:58:41', '2018-10-26 01:54:33', 1, 3, 4, 'Vivamus in felis eu sapien cursus vestibulum.', 'Arabelle', 1, 5, 3, 1, 1, null, null, 10),
('2018-10-24 18:09:41', '2018-10-24 18:35:46', 1, 2, 1, 'Curabitur convallis. Duis consequat dui nec nisi volutpat eleifend.', 'Wolfy', 3, 4, 3, 1, 1, null, null, 10),
('2018-10-24 02:45:47', '2018-10-24 18:07:05', 1, 3, 2, 'Fusce consequat. Nulla nisl. Nunc nisl.', 'Creighton', 1, 4, 3, 1, 1, null, 4, 10),
('2018-10-23 06:49:59', '2018-10-24 08:11:06', 1, 2, 1, 'Proin eu mi. Nulla ac enim. In tempor, turpis nec euismod scelerisque, quam turpis adipiscing lorem, vitae mattis nibh ligula nec sem.', null, 2, 5, 3, 1, 1, null, null, 9),
('2018-10-23 20:33:38', '2018-10-24 02:38:02', 1, 3, 3, null, null, 2, 4, 3, 1, 1, null, null, 2),
('2018-10-23 19:24:58', '2018-10-24 05:52:45', 1, 3, 1, 'Vestibulum rutrum rutrum neque. Aenean auctor gravida sem. Praesent id massa id nisl venenatis lacinia.', 'Isidro', 3, 7, 3, 2, 1, null, null, 5),
('2018-10-23 03:47:14', '2018-10-23 21:31:59', 1, 1, 1, 'Pellentesque viverra pede ac diam.', 'Julee', 1, 6, 3, 2, 1, null, null, 9),
('2018-10-23 20:08:20', '2018-10-24 03:06:22', 1, 2, 3, 'Morbi sem mauris, laoreet ut, rhoncus aliquet, pulvinar sed, nisl.', 'Merridie', 1, 7, 3, 2, 1, null, 3, 8),
('2018-10-26 13:23:29', '2018-10-26 13:49:13', 1, 3, 4, null, 'Itch', 3, 7, 3, 2, 1, null, 2, 9),
('2018-10-23 01:04:15', '2018-10-23 11:53:37', 1, 1, 2, 'Phasellus sit amet erat. Nulla tempus. Vivamus in felis eu sapien cursus vestibulum.', null, 1, 8, 3, 3, 1, null, null, 4),
('2018-10-26 12:37:41', '2018-10-27 06:58:54', 1, 2, 4, 'Ut at dolor quis odio consequat varius.', 'Julee', 3, 9, 3, 3, 1, null, null, 2);

INSERT INTO REGISTRO (ID_PET, DATA, SITUACAO) VALUES
(1, '2018-12-02 08:38:15', 1),
(1, '2018-12-03 16:17:45', 2),
(1, '2018-12-03 21:26:12', 3),
(9, '2018-12-04 05:08:41', 1),
(9, '2018-12-04 09:14:12', 2),
(9, '2018-12-04 14:21:59', 3),
(10, '2018-12-02 22:08:31', 1),
(10, '2018-12-04 13:44:54', 2),
(10, '2018-12-04 13:51:43', 3),
(7, '2018-12-01 05:33:40', 1),
(7, '2018-12-01 18:29:26', 2),
(7, '2018-12-04 09:30:47', 3),
(6, '2018-11-30 10:04:11', 1),
(6, '2018-12-01 12:56:46', 2),
(8, '2018-12-03 11:05:50', 1),
(8, '2018-12-04 11:10:45', 2),
(5, '2018-11-30 00:30:53', 1),
(5, '2018-11-30 22:09:43', 2),
(2, '2018-12-01 22:59:51', 1),
(3, '2018-12-02 23:32:35', 1),
(4, '2018-12-04 03:35:50', 1);

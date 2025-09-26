-- Patios
INSERT INTO patio (nome) VALUES
('Patio Central'),
('Patio Norte'),
('Patio Sul');

-- Setores
INSERT INTO setor (nome, capacidade_maxima, patio_id) VALUES
('Setor A', 50, 1),
('Setor B', 30, 1),
('Setor C', 40, 2),
('Setor D', 20, 3);

-- Motos
INSERT INTO moto (modelo, iot_identificador, data_entrada, setor_id) VALUES
('Honda CG 160', 'IOT001', NOW(), 1),
('Yamaha XTZ 250', 'IOT002', NOW(), 1),
('Suzuki Burgman', 'IOT003', NOW(), 2),
('Honda CB 500', 'IOT004', NOW(), 3),
('Kawasaki Ninja', 'IOT005', NOW(), 4);

-- PT-BR: Dados iniciais para desenvolvimento e demonstracao.
--        Simula uma fabrica de alimentos com 5 materias-primas e 3 produtos.
-- EN-US: Seed data for development and demonstration.
--        Simulates a food factory with 5 raw materials and 3 products.

-- Materias-primas (Raw Materials)
INSERT INTO raw_material (id, code, name, stock_quantity, unit) VALUES (1, 'MP001', 'Farinha de Trigo', 5000.0000, 'g');
INSERT INTO raw_material (id, code, name, stock_quantity, unit) VALUES (2, 'MP002', 'Acucar', 3000.0000, 'g');
INSERT INTO raw_material (id, code, name, stock_quantity, unit) VALUES (3, 'MP003', 'Manteiga', 2000.0000, 'g');
INSERT INTO raw_material (id, code, name, stock_quantity, unit) VALUES (4, 'MP004', 'Ovos', 50.0000, 'un');
INSERT INTO raw_material (id, code, name, stock_quantity, unit) VALUES (5, 'MP005', 'Leite', 4000.0000, 'ml');

-- Produtos (Products)
INSERT INTO product (id, code, name, sale_price) VALUES (1, 'PROD001', 'Bolo de Chocolate', 45.00);
INSERT INTO product (id, code, name, sale_price) VALUES (2, 'PROD002', 'Pao Caseiro', 12.00);
INSERT INTO product (id, code, name, sale_price) VALUES (3, 'PROD003', 'Biscoito Amanteigado', 25.00);

-- Composicao do Bolo de Chocolate (Chocolate Cake Composition)
INSERT INTO product_composition (id, product_id, raw_material_id, required_quantity) VALUES (1, 1, 1, 300.0000);
INSERT INTO product_composition (id, product_id, raw_material_id, required_quantity) VALUES (2, 1, 2, 200.0000);
INSERT INTO product_composition (id, product_id, raw_material_id, required_quantity) VALUES (3, 1, 3, 150.0000);
INSERT INTO product_composition (id, product_id, raw_material_id, required_quantity) VALUES (4, 1, 4, 3.0000);
INSERT INTO product_composition (id, product_id, raw_material_id, required_quantity) VALUES (5, 1, 5, 200.0000);

-- Composicao do Pao Caseiro (Homemade Bread Composition)
INSERT INTO product_composition (id, product_id, raw_material_id, required_quantity) VALUES (6, 2, 1, 500.0000);
INSERT INTO product_composition (id, product_id, raw_material_id, required_quantity) VALUES (7, 2, 4, 1.0000);
INSERT INTO product_composition (id, product_id, raw_material_id, required_quantity) VALUES (8, 2, 5, 150.0000);

-- Composicao do Biscoito Amanteigado (Butter Cookie Composition)
INSERT INTO product_composition (id, product_id, raw_material_id, required_quantity) VALUES (9, 3, 1, 200.0000);
INSERT INTO product_composition (id, product_id, raw_material_id, required_quantity) VALUES (10, 3, 2, 100.0000);
INSERT INTO product_composition (id, product_id, raw_material_id, required_quantity) VALUES (11, 3, 3, 250.0000);
INSERT INTO product_composition (id, product_id, raw_material_id, required_quantity) VALUES (12, 3, 4, 2.0000);

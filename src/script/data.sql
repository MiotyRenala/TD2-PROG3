INSERT INTO Dish VALUES(1, 'Salade Fraîche', 'START'),
(2, 'Poulet Grillé', 'MAIN'),
(3, 'Riz aux légumes', 'MAIN'),
(4, 'Gâteau au chocolat', 'DESSERT'),
(5, 'Salade de fruits', 'DESSERT');


INSERT INTO Ingredient VALUES(1, 'Laitue', 800.00, 'VEGETABLE', 1),
                             (2, 'Tomate', 600.00, 'VEGETABLE', 1),
                             (3, 'Poulet', 4500.00, 'ANIMAL', 2),
                             (4, 'Chocolat', 3000.00, 'OTHER', 4),
                             (5, 'Beurre', 2500.00, 'DAIRY', 4);


INSERT INTO DishIngredient VALUES(1, 1, 1, 0.20, 'KG'),
                                 (2, 1, 2, 0.15, 'KG'),
                                 (3, 2, 3, 1.00, 'KG'),
                                 (4, 4, 4, 0.30, 'KG'),
                                 (5, 4, 5, 0.20, 'KG');

UPDATE Dish SET selling_price = 3500.00 where id = 1;
UPDATE Dish SET selling_price = 12000.00 where id = 2;
UPDATE Dish SET selling_price = 8000.00 where id = 4;
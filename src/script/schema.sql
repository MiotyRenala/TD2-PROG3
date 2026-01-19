
create type ingredient_category as enum('VEGETABLE', 'ANIMAL', 'MARINE', 'DAIRY', 'OTHER');
create type type_of_Dishes as enum('START', 'MAIN', 'DESSERT');
create type unit_type as enum('PCS', 'KG', 'L');

CREATE TABLE Dish(
    id int primary key,
    name varchar(255),
    dish_type type_of_Dishes

);

CREATE TABLE Ingredient(
    id serial primary key,
    name varchar(255),
    price numeric (10,2),
    category ingredient_category,
    id_dish int,
    CONSTRAINT fk_dish FOREIGN KEY (id_dish) REFERENCES Dish(id)
);

CREATE TABLE DishIngredient(
    id serial primary key,
    id_dish int,
    id_ingredient int,
    quantity_required numeric(10,2),
    unit unit_type,
    CONSTRAINT fk_id_dish FOREIGN KEY (id_dish) REFERENCES Dish(id),
    CONSTRAINT fk_id_ingredient FOREIGN KEY (id_ingredient) REFERENCES Ingredient(id)
);

ALTER TABLE dish ADD COLUMN selling_price DOUBLE PRECISION;


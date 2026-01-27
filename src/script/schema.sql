
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
ALTER TABLE ingredient DROP COLUMN id_dish;

ALTER TABLE ingredient DROP COLUMN required_quantity;

create type  mouvement_type as enum('IN', 'OUT');

CREATE TABLE StockMovement(
    id serial primary key ,
    id_ingredient int,
    quantity numeric (10,2),
    type mouvement_type,
    unit unit_type,
    creation_datetime timestamp;
    CONSTRAINT fk_id_ingredient FOREIGN KEY (id_ingredient) REFERENCES Ingredient(id)

);


create type ingredient_category as enum('VEGETABLE', 'ANIMAL', 'MARINE', 'DAIRY', 'OTHER');
create type type_of_Dishes as enum('START', 'MAIN', 'DESSERT');


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


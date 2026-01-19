package school.hei;

import java.util.ArrayList;
import java.util.List;

public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum dishType;
    private List<Ingredient> ingredient;
    private double totalCost;

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Dish(Integer id, String name, DishTypeEnum dishType, List<Ingredient> ingredient, double totalCost) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.ingredient = ingredient;
        this.totalCost = totalCost;
    }

    public Dish(int id) {
        this.id = id;
    }

    public Dish(Integer id, String name, DishTypeEnum dishType) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
    }

    public Dish(Integer id, String name, DishTypeEnum dishType, List<Ingredient> ingredient ) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.ingredient = ingredient;
    }

    public Dish(Integer id, String nameDish, DishTypeEnum dishTypeEnum, List<Ingredient> ingredientfromId, Double requiredQuantity) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.ingredient = ingredient;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", ingredient=" + ingredient +
                '}';
    }

    public Double getDishCost(){
        Double totalCost = 0.00;
        for(Ingredient i: ingredient){
            if (i.getRequiredQuantity() == null) {
                throw new IllegalStateException(
                        "Quantité nécessaire inconnue pour l'ingrédient : "+ i.getName()
                );
            }
            totalCost += i.getPrice() * i.getRequiredQuantity();
        }
        return totalCost;
    };


    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DishTypeEnum getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeEnum dishType) {
        this.dishType = dishType;
    }

    public List<Ingredient> getIngredient() {
        return ingredient;
    }

    public void setIngredient(List<Ingredient> ingredient) {
        this.ingredient = ingredient;
    }
}

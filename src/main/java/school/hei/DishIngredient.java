package school.hei;

import java.util.ArrayList;
import java.util.List;

public class DishIngredient {
    private Integer id;
    private Dish dish;
    private Ingredient ingredient;
    private Double quantity_required;
    private UnitEnum unit;
    private List<DishIngredient> dishIngredients;

    public DishIngredient(Integer id, Dish dish, Ingredient ingredient, Double quantity_required, UnitEnum unit, List<DishIngredient> dishIngredients) {
        this.id = id;
        this.dish = dish;
        this.ingredient = ingredient;
        this.quantity_required = quantity_required;
        this.unit = unit;
        this.dishIngredients = dishIngredients;
    }

    public DishIngredient(Integer id, Dish dish, Ingredient ingredient, Double quantity_required, UnitEnum unit) {
        this.id = id;
        this.dish = dish;
        this.ingredient = ingredient;
        this.quantity_required = quantity_required;
        this.unit = unit;
    }

    public List<DishIngredient> getDishIngredients() {
        return dishIngredients;
    }

    public void setDishIngredients(List<DishIngredient> dishIngredients) {
        this.dishIngredients = dishIngredients;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public Double getQuantity_required() {
        return quantity_required;
    }

    public void setQuantity_required(Double quantity_required) {
        this.quantity_required = quantity_required;
    }

    public UnitEnum getUnit() {
        return unit;
    }

    public void setUnit(UnitEnum unit) {
        this.unit = unit;
    }
}


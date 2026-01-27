package school.hei;

import java.util.ArrayList;
import java.util.List;

public class Dish {
    private Integer id;
    private String name;
    private DishTypeEnum dishType;
    private Double sellingPrice;
    private List<DishIngredient> dishIngredients;
    private double totalCost;

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Double getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(Double sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Dish(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Dish(Integer id, String name, DishTypeEnum dishType, Double sellingPrice, List<DishIngredient> dishIngredients) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.sellingPrice = sellingPrice;
        this.dishIngredients = dishIngredients;
    }



    public Dish(Integer id, String name, DishTypeEnum dishType, List<DishIngredient> dishIngredients, double totalCost) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.totalCost = totalCost;
        this.dishIngredients = dishIngredients;
    }

    public Dish(int id) {

        this.id = id;
    }

    public Dish(Integer id, String name, DishTypeEnum dishType, Double selling_price) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.sellingPrice = selling_price;
    }

    public Dish(Integer id, String name, DishTypeEnum dishType, List<DishIngredient> dishIngredients ) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.dishIngredients = dishIngredients;
    }

    public Dish(Integer id, String nameDish, DishTypeEnum dishTypeEnum, List<DishIngredient> dishIngredients, Double requiredQuantity) {
        this.id = id;
        this.name = name;
        this.dishType = dishType;
        this.dishIngredients= dishIngredients;
    }

    public Double getDishCost(){
        Double cost = 0.00;
        for(DishIngredient dI : dishIngredients){
            if(dI == null){
                throw (new RuntimeException("NULL"));
            }
            else {
                cost += dI.getQuantity_required() * dI.getIngredient().getPrice();
            }
        }
        return cost;

    }

    public Double getGrossMargin(){
        Double margin = 0.00;
        Double cost = 0.00;
        for(DishIngredient dI : dishIngredients ){
            if(dI.getDish().sellingPrice == null){
                throw (new RuntimeException("NULL"));
            }
            else {
                cost += dI.getQuantity_required() * dI.getIngredient().getPrice();
                margin = dI.getDish().sellingPrice - cost;
            }
        }
       return margin;
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dishType=" + dishType +
                ", selling_price=" + sellingPrice +
                ", dishIngredient=" + dishIngredients +
                '}';
    }





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

}

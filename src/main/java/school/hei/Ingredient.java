package school.hei;


import java.time.Instant;
import java.util.List;

public class Ingredient {
    private Integer id;
    private String name;
    private double price;
    private CategoryEnum category;
    private List<DishIngredient> dishIngredients;
    private List<StockMovement> stockMovementList;


    public StockValue getStockValueAt(Instant t){
        return stockMovementList.getFirst().getValue();
    }

    public Ingredient(Integer id) {
        this.id = id;
    }

    public Ingredient(Integer id, String name) {
        this.id = id;
        this.name = name;
    }



    public Ingredient(Integer id, String name, double price, CategoryEnum category, List<DishIngredient> dishIngredients, List<StockMovement> stockMovementList) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.dishIngredients = dishIngredients;
        this.stockMovementList = stockMovementList;
    }

    public Ingredient(Integer id, String name, double price, CategoryEnum category, List<DishIngredient> dishIngredients) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.dishIngredients = dishIngredients;
    }


    public Ingredient(Integer id, String name, double price, CategoryEnum category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;

    }

    @Override
    public String toString() {
        return "Ingredient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", category=" + category +
                '}';
    }

    public List<StockMovement> getStockMovementList() {
        return stockMovementList;
    }

    public void setStockMovementList(List<StockMovement> stockMovementList) {
        this.stockMovementList = stockMovementList;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

}

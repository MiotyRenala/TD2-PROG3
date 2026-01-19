package school.hei;


public class Ingredient {
    private Integer id;
    private String name;
    private double price;
    private CategoryEnum category;
    private Dish dish;
    private Double requiredQuantity;



    public Ingredient(Integer id, String name, double price, CategoryEnum category, Dish dish, Double requiredQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.dish = dish;
        this.requiredQuantity = requiredQuantity;
    }


    public Ingredient(Integer id, String name, double price, CategoryEnum category, Double requiredQuantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.requiredQuantity = requiredQuantity;
    }

    public Ingredient(Integer id, String name, double price, CategoryEnum category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Ingredient(Integer id, String name, double price, CategoryEnum category, Dish dish) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.dish = dish;
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



    public String getDishName(){
        return dish.getName();
    }

    public Double getRequiredQuantity() {
        return requiredQuantity;
    }

    public void setRequiredQuantity(Integer required_quantity) {
        this.requiredQuantity = requiredQuantity;
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

    public Dish getDish() {
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }
}

package school.hei;


import java.time.Instant;

public class StockMovement {
    private Integer id;
    private Ingredient ingredient;
    private StockValue value;
    private MovementTypeEnum type;
    private Instant creationDateTime;


    public StockMovement(Integer id, Ingredient ingredient, StockValue value, MovementTypeEnum type, Instant creationDateTime) {
        this.id = id;
        this.ingredient = ingredient;
        this.value = value;
        this.type = type;
        this.creationDateTime = creationDateTime;
    }

    public StockMovement() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public StockValue getValue() {
        return value;
    }

    public void setValue(StockValue value) {
        this.value = value;
    }

    public MovementTypeEnum getType() {
        return type;
    }

    public void setType(MovementTypeEnum type) {
        this.type = type;
    }

    public Instant getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(Instant creationDateTime) {
        this.creationDateTime = creationDateTime;
    }
}

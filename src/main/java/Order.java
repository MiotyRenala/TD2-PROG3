import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class Order {
    private Integer id;
    private String reference;
    private Instant creationDatetime;
    private List<DishOrder> dishOrderList;

    public Order() {
    }

    public Order(Integer id, String reference, Instant creationDatetime) {
        this.id = id;
        this.reference = reference;
        this.creationDatetime = creationDatetime;
    }

    public Order(Integer id, String reference, Instant creationDatetime, List<DishOrder> dishOrderList) {
        this.id = id;
        this.reference = reference;
        this.creationDatetime = creationDatetime;
        this.dishOrderList = dishOrderList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Instant getCreationDatetime() {
        return creationDatetime;
    }

    public void setCreationDatetime(Instant creationDatetime) {
        this.creationDatetime = creationDatetime;
    }

    public List<DishOrder> getDishOrderList() {
        return dishOrderList;
    }

    public void setDishOrderList(List<DishOrder> dishOrderList) {
        this.dishOrderList = dishOrderList;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", creationDatetime=" + creationDatetime +
                ", dishOrderList=" + dishOrderList +
                " , Total Amount =" + getTotalAmountWithoutVat() +
                '}';
    }

    Double getTotalAmountWithoutVat() {
        Double amount = 0.0;
        for (DishOrder dO : dishOrderList) {
            amount += dO.getDish().getPrice();
        }
        return amount;

    }

    Double getTotalAmountWithVat() {
        Double amount = 0.0;
        for (DishOrder dO : dishOrderList) {
            amount += dO.getDish().getPrice() *( 1 + 0.2 );
        }
        return amount;


    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.id) && Objects.equals(reference, order.reference) && Objects.equals(creationDatetime, order.creationDatetime) && Objects.equals(dishOrderList, order.dishOrderList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reference, creationDatetime, dishOrderList);
    }
}

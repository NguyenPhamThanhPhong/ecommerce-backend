package ecommerce.api.entity.transaction;

import ecommerce.api.constants.OrderStatus;
import ecommerce.api.entity.base.EntityBase;
import ecommerce.api.entity.user.Customer;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class Order extends EntityBase<String> {
    private String address;
    private OrderStatus status;
    private int total;
    private Discount discount;

    private Customer customer;

    @OneToOne
    private Payment payment;

    @OneToMany
    private List<OrderDetail> details;
}

package ecommerce.api.entity.transaction;

import ecommerce.api.entity.base.EntityBase;

public class Payment extends EntityBase<String> {
    // NEED FOR VNPAY
    private int amount;
    private String bankCode;
    private String orderInfo;
    private String responseCode;
}

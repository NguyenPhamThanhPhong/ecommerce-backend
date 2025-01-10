package ecommerce.api.dto.projection;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VNPUpdateDTO {
    private BigDecimal amount;
    private String bankCode;
    private String cardMethod;
    private String orderInfo;
    private String transRef;
    private String secureHash;
    private String transNo;
}

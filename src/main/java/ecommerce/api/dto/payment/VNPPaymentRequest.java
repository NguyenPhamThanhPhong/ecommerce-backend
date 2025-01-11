package ecommerce.api.dto.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VNPPaymentRequest {
    @JsonProperty("vnp_Amount")
    private int amount;
    @JsonProperty("vnp_BankCode")
    private String bankCode;
    @JsonProperty("vnp_CardType")
    private String cardMethod;
    @JsonProperty("vnp_OrderInfo")
    private String orderInfo;
    @JsonProperty("vnp_TxnRef")
    private String transRef;
    @JsonProperty("vnp_SecureHash")
    private String secureHash;
    @JsonProperty("vnp_TransactionNo")
    private String transNo;
    @JsonProperty("vnp_ResponseCode")
    private String responseCode;
}

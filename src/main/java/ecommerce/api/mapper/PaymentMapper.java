package ecommerce.api.mapper;

import ecommerce.api.dto.payment.VNPPaymentRequest;
import ecommerce.api.dto.projection.VNPUpdateDTO;
import ecommerce.api.entity.transaction.payment.Payment;
import org.mapstruct.*;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
@MapperConfig(unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PaymentMapper {
    @Mapping(source = "amount", target = "amount", qualifiedByName = "intToBigDecimal")
    VNPUpdateDTO fromVNPCallbackToEntity(VNPPaymentRequest request);

    @Named("intToBigDecimal")
    default BigDecimal fromIntToBigDecimal(int amount) {
        return BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(100));
    }
}

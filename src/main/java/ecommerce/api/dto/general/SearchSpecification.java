package ecommerce.api.dto.general;

import ecommerce.api.constants.ChainType;
import ecommerce.api.constants.ComparisonType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class SearchSpecification {
    // AND or OR
    private ChainType joinCondition;
    // NOT condition or normal
    private boolean isOpposite;
    // Column name like product_name
    private String parameterName;
    // Comparison operator like equal, notEqual, like, less, greater, etc.
    private ComparisonType comparison;
    // Left value, usually for range checks (like BETWEEN)
    private Object value;

}
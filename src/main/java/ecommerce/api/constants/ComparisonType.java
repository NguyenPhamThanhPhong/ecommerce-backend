package ecommerce.api.constants;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.function.TriFunction;

@AllArgsConstructor
@Getter
public enum ComparisonType {
    EQUAL(CriteriaBuilder::equal),
    NOT_EQUAL(CriteriaBuilder::notEqual),
    NOT_LIKE((criteriaBuilder, expression, value)
            -> criteriaBuilder.notLike(criteriaBuilder.lower(expression.as(String.class)), "%" + value.toString().toLowerCase() + "%")),
    LIKE((criteriaBuilder, expression, value)
            -> criteriaBuilder.like(criteriaBuilder.lower(expression.as(String.class)), "%" + value.toString().toLowerCase() + "%")),
    LESS((criteriaBuilder, expression, value)
            -> criteriaBuilder.lessThan(expression.as((Class<Comparable>) value.getClass()), (Comparable) value)),
    GREATER((criteriaBuilder, expression, value) ->
            criteriaBuilder.greaterThan(expression.as((Class<Comparable>) value.getClass()), (Comparable) value)),
    LESS_OR_EQUAL((criteriaBuilder, expression, value)
            -> criteriaBuilder.lessThanOrEqualTo(expression.as((Class<Comparable>) value.getClass()), (Comparable) value)),
    GREATER_OR_EQUAL((criteriaBuilder, expression, value)
            -> criteriaBuilder.greaterThanOrEqualTo(expression.as((Class<Comparable>) value.getClass()), (Comparable) value)),
    IN((criteriaBuilder, expression, value)
            -> expression.in((Object[]) value)),
    NOT_IN((criteriaBuilder, expression, value)
            -> criteriaBuilder.not(expression.in((Object[]) value))),
    IS_NULL((criteriaBuilder, expression, value)
            -> criteriaBuilder.isNull(expression)),
    IS_NOT_NULL((criteriaBuilder, expression, value)
            -> criteriaBuilder.isNotNull(expression));

    private final TriFunction<CriteriaBuilder, Expression<?>, Object, Predicate> func;


    public static ComparisonType fromString(String comparison) {
        for (ComparisonType comparisonType : ComparisonType.values()) {
            if (comparisonType.name().equalsIgnoreCase(comparison)) {
                return comparisonType;
            }
        }
        return null;
    }

}

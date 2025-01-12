package ecommerce.api.utils;

import ecommerce.api.constants.ChainType;
import ecommerce.api.dto.general.SearchSpecification;
import ecommerce.api.exception.BadRequestException;
import jakarta.persistence.criteria.*;
import org.apache.commons.lang3.function.TriFunction;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class DynamicSpecificationUtils {
    public static Map<String, Function<String, ?>> parsers = Map.of(
            "string", (String str) -> str,
            "int", (String str) -> {
                try {
                    return Integer.parseInt(str);
                } catch (NumberFormatException e) {
                    return null;
                }
            },
            "long", (String str) -> {
                try {
                    return Long.parseLong(str);
                } catch (NumberFormatException e) {
                    return null;
                }
            },
            "bigDecimal", (String str) -> {
                try {
                    return BigDecimal.valueOf(Double.parseDouble(str));
                } catch (NumberFormatException e) {
                    return null;
                }
            },
            "bool", Boolean::parseBoolean,
            "date", (String str) -> {
                try {
                    SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    return isoFormat.parse(str);
                } catch (Exception e) {
                    throw new BadRequestException("Invalid date format");
                }
            },
            "uuid", (String str) -> {
                try {
                    return java.util.UUID.fromString(str);
                } catch (Exception e) {
                    throw new BadRequestException("Invalid UUID format");
                }
            },
            "milisecs", (String str) -> {
                try {
                    return new Date(Long.parseLong(str));
                } catch (Exception e) {
                    throw new BadRequestException("Invalid date format");
                }
            }
    );

    public static <T> Specification<T> buildSpecification(Set<SearchSpecification> specifications) {
        return (root, query, criteriaBuilder) -> {
            if (specifications == null || specifications.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Predicate combinedPredicate = criteriaBuilder.conjunction();
            for (SearchSpecification spec : specifications) {
                Predicate predicate = DynamicSpecificationUtils.createPredicate(spec, root, criteriaBuilder);

                if (ChainType.AND.equals(spec.getJoinCondition())) {
                    combinedPredicate = criteriaBuilder.and(combinedPredicate, predicate);
                } else if (ChainType.OR.equals(spec.getJoinCondition())) {
                    combinedPredicate = criteriaBuilder.or(combinedPredicate, predicate);
                }
            }
            return combinedPredicate;
        };
    }


    public static <T> Predicate createPredicate(SearchSpecification spec, Root<T> root, CriteriaBuilder criteriaBuilder) {
        Expression<?> expression;

        if (spec.getJoinEntity() != null) {
            expression = root.join(spec.getJoinEntity()).get(spec.getParameterName());
        } else {
            expression = root.get(spec.getParameterName());
        }

        TriFunction<CriteriaBuilder, Expression<?>, Object, Predicate> function = spec.getComparison().getFunc();

        if (function == null) {
            throw new UnsupportedOperationException("Unsupported comparison operation: " + spec.getComparison());
        }
        Object value = spec.getValue();
        if (spec.getType() != null) {
            value = parsers.get(spec.getType()).apply(spec.getValue());
        }
        Predicate predicate = function.apply(criteriaBuilder, expression, value);
        return spec.isOpposite() ? criteriaBuilder.not(predicate) : predicate;
    }
}
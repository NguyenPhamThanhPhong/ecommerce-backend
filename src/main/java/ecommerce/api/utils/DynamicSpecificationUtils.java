package ecommerce.api.utils;

import ecommerce.api.constants.ChainType;
import ecommerce.api.dto.general.SearchSpecification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.commons.lang3.function.TriFunction;
import org.springframework.data.jpa.domain.Specification;

import java.util.Set;

public class DynamicSpecificationUtils {
    public static <T> Specification<T> buildSpecification(Set<SearchSpecification> specifications) {
        return (root, query, criteriaBuilder) -> {
            Predicate combinedPredicate = null;

            for (SearchSpecification spec : specifications) {
                Predicate predicate = DynamicSpecificationUtils.createPredicate(spec, root, criteriaBuilder);

                // Apply NOT condition if isOpposite is true
                if (spec.isOpposite()) {
                    predicate = criteriaBuilder.not(predicate);
                }

                // Combine predicates based on join condition
                if (combinedPredicate == null) {
                    combinedPredicate = predicate;
                } else if (ChainType.AND.equals(spec.getJoinCondition())) {
                    combinedPredicate = criteriaBuilder.and(combinedPredicate, predicate);
                } else if (ChainType.OR.equals(spec.getJoinCondition())) {
                    combinedPredicate = criteriaBuilder.or(combinedPredicate, predicate);
                }
            }

            return combinedPredicate;
        };
    }

    public static <T> Predicate createPredicate(SearchSpecification spec, Root<T> root, CriteriaBuilder criteriaBuilder) {
        Expression<?> expression = root.get(spec.getParameterName());
        TriFunction<CriteriaBuilder, Expression<?>, Object, Predicate> function = spec.getComparison().getFunc();

        if (function == null) {
            throw new UnsupportedOperationException("Unsupported comparison operation: " + spec.getComparison());
        }

        Predicate predicate = function.apply(criteriaBuilder, expression, spec.getValue());
        return spec.isOpposite() ? criteriaBuilder.not(predicate) : predicate;
    }
}
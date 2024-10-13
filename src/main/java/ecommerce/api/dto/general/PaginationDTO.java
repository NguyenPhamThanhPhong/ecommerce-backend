package ecommerce.api.dto.general;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@Data
public class PaginationDTO<T> {
    private int totalInstances;
    private int totalPages;
    private int elementCounts;
    private List<T> data;

    public static <T> PaginationDTO<T> fromPage(Page<T> page){
        return PaginationDTO.<T>builder()
                .totalInstances((int) page.getTotalElements())
                .totalPages(page.getTotalPages())
                .elementCounts(page.getNumberOfElements())
                .data(page.getContent())
                .build();
    }
}

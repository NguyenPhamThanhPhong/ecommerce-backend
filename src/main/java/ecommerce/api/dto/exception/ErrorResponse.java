package ecommerce.api.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse<T> {
    private String path;
    private String message;
    private T data;
}

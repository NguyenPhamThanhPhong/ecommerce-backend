package ecommerce.api.entity.base;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;

@MappedSuperclass
@Data
public abstract class EntityBase<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected T id;

    @Column(name = "created_at")
    protected Instant createdAt;

//    @Column(name = "updated_at")
//    private Instant updatedAt;
}
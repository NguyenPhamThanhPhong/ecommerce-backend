package ecommerce.api.entity.base;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@MappedSuperclass
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class EntityBase<T> {
    @Id
    protected T id;

    @ColumnDefault("now()")
    @Column(name = "created_at", insertable = false, updatable = false)
    protected Date createdAt;

    @Column(name = "deleted_at")
    protected Date deletedAt;
}
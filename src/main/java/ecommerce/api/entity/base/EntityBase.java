package ecommerce.api.entity.base;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class EntityBase {
    @Id
    @Builder.Default
    protected UUID id = UUID.randomUUID();;

    @ColumnDefault("now()")
    @Column(name = "created_at", insertable = false, updatable = false)
    protected Date createdAt;

    @Column(name = "deleted_at")
    protected Date deletedAt;

}
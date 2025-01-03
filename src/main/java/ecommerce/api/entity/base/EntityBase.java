package ecommerce.api.entity.base;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.UUID;

@MappedSuperclass
@Data
@SuperBuilder
@AllArgsConstructor
public abstract class EntityBase {
    @Id
    @Builder.Default
    protected UUID id = UUID.randomUUID();

    @ColumnDefault("now()")
    @Column(name = "created_at", insertable = false, updatable = false)
    protected Date createdAt;

    @Column(name = "deleted_at")
    protected Date deletedAt;

    //in case we exceed the limit of 6 digits, we will use the base32 encoding
    @Column(name = "code",insertable = false, updatable = false)
    protected long code;

    public EntityBase() {
        this.id = UUID.randomUUID();
    }
}
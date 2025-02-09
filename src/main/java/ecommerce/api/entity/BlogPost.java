package ecommerce.api.entity;

import ecommerce.api.entity.base.EntityBase;
import ecommerce.api.entity.user.Profile;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Builder
@Entity
@Table(name = "blog_posts")
@DynamicInsert
@DynamicUpdate
public class BlogPost extends EntityBase {

    @ColumnDefault("''")
    @Column(name = "title", length = Integer.MAX_VALUE)
    private String title;

    @ColumnDefault("now()")
    @Column(name = "subtitle", length = Integer.MAX_VALUE)
    private String subtitle;

    @ColumnDefault("''")
    @Column(name = "image_url", length = Integer.MAX_VALUE)
    private String imageUrl;;

    @Column(name = "author_id")
    private UUID authorId;

    @ColumnDefault("''")
    @Column(name = "content", length = Integer.MAX_VALUE)
    private String content;

    @Column(name = "is_html")
    private Boolean isHtml;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private Profile author;

    public BlogPost() {
        this.id = UUID.randomUUID();
    }
}

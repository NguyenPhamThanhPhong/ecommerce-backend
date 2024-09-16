package ecommerce.api.entity;

import ecommerce.api.entity.base.EntityBase;
import jakarta.persistence.Column;

public class BlogPost extends EntityBase<String> {
    private String title;
    private String subtitle;


    @Column(columnDefinition = "text")
    private String content;


}

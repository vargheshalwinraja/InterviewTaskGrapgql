package com.allwin.graphql.model;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "producers")
@Data
public class Producers {
    @Id
    @Column(name = "id", nullable = false)
    @Type(type="uuid-char")
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

}

package com.github.qfood.management.domain.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "restaurant")
public class Restaurant extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String owner;
    public String documentID;
    public String name;

    @ManyToOne
    public Location location;

    @CreationTimestamp
    public Date dateCreatedAt;

    @UpdateTimestamp
    public Date dataUpdatedAt;
}

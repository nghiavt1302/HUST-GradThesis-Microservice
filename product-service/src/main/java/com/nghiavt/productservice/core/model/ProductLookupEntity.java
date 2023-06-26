package com.nghiavt.productservice.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
@Data
@Entity
@Table(name = "productlookup")
@AllArgsConstructor
@NoArgsConstructor
public class ProductLookupEntity implements Serializable {
    private static final long serialVersionUID = -64029926457180366L;
    @Id
    private String productId;
    @Column(unique = true)
    private String title;
}

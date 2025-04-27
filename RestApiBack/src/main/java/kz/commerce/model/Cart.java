package kz.commerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @Column(name = "prod_id")
    private int prodId;

    @Column(name = "quantity")
    private int quantity;

    public Cart(){};

    public Cart(int id){
        prodId = id;
    }

    public Cart(int id, int quantity){
        prodId = id;
        this.quantity = quantity;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

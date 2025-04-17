package isd.be.htc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "unit_price")
    private double unitPrice;

    @Column(name = "size")
    private String size;

    @Column(name = "sugar_rate")
    private String sugarRate;

    @Column(name = "ice_rate")
    private String iceRate;
}

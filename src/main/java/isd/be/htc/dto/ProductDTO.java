package isd.be.htc.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    private String description;
    private String image;
    private String ingredients;

    public ProductDTO(Long id, String name, Double price, String description, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
    }

    public ProductDTO(Long id, String name, Double price, String description, String image, String ingredients) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.ingredients = ingredients;
    }
}

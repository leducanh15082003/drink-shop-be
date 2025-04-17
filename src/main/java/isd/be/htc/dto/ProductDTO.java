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
    private String category;

    public ProductDTO(Long id, String name, Double price, String description, String image, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.category = category;
    }

    public ProductDTO(Long id, String name, Double price, String description, String image, String ingredients,
            String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.ingredients = ingredients;
        this.category = category;
    }
}

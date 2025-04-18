package isd.be.htc.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateProductDTO {
    private String name;
    private Double price;
    private String description;
    private String image;
    private String ingredients;
    private Long categoryId;

    public CreateProductDTO(String name, Double price, String description, String image, Long categoryId) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.categoryId = categoryId;
    }

    public CreateProductDTO(String name, Double price, String description, String image, String ingredients,
            Long categoryId) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.ingredients = ingredients;
        this.categoryId = categoryId;
    }
}

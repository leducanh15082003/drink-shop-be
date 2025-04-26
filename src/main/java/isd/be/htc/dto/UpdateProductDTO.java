package isd.be.htc.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProductDTO {
    private String name;
    private Double price;
    private String description;
    private String image;
    private String ingredients;
    private Long categoryId;
}

package isd.be.htc.dto;

import isd.be.htc.model.CartItem;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartDTO {
    private Long id;
    private List<CartItemDTO> items;

    public CartDTO(Long id, List<CartItemDTO> items) {
        this.id = id;
        this.items = items;
    }
}

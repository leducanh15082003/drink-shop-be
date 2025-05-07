package isd.be.htc.dto;

import isd.be.htc.model.User;
import isd.be.htc.model.enums.LoyaltyMember;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;
    private LoyaltyMember loyaltyMember;

    public UserDTO(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole().toString();
        this.loyaltyMember = user.getLoyaltyMember();
    }
}

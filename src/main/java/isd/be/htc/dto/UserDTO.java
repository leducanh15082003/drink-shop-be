package isd.be.htc.dto;

import isd.be.htc.model.User;
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

    public UserDTO(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.phoneNumber = user.getPhoneNumber();
        this.role = user.getRole().toString();
    }
}

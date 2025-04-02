package isd.be.htc.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDTO {
    private String fullName;
    private String phoneNumber;
    private String email;
    private String password;
}

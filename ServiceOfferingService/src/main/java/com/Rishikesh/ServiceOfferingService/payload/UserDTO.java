package com.Rishikesh.ServiceOfferingService.payload;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserDTO {

    private Long Id;

    private String fullname;

    private String email;
}

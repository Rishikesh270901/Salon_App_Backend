package com.Rishikesh.UserService.payload.dto;

import lombok.Data;

@Data
public class Credential {

    private String type;

    private String value;

    private Boolean temporary;
}

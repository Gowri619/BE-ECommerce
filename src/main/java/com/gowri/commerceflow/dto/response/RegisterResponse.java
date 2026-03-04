package com.gowri.commerceflow.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {

    private long id;
    private String name;
    private String email;
    private String role;
}

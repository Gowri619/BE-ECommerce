package com.gowri.commerceflow.service;

import com.gowri.commerceflow.dto.request.RegisterRequest;
import com.gowri.commerceflow.dto.response.RegisterResponse;

public interface UserService {
    RegisterResponse register(RegisterRequest request);
}

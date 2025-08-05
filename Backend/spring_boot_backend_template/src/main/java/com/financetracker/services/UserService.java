package com.financetracker.services;

import com.financetracker.dto.ApiResponse;
import com.financetracker.dto.UserReqDTO;

public interface UserService {
    ApiResponse signUp(UserReqDTO dto);
}

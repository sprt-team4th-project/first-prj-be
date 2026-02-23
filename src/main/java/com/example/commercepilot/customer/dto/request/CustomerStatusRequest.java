package com.example.commercepilot.customer.dto.request;

import com.example.commercepilot.customer.entity.CustomerStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CustomerStatusRequest {

    @NotNull(message = "상태는 필수입니다.")
    private CustomerStatus status;
}

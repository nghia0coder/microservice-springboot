package com.ecommercial.frontend.models.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class APIResponse {
    @Builder.Default
    boolean isIsSuccess = true;
    String message;
    Object result;
}

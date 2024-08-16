package com.ecommercial.frontend.models.request;

import com.ecommercial.frontend.ultilities.SD;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestDTO {
    SD.ApiType APItype = SD.ApiType.GET;
    String Url;
    Object Data;
    SD.ContentType ContentType = SD.ContentType.Json;
}

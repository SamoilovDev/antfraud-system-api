package com.samoilov.project.antifraud.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IpAddressDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Min(value = 1, message = "Id must be greater than 0")
    private Long id;

    private String ip;

}

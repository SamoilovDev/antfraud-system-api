package com.samoilov.project.antifraud.dto;

import com.samoilov.project.antifraud.enums.StateCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionPreparingInfoDto {

    private String ip;

    private StateCode stateCode;

}

package com.samoilov.project.antifraud.controller.interfaces;

import com.samoilov.project.antifraud.dto.CardNumberDto;
import com.samoilov.project.antifraud.dto.IpAddressDto;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Tag(name = "Antifraud api")
@RequestMapping("/api/antifraud")
public interface AntifraudApi {

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully saved suspicious ip address",
                    content = @Content(
                            schema = @Schema(implementation = IpAddressDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Wrong ip address"),
            @ApiResponse(responseCode = "409", description = "Ip address already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/suspicious-ip")
    ResponseEntity<IpAddressDto> saveSuspiciousIp(@Valid @RequestBody IpAddressDto ipAddressDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted suspicious ip address"),
            @ApiResponse(responseCode = "400", description = "Wrong ip address"),
            @ApiResponse(responseCode = "404", description = "Ip address not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/suspicious-ip/{ip}")
    ResponseEntity<Map<String, String>> deleteSuspiciousIp(@PathVariable String ip);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully got all suspicious ip addresses",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = IpAddressDto.class)
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/suspicious-ip")
    ResponseEntity<List<IpAddressDto>> getAllSuspiciousIp();

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully saved stolen card number",
                    content = @Content(
                            schema = @Schema(implementation = CardNumberDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Wrong card number"),
            @ApiResponse(responseCode = "409", description = "Card number already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/stolencard")
    ResponseEntity<CardNumberDto> saveStolenCard(@Valid @RequestBody CardNumberDto cardNumberDto);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted stolen card number"),
            @ApiResponse(responseCode = "400", description = "Wrong card number"),
            @ApiResponse(responseCode = "404", description = "Card number not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/stolencard/{cardNumber}")
    ResponseEntity<Map<String, String>> deleteStolenCard(@PathVariable String cardNumber);

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully got all stolen card numbers",
                    content = @Content(
                            array = @ArraySchema(
                                    schema = @Schema(implementation = CardNumberDto.class)
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/stolencard")
    ResponseEntity<List<CardNumberDto>> getAllStolenCards();

}

package com.samoilov.project.antifraud.controller.interfaces;

import com.samoilov.project.antifraud.dto.CardNumberDto;
import com.samoilov.project.antifraud.dto.IpAddressDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

@Api(tags = "Antifraud api")
@RequestMapping("/api/antifraud")
public interface AntifraudApi {

    @ApiOperation(value = "Save suspicious ip address")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully saved suspicious ip address", response = IpAddressDto.class),
            @ApiResponse(code = 400, message = "Wrong ip address", response = String.class),
            @ApiResponse(code = 409, message = "Ip address already exists", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)
    })
    @PostMapping("/suspicious-ip")
    ResponseEntity<IpAddressDto> saveSuspiciousIp(@Valid @RequestBody IpAddressDto ipAddressDto);

    @ApiOperation(value = "Delete suspicious ip address")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted suspicious ip address", response = String.class, responseContainer = "Map"),
            @ApiResponse(code = 400, message = "Wrong ip address", response = String.class),
            @ApiResponse(code = 404, message = "Ip address not found", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)
    })
    @DeleteMapping("/suspicious-ip/{ip}")
    ResponseEntity<Map<String, String>> deleteSuspiciousIp(@PathVariable String ip);

    @ApiOperation(value = "Get all suspicious ip addresses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully got all suspicious ip addresses", response = IpAddressDto.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)
    })
    @GetMapping("/suspicious-ip")
    ResponseEntity<List<IpAddressDto>> getAllSuspiciousIp();

    @ApiOperation(value = "Save stolen card number")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully saved stolen card number", response = CardNumberDto.class),
            @ApiResponse(code = 400, message = "Wrong card number", response = String.class),
            @ApiResponse(code = 409, message = "Card number already exists", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)
    })
    @PostMapping("/stolencard")
    ResponseEntity<CardNumberDto> saveStolenCard(@Valid @RequestBody CardNumberDto cardNumberDto);

    @ApiOperation(value = "Delete stolen card number")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully deleted stolen card number", response = String.class, responseContainer = "Map"),
            @ApiResponse(code = 400, message = "Wrong card number", response = String.class),
            @ApiResponse(code = 404, message = "Card number not found", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)
    })
    @DeleteMapping("/stolencard/{cardNumber}")
    ResponseEntity<Map<String, String>> deleteStolenCard(@PathVariable String cardNumber);

    @ApiOperation(value = "Get all stolen card numbers")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully got all stolen card numbers", response = CardNumberDto.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)
    })
    @GetMapping("/stolencard")
    ResponseEntity<List<CardNumberDto>> getAllStolenCards();

}

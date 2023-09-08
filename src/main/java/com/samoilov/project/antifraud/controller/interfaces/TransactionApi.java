package com.samoilov.project.antifraud.controller.interfaces;

import com.samoilov.project.antifraud.dto.FeedbackDto;
import com.samoilov.project.antifraud.dto.ResultDto;
import com.samoilov.project.antifraud.dto.TransactionDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Transaction api")
@RequestMapping("/api/antifraud")
public interface TransactionApi {

    @Operation(description = "Prepare transaction")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully prepared transaction",
                    content = @Content(
                            schema = @Schema(implementation = ResultDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Wrong transaction data"),
            @ApiResponse(responseCode = "404", description = "Max amounts not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/transaction")
    ResponseEntity<ResultDto> prepareTransaction(@Valid @RequestBody TransactionDto transactionDto);

    @Operation(description = "Add feedback")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully added feedback", content = @Content(
                    schema = @Schema(implementation = TransactionDto.class)
            )),
            @ApiResponse(responseCode = "400", description = "Wrong feedback data"),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "409", description = "Transaction already has feedback"),
            @ApiResponse(responseCode = "422", description = "Validity and Feedback payment states can not be equal"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/transaction")
    ResponseEntity<TransactionDto> addFeedback(@Valid @RequestBody FeedbackDto feedbackDto);

    @Operation(description = "Get full history")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully got full history",
                    content = @Content(
                            schema = @Schema(implementation = TransactionDto.class)
                    )
            ),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/history")
    ResponseEntity<List<TransactionDto>> getFullHistory();

    @Operation(description = "Get history by card number")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully got history by card number",
                    content = @Content(
                            schema = @Schema(implementation = TransactionDto.class)
                    )
            ),
            @ApiResponse(responseCode = "400", description = "Wrong card number"),
            @ApiResponse(responseCode = "404", description = "Transactions not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/history/{number}")
    ResponseEntity<List<TransactionDto>> getHistoryByCardNumber(@PathVariable String number);

}

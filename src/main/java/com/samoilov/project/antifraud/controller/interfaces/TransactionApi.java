package com.samoilov.project.antifraud.controller.interfaces;

import com.samoilov.project.antifraud.dto.FeedbackDto;
import com.samoilov.project.antifraud.dto.ResultDto;
import com.samoilov.project.antifraud.dto.TransactionDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Api(tags = "Transaction api")
@RequestMapping("/api/antifraud")
public interface TransactionApi {

    @ApiOperation(value = "Prepare transaction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully prepared transaction", response = ResultDto.class),
            @ApiResponse(code = 400, message = "Wrong transaction data", response = String.class),
            @ApiResponse(code = 404, message = "Max amounts not found", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)
    })
    @PostMapping("/transaction")
    ResponseEntity<ResultDto> prepareTransaction(@RequestBody @Valid TransactionDto transactionDto);

    @ApiOperation(value = "Add feedback")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully added feedback", response = TransactionDto.class),
            @ApiResponse(code = 400, message = "Wrong feedback data", response = String.class),
            @ApiResponse(code = 404, message = "Transaction not found", response = String.class),
            @ApiResponse(code = 409, message = "Transaction already has feedback", response = String.class),
            @ApiResponse(code = 422, message = "Validity and Feedback payment states can not be equal", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)
    })
    @PutMapping("/transaction")
    ResponseEntity<TransactionDto> addFeedback(@RequestBody @Valid FeedbackDto feedbackDto);

    @ApiOperation(value = "Get full history")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully got full history", response = TransactionDto.class),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)
    })
    @GetMapping("/history")
    ResponseEntity<List<TransactionDto>> getFullHistory();

    @ApiOperation(value = "Get history by card number")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully got history by card number", response = TransactionDto.class),
            @ApiResponse(code = 400, message = "Wrong card number", response = String.class),
            @ApiResponse(code = 404, message = "Transactions not found", response = String.class),
            @ApiResponse(code = 500, message = "Internal server error", response = String.class)
    })
    @GetMapping("/history/{number}")
    ResponseEntity<List<TransactionDto>> getHistoryByCardNumber(@PathVariable String number);

}

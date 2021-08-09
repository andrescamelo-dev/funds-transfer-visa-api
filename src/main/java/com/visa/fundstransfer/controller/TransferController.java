package com.visa.fundstransfer.controller;


import com.visa.fundstransfer.FundsTransferVisaApiApplication;
import com.visa.fundstransfer.domain.dto.TransferDTO;
import com.visa.fundstransfer.service.TransferService;
import org.springframework.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value=FundsTransferVisaApiApplication.PATH_SERVICE)
public class TransferController {
    @Autowired
    TransferService transferService;

    @ApiOperation(value = "Operation to create a funds transfer", response = ResponseEntity.class)
    @PostMapping(value = "/transferFunds", produces = { MediaType.APPLICATION_JSON_VALUE })
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Object transferFunds(@RequestBody TransferDTO transfer, @ApiParam(value = "Use Bearer token type ej: Bearer eyJhbGciO...", required = true) @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        return transferService.transferFunds(transfer);
    }

    
}


package com.visa.fundstransfer.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.visa.fundstransfer.domain.dto.AuthDTO;
import com.visa.fundstransfer.domain.dto.LoginDTO;
import com.visa.fundstransfer.domain.dto.TransferDTO;
import com.visa.fundstransfer.service.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class TransferControllerTests {

        private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

        @Value("${funds-transfer.rate-exchange.currency.exchangeable}")
        private String fundsTransferRateExchangeCurrencyExchangeable;

        @Autowired
        private MockMvc mvc;

        @Autowired
        private ObjectMapper objectMapper;

        MvcResult authResult;
        AuthDTO auth;

        @BeforeEach
        void setUp() {
                try {
                        authResult = mvc.perform(MockMvcRequestBuilders.post("/authenticate")
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .content(objectMapper
                                                        .writeValueAsString(new LoginDTO("tranferUser", "FT6HJ65&"))))
                                        .andExpect(status().isOk()).andExpect(jsonPath("$.jwt").exists()).andReturn();
                        auth = objectMapper.readValue(authResult.getResponse().getContentAsString(), AuthDTO.class);
                } catch (JsonProcessingException e) {
                        logger.error(e.getMessage(), e);
                } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                }

        }

        @Test
        public void whenDoTransferFunds_thenShouldReturnErrorEmptyParameters() throws Exception {
                TransferDTO transfer = new TransferDTO();
                transfer.setAmount(0d);
                transfer.setCurrency("");
                transfer.setDescription("");
                transfer.setOriginAccount(0);

                mvc.perform(MockMvcRequestBuilders.post("/1.0.0/transferFunds").contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ".concat(auth.getJwt()))
                                .content(objectMapper.writeValueAsString(transfer))).andExpect(status().isOk())
                                .andExpect(jsonPath("$.errorCode").exists()).andExpect(jsonPath("$.errors").exists())
                                .andReturn();

        }

        @Test
        public void whenDoTransferFunds_thenShouldReturnErrorInsufficientFunds() throws Exception {
                TransferDTO transfer = new TransferDTO();
                transfer.setAmount(800000d);
                transfer.setCurrency(fundsTransferRateExchangeCurrencyExchangeable);
                transfer.setDescription("Test InsufficientFunds");
                transfer.setOriginAccount(345622);
                transfer.setDestinationAccount(456337);

                mvc.perform(MockMvcRequestBuilders.post("/1.0.0/transferFunds").contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ".concat(auth.getJwt()))
                                .content(objectMapper.writeValueAsString(transfer))).andExpect(status().isOk())
                                .andExpect(jsonPath("$.errorCode").exists()).andExpect(jsonPath("$.errors").exists())
                                .andReturn();

        }

        @Test
        public void whenDoTransferFunds_thenShouldReturnErrorMaxDailyTransfer() throws Exception {
                TransferDTO transfer = new TransferDTO();
                transfer.setAmount(99d);
                transfer.setCurrency(fundsTransferRateExchangeCurrencyExchangeable);
                transfer.setDescription("Test AmountLessThan");
                transfer.setOriginAccount(345622);
                transfer.setDestinationAccount(456337);

                mvc.perform(MockMvcRequestBuilders.post("/1.0.0/transferFunds").contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ".concat(auth.getJwt()))
                                .content(objectMapper.writeValueAsString(transfer))).andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.taxCollected").exists())
                                .andExpect(jsonPath("$.cad").exists()).andReturn();

                mvc.perform(MockMvcRequestBuilders.post("/1.0.0/transferFunds").contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ".concat(auth.getJwt()))
                                .content(objectMapper.writeValueAsString(transfer))).andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.taxCollected").exists())
                                .andExpect(jsonPath("$.cad").exists()).andReturn();

                mvc.perform(MockMvcRequestBuilders.post("/1.0.0/transferFunds").contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ".concat(auth.getJwt()))
                                .content(objectMapper.writeValueAsString(transfer))).andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.taxCollected").exists())
                                .andExpect(jsonPath("$.cad").exists()).andReturn();

                mvc.perform(MockMvcRequestBuilders.post("/1.0.0/transferFunds").contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ".concat(auth.getJwt()))
                                .content(objectMapper.writeValueAsString(transfer))).andExpect(status().isOk())
                                .andExpect(jsonPath("$.errorCode").exists()).andExpect(jsonPath("$.errors").exists())
                                .andReturn();

        }

        @Test
        public void whenDoTransferFunds_thenShouldReturnOkWithAmountBiggerThan() throws Exception {
                TransferDTO transfer = new TransferDTO();
                transfer.setAmount(101d);
                transfer.setCurrency(fundsTransferRateExchangeCurrencyExchangeable);
                transfer.setDescription("Test AmountBiggerThan");
                transfer.setOriginAccount(345622);
                transfer.setDestinationAccount(456337);

                mvc.perform(MockMvcRequestBuilders.post("/1.0.0/transferFunds").contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ".concat(auth.getJwt()))
                                .content(objectMapper.writeValueAsString(transfer))).andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.taxCollected").exists())
                                .andExpect(jsonPath("$.cad").exists()).andReturn();

        }

        @Test
        public void whenDoTransferFunds_thenShouldReturnOkWithAmountLessThan() throws Exception {
                TransferDTO transfer = new TransferDTO();
                transfer.setAmount(99d);
                transfer.setCurrency(fundsTransferRateExchangeCurrencyExchangeable);
                transfer.setDescription("Test AmountLessThan");
                transfer.setOriginAccount(345622);
                transfer.setDestinationAccount(456337);

                mvc.perform(MockMvcRequestBuilders.post("/1.0.0/transferFunds").contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer ".concat(auth.getJwt()))
                                .content(objectMapper.writeValueAsString(transfer))).andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").exists()).andExpect(jsonPath("$.taxCollected").exists())
                                .andExpect(jsonPath("$.cad").exists()).andReturn();

        }

}

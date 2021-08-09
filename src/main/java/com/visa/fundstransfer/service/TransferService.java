package com.visa.fundstransfer.service;

import java.time.LocalDate;
import java.util.Date;

import com.visa.fundstransfer.common.ExceptionTypesEnum;
import com.visa.fundstransfer.common.Utils;
import com.visa.fundstransfer.convert.TransferConverter;
import com.visa.fundstransfer.domain.dto.TransferDTO;
import com.visa.fundstransfer.domain.dto.TransferResponseDTO;
import com.visa.fundstransfer.domain.model.Account;
import com.visa.fundstransfer.domain.model.TransferTrack;
import com.visa.fundstransfer.domain.repository.TransferAuditRepository;
import com.visa.fundstransfer.exception.FundsTransferException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TransferService {

    @Value("${funds-transfer.account.maximum-daily-transfers}")
    private Integer fundsTransferAccountMaximumDailyTransfers;

    @Value("${funds-transfer.rate-exchange.currency.locale}")
    private String fundsTransferRateExchangeCurrencyLocale;

    @Value("${funds-transfer.rate-exchange.currency.exchangeable}")
    private String fundsTransferRateExchangeCurrencyExchangeable;

    private static final Logger logger = LoggerFactory.getLogger(TransferService.class);

    @Autowired
    AccountService accountService;

    @Autowired
    RateExchangeService rateExchangeService;
    @Autowired
    TransferAuditRepository transferAuditRepository;

    @Autowired
    Utils util;

    private Account originAccount;
    private Account destinationAccount;
    private TransferTrack transferTrack;

    public Object transferFunds(TransferDTO transfer) {

        logger.info("Transfering funds: {} ", transfer);
        try {
           // validing paramters
        valideParameters(transfer);
            // Getting accounts data
            originAccount = accountService.getAccount(transfer.getOriginAccount());
            destinationAccount = accountService.getAccount(transfer.getDestinationAccount());
            // calculate data transfer
            Date transferDate = Utils.convertLocalDateToDate(LocalDate.now());
            Double localeRateExchange = rateExchangeService.getLocaleRateExchange(transfer.getCurrency(),
                    Utils.formatDate(transferDate, "yyyy-mm-dd"));
            Double localeTransferAmount = transfer.getAmount() * localeRateExchange;
            Double localeTransferTax = util.calculateLocaleTransferTax(transfer.getAmount());
            Double originNewBalance = originAccount.getBalance() - (localeTransferAmount + localeTransferTax);
            Double destinationNewBalance = destinationAccount.getBalance() + transfer.getAmount();
            // Validing bussines rules
            validInsufficientFunds(localeTransferAmount, localeTransferTax);
            validNumberTransaction(originAccount.getCurrentTransferOperations());
            // Save change accounts
            accountService.updateOriginAccountTransfer(originAccount, originNewBalance, transferDate);
            accountService.updateBalanceAccount(destinationAccount, destinationNewBalance);
            transferTrack = TransferConverter.convertTransferToTransferTrack(originAccount, destinationAccount,
                    transfer, originNewBalance, fundsTransferRateExchangeCurrencyLocale, transferDate,
                    localeRateExchange);
            transferAuditRepository.save(transferTrack);
            return TransferResponseDTO.builder().id(transferTrack.getId().toString()).CAD(localeRateExchange.toString())
                    .taxCollected(localeTransferTax.toString()).build();
        } catch (FundsTransferException ftex) {
            logger.error(" Controled error  transfering funds: {}", ftex.getExceptionTypesEnum().getDescription());
            return Utils.errorResponse(ftex.getExceptionTypesEnum().getCode(),
                    ftex.getExceptionTypesEnum().getDescription());
        } catch (Exception ex) {
            logger.error(" General error transfering funds:: {}", ex.getMessage());
            return Utils.errorResponse(ExceptionTypesEnum.GENERAL_ERROR.getCode(),
                    ExceptionTypesEnum.GENERAL_ERROR.getDescription());
        }
    }

    public void validInsufficientFunds(double localeTransferAmount, double localeTransferTax)
            throws FundsTransferException {
        Double finalBalance = localeTransferAmount + localeTransferTax;
        finalBalance = originAccount.getBalance() - finalBalance;

        if (finalBalance < 0) {
            throw new FundsTransferException(ExceptionTypesEnum.ACCOUNT_INSUFFICIENT_FUNDS);
        }
    }

    public void validNumberTransaction(Integer currentOperationsAccount) throws FundsTransferException {
        Integer numberTransactions = currentOperationsAccount + 1;
        if (numberTransactions > fundsTransferAccountMaximumDailyTransfers) {
            throw new FundsTransferException(ExceptionTypesEnum.ACCOUNT_MAX_DAILY_TRANSFERS_EXCEEDED);
        }
    }

    private void valideParameters(TransferDTO transfer) throws FundsTransferException {
        Double zero = 0d;
        if (transfer == null || transfer.getAmount() == null || transfer.getAmount().equals(zero)
                || transfer.getOriginAccount() == null || transfer.getOriginAccount().equals(0) || transfer.getDestinationAccount().equals(0)  || transfer.getCurrency().isBlank() || transfer.getDescription().isBlank() ) {
                    throw new FundsTransferException(ExceptionTypesEnum.EMPTY_PARAMETERS);
        }
    }
}

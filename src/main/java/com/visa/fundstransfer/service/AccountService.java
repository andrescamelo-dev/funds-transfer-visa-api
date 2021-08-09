package com.visa.fundstransfer.service;

import java.util.Date;

import com.visa.fundstransfer.common.ExceptionTypesEnum;
import com.visa.fundstransfer.common.Utils;
import com.visa.fundstransfer.domain.model.Account;
import com.visa.fundstransfer.domain.repository.AccountRepository;
import com.visa.fundstransfer.exception.DefaultFundsTransferException;
import com.visa.fundstransfer.exception.FundsTransferException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    AccountRepository accountRepository;

    public Account getAccount(Integer accountNumber) throws FundsTransferException {

        Account account = accountRepository.findByNumber(accountNumber);

        if (account != null) {
            logger.info("Account information: {} ", account);
            return account;
        } else {
            logger.error("Error coount no found: {} ", accountNumber);
            throw new FundsTransferException(ExceptionTypesEnum.ACCOUNT_NOT_FOUND);
        }
    }

    public Account updateBalanceAccount(Account account, Double douNewBalance) throws FundsTransferException {
        logger.info("Updating balance  account: {} ", account);
        account.setBalance(douNewBalance);
        return accountRepository.save(account);
    }

    public Account updateOriginAccountTransfer(Account account, Double balance, Date transferDate)
            throws FundsTransferException {

        logger.info("Tracking transfer: {} ", account);
        try {
            String lastTransferDate = Utils.formatDate(account.getLastTransferDate(), "yyyy-mm-dd");
            String transferDateSt = Utils.formatDate(transferDate, "yyyy-mm-dd");
            Integer currentTransferOperationsTmp = 0;

            if (account.getCurrentTransferOperations() != null && lastTransferDate.equals(transferDateSt)) {
                currentTransferOperationsTmp = account.getCurrentTransferOperations();
            }

            account.setBalance(balance);
            account.setCurrentTransferOperations(currentTransferOperationsTmp + 1);
            account.setLastTransferDate(transferDate);
            accountRepository.save(account);
            logger.info("Saved transfer: {} ", account);
            return account;
        } catch (Exception ex) {
            logger.error("Error tracking transfer {} ", account);
            throw new DefaultFundsTransferException(ex.getMessage(), ex);
        }
    }

}

package com.visa.fundstransfer.convert;

import java.util.Date;

import com.visa.fundstransfer.domain.dto.TransferDTO;
import com.visa.fundstransfer.domain.model.Account;
import com.visa.fundstransfer.domain.model.TransferTrack;

public class TransferConverter {

    public static TransferTrack convertTransferToTransferTrack(Account originAccount, Account accountDestination,
            TransferDTO transfer, Double originNewBalance,String localeCurrency, Date transferDate, Double rateExchange) {
        return TransferTrack.builder().originAccount(originAccount.getNumber())
                .originPreviousBalance(originAccount.getBalance()).originCurrency(transfer.getCurrency())
                .originCurrencyAmount(transfer.getAmount()).originNewBalance(originNewBalance)
                .destinationAccount(accountDestination.getNumber())
                .destinationPreviousBalance(accountDestination.getBalance()).description(transfer.getDescription())
                .localeCurrency(localeCurrency).transferDate(transferDate).rateExchange(rateExchange).build();
    }
}

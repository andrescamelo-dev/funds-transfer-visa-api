package com.visa.fundstransfer.domain.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Entity
@Table(name="transfer_track")
@Data
@Builder
public class TransferTrack implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "origin_account")
    private Integer originAccount;

    @Column(name = "destination_account")
    private Integer destinationAccount;

    @Column(name = "description")
    private String description;

    @Column(name = "origin_currency")
    private String originCurrency;

    @Column(name = "origin_currency_amount")
    private Double originCurrencyAmount;

    @Column(name = "transfer_date")
    private Date transferDate;

    @Column(name = "rate_exchange")
    private Double rateExchange;

    @Column(name = "locale_currency")
    private String localeCurrency;

    @Column(name = "origin_previous_balance")
    private Double originPreviousBalance;

    @Column(name = "origin_new_balance")
    private Double originNewBalance;

    @Column(name = "destination_previous_balance")
    private Double destinationPreviousBalance;

    @Column(name = "destination_new_balance")
    private Double destinationNewBalance;

   
}

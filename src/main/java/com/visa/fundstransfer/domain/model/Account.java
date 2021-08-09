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
@Table(name="account")
@Data
public class Account implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
  
    @Column(name = "number")
    private Integer number;

    @Column(name = "balance")
    private Double balance;

    @Column(name = "current_transfer_operations")
    private Integer currentTransferOperations;

    @Column(name = "last_transfer_date")
    private Date lastTransferDate;


}

package com.phoenix.phoenixbankapp.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "topup")
public class TopUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userId;
    private BigDecimal amount;
    private BigDecimal debtAmount;
    private String debtToUser;
    private BigDecimal transferAmount;

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public BigDecimal getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(BigDecimal debtAmount) {
        this.debtAmount = debtAmount;
    }

    public String getDebtToUser() {
        return debtToUser;
    }

    public void setDebtToUser(String debtToUser) {
        this.debtToUser = debtToUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "TopUp{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", amount=" + amount +
                ", debtAmount=" + debtAmount +
                ", debtToUser='" + debtToUser + '\'' +
                ", transferAmount=" + transferAmount +
                '}';
    }
}

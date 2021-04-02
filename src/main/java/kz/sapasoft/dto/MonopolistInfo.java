package kz.sapasoft.dto;

import java.util.List;

public class MonopolistInfo {

    public Long id;

    public String name;

    public Long balance;

    public Boolean isBank;

    public MonopolistInfo bank;

    public List<TransactionInfo> transactions;
}

package kz.sapasoft.dto;

public interface TransactionInfo {
    Long getFrom_id();
    Long getTo_id();
    Long getAmount();
    String getFrom_name();
    String getTo_name();
}

package kz.sapasoft.mapper;

import java.util.ArrayList;
import kz.sapasoft.domain.Monopolist;
import kz.sapasoft.domain.Transaction;
import kz.sapasoft.dto.MonopolistInfo;
import kz.sapasoft.web.rest.vm.TransactionVM;

public class MonopolyMapper {

    public static MonopolistInfo toInfo(Monopolist mon) {
        MonopolistInfo i = new MonopolistInfo();
        i.id = mon.getId();
        i.name = mon.getName();
        i.balance = mon.getBalance();
        i.isBank = mon.getIs_bank();
        i.transactions = new ArrayList<>(0);
        return i;
    }

    public static Transaction toTransaction(TransactionVM vm) {
        Transaction t = new Transaction();
        t.setFrom_id(vm.fromId);
        t.setTo_id(vm.toId);
        t.setAmount(vm.amount);
        return t;
    }
}

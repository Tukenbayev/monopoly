package kz.sapasoft.repository;

import java.util.List;
import java.util.Optional;
import kz.sapasoft.domain.Transaction;
import kz.sapasoft.dto.TransactionInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Transaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(
        value = "select t.from_id, t.to_id, t.amount, m1.name as from_name, m2.name as to_name from transaction t " +
        " left join monopolist m1 on m1.id = t.from_id " +
        " left join monopolist m2 on m2.id = t.to_id " +
        "where t.from_id = ?1 or t.to_id = ?1 order by t.id desc limit 10",
        nativeQuery = true
    )
    List<TransactionInfo> getMonopolistTransactions(Long id);

    @Query(
        value = "select t.from_id, t.to_id, t.amount, m1.name as from_name, m2.name as to_name from transaction t " +
        " left join monopolist m1 on m1.id = t.from_id " +
        " left join monopolist m2 on m2.id = t.to_id " +
        "where t.id = ?1 limit 1",
        nativeQuery = true
    )
    TransactionInfo getInfoById(Long id);
}

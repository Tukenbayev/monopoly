package kz.sapasoft.repository;

import java.util.List;
import kz.sapasoft.domain.Monopolist;
import kz.sapasoft.dto.ListItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Monopolist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MonopolistRepository extends JpaRepository<Monopolist, Long> {
    @Modifying
    @Query(
        value = "update monopolist set name = CAST(id AS VARCHAR), balance = 15000000, is_active=FALSE, " + "is_bank=FALSE WHERE id < 1818",
        nativeQuery = true
    )
    void dropToDefault();

    @Modifying
    @Query(value = "update monopolist set balance = 15000000, is_active = TRUE WHERE id = 1818", nativeQuery = true)
    void dropBankToDefault();

    @Query(value = "select count(id) > 0 from monopolist where is_bank = TRUE", nativeQuery = true)
    boolean isBankAlreadyExists();

    @Query(value = "select id, name from monopolist where id <> ?1 and is_active = TRUE order by name", nativeQuery = true)
    List<ListItem> getListOfMonopolist(Long except);

    @Modifying
    @Query(value = "update monopolist set balance = balance - ?2 where id = ?1", nativeQuery = true)
    void updateBalanceFrom(Long fromId, Long amount);

    @Modifying
    @Query(value = "update monopolist set balance = balance + ?2 where id = ?1", nativeQuery = true)
    void updateBalanceTo(Long toId, Long amount);
}

package kz.sapasoft.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Transaction.
 */
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "from_id", nullable = false)
    private Long from_id;

    @NotNull
    @Column(name = "to_id", nullable = false)
    private Long to_id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Long amount;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Transaction id(Long id) {
        this.id = id;
        return this;
    }

    public Long getFrom_id() {
        return this.from_id;
    }

    public Transaction from_id(Long from_id) {
        this.from_id = from_id;
        return this;
    }

    public void setFrom_id(Long from_id) {
        this.from_id = from_id;
    }

    public Long getTo_id() {
        return this.to_id;
    }

    public Transaction to_id(Long to_id) {
        this.to_id = to_id;
        return this;
    }

    public void setTo_id(Long to_id) {
        this.to_id = to_id;
    }

    public Long getAmount() {
        return this.amount;
    }

    public Transaction amount(Long amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return id != null && id.equals(((Transaction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", from_id=" + getFrom_id() +
            ", to_id=" + getTo_id() +
            ", amount=" + getAmount() +
            "}";
    }
}

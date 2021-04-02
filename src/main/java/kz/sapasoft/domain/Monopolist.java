package kz.sapasoft.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Monopolist.
 */
@Entity
@Table(name = "monopolist")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Monopolist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "balance")
    private Long balance;

    @Column(name = "is_bank")
    private Boolean is_bank;

    @Column(name = "is_active")
    private Boolean is_active;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Monopolist id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Monopolist name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBalance() {
        return this.balance;
    }

    public Monopolist balance(Long balance) {
        this.balance = balance;
        return this;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Boolean getIs_bank() {
        return this.is_bank;
    }

    public Monopolist is_bank(Boolean is_bank) {
        this.is_bank = is_bank;
        return this;
    }

    public void setIs_bank(Boolean is_bank) {
        this.is_bank = is_bank;
    }

    public Boolean getIs_active() {
        return this.is_active;
    }

    public Monopolist is_active(Boolean is_active) {
        this.is_active = is_active;
        return this;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Monopolist)) {
            return false;
        }
        return id != null && id.equals(((Monopolist) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Monopolist{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", balance=" + getBalance() +
            ", is_bank='" + getIs_bank() + "'" +
            ", is_active='" + getIs_active() + "'" +
            "}";
    }
}

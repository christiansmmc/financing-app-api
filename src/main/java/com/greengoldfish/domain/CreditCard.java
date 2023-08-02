package com.greengoldfish.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;

@Entity
@Table(name = "credit_card")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@ToString
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Getter
@Setter
public class CreditCard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "identifier")
    private String identifier;

    @NotNull
    @Min(1)
    @Max(31)
    @Column(name = "best_purchase_day")
    private Integer bestPurchaseDay;

    @NotNull
    @ManyToOne
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreditCard that = (CreditCard) o;

        if (!id.equals(that.id)) return false;
        if (!identifier.equals(that.identifier)) return false;
        if (!bestPurchaseDay.equals(that.bestPurchaseDay)) return false;
        return user.equals(that.user);
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + identifier.hashCode();
        result = 31 * result + bestPurchaseDay.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }
}

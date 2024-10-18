package fpt.swp.WorkSpace.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "wallet")
@NoArgsConstructor
@Getter
@Setter
public class Wallet {
    @Id
    @Column(name = "wallet_id", length = 36, nullable = false)
    private String walletId;

    @Column(name = "Amount",nullable = false)
    private float amount;

    @OneToOne(mappedBy = "wallet")
    @JsonBackReference
    private Customer customer;
}

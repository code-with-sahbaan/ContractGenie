package code.with.sahbaan.contractgenie.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "CONTRACT")
@Entity
@Getter
@Setter
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "CONTRACT_ID")
    private long contractId;

    @Column(name = "CONTRACT_NAME")
    private String contractName;

    @Column(name = "CONTRACT_URL")
    private String contractUrl;

    @Column(name = "CONTRACT_INSIGHTS" , length = 65535)
    private String contractInsights;

    @ManyToOne(fetch = FetchType.LAZY)
    private Folder folder;

}

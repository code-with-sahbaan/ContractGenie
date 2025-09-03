package code.with.sahbaan.contractgenie.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name = "FOLDER")
@Entity
@Getter
@Setter
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "FOLDER_ID")
    private long folderId;

    @Column(name = "FOLDER_NAME")
    private String folderName;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @OneToMany(
            mappedBy = "folder",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Contract> contracts = new ArrayList<>();

}

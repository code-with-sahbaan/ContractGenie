package code.with.sahbaan.contractgenie.Repositories;

import code.with.sahbaan.contractgenie.Entities.Contract;
import code.with.sahbaan.contractgenie.ResponseDTO.GetContractsResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract,Long> {

    @Query("SELECT NEW code.with.sahbaan.contractgenie.ResponseDTO.GetContractsResponse(" +
            "c.contractId, " +
            "c.contractName, " +
            "c.contractUrl, " +
            "c.folder.folderId) " +
            "FROM Contract c " +
            "WHERE c.folder.folderId = :folderId")
    List<GetContractsResponse> getContractsByFolderId(@Param("folderId") long folderId);
}

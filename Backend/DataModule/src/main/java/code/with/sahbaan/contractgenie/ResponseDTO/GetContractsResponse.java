package code.with.sahbaan.contractgenie.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetContractsResponse {

    private long contractId;

    private String contractName;

    private long folderId;
}

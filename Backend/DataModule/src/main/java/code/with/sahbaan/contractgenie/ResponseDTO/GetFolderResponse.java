package code.with.sahbaan.contractgenie.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetFolderResponse {

    private long folderId;
    private String folderName;
    private List<GetContractsResponse> contracts;

    public GetFolderResponse(long folderId, String folderName) {
        this.folderId = folderId;
        this.folderName = folderName;
        this.contracts = new ArrayList<>();
    }
}

package code.with.sahbaan.contractgenie.RequestDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFolderRequest {

    private long folderId;
    private String folderName;
}

package code.with.sahbaan.contractgenie.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddContractRequest {

    private String contractName;

    private String contractFileName;

    private long folderId;

    private String contractUrl;
}

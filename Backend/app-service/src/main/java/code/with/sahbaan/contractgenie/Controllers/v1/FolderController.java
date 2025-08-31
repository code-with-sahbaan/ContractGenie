package code.with.sahbaan.contractgenie.Controllers.v1;

import code.with.sahbaan.contractgenie.RequestDTO.AddFolderRequest;
import code.with.sahbaan.contractgenie.RequestDTO.GetFolderRequest;
import code.with.sahbaan.contractgenie.RequestDTO.UpdateFolderRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.ResponseDTO.GetFolderResponse;
import code.with.sahbaan.contractgenie.Services.FolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folder")
@Slf4j
public class FolderController {

    @Autowired
    private FolderService folderService;

    @GetMapping("v1/getFolders")
    public ResponseEntity<BaseResponse<List<GetFolderResponse>>> getFolders() throws Exception {
        log.info("Executing getFolders in FolderController");
        return new ResponseEntity<>(folderService.getAllFolders(), HttpStatus.OK);
    }

    @PostMapping("v1/updateFolder")
    public ResponseEntity<?> updateFolder(@RequestBody UpdateFolderRequest updateFolderRequest) throws Exception {
        log.info("Executing updateFolder in FolderController");
        folderService.updateFolder(updateFolderRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("v1/addFolder")
    public ResponseEntity<?> addFolder(@RequestBody AddFolderRequest addFolderRequest) throws Exception {
        log.info("Executing addFolder in FolderController");
        folderService.addFolder(addFolderRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

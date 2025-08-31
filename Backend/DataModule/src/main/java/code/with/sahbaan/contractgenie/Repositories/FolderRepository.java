package code.with.sahbaan.contractgenie.Repositories;

import code.with.sahbaan.contractgenie.Entities.Folder;
import code.with.sahbaan.contractgenie.ResponseDTO.GetFolderResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder,Long> {

    @Query("SELECT NEW code.with.sahbaan.contractgenie.ResponseDTO.GetFolderResponse(f.folderId, f.folderName) " +
            "FROM Folder f " +
            "WHERE f.users.userId = :userId")
    List<GetFolderResponse> getFoldersByUserId(@Param("userId") long userId);
}

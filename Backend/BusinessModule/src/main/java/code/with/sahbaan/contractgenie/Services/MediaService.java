package code.with.sahbaan.contractgenie.Services;

import org.springframework.web.multipart.MultipartFile;

public interface MediaService {

    public String uploadFile(MultipartFile file) throws Exception;
}

package code.with.sahbaan.contractgenie.ServicesImpl;

import code.with.sahbaan.contractgenie.Entities.Contract;
import code.with.sahbaan.contractgenie.Entities.Folder;
import code.with.sahbaan.contractgenie.Entities.Users;
import code.with.sahbaan.contractgenie.Repositories.ContractRepository;
import code.with.sahbaan.contractgenie.RequestDTO.*;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.ResponseDTO.GetContractsResponse;
import code.with.sahbaan.contractgenie.Services.ContractService;
import code.with.sahbaan.contractgenie.Services.FolderService;
import code.with.sahbaan.contractgenie.Services.MediaService;
import code.with.sahbaan.contractgenie.Services.UserService;
import code.with.sahbaan.contractgenie.Utils.Constants;
import jakarta.transaction.Transactional;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@EnableAspectJAutoProxy(exposeProxy = true)
public class ContractServiceImpl extends GenericServiceImpl<Contract> implements ContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private MediaService mediaService;

    @Autowired
    private UserService userService;

    @Autowired
    private FolderService  folderService;

    @Autowired
    private VectorStore vectorStore;

    public ContractServiceImpl() {
        super(Contract.class);
    }

    @Override
    public BaseResponse<List<GetContractsResponse>> getAllContracts(GetContractsRequest getContractsRequest) throws Exception {
        try{
            List<GetContractsResponse> getContractsResponseList = contractRepository.getContractsByFolderId(getContractsRequest.getFolderId());
            return new BaseResponse<>("Contracts Fetched Successfully",getContractsResponseList);
        }catch (Exception e){
            throw new Exception("Failed to get Contracts");
        }
    }

    @Override
    @Transactional
    public BaseResponse<List<GetContractsResponse>> addContract(AddContractRequest addContractRequest) throws Exception {
        try{
            Contract contract = new Contract();
            BeanUtils.copyProperties(addContractRequest,contract);
            Folder folder = folderService.getFolderById(addContractRequest.getFolderId());
            contract.setFolder(folder);
            contractRepository.save(contract);
            // Creating Embeddings
            ((ContractService) AopContext.currentProxy()).createEmbeddingFromDoc(contract.getContractUrl(), contract.getContractFileName(), userService.getCurrentUser());
            // Returning All Updated contracts for that folder
            GetContractsRequest getContractsRequest = new GetContractsRequest();
            getContractsRequest.setFolderId(addContractRequest.getFolderId());
            return getAllContracts(getContractsRequest);
        } catch (Exception e) {
            throw new Exception("Failed to add Contract");
        }
    }

    @Async
    @Override
    public void createEmbeddingFromDoc(String docUrl, String docName, Users users) throws Exception{
        try {
            String filePath = "downloaded_document.pdf";
            File file = new File(filePath);
            URL url = new URL(docUrl);
            try (InputStream in = url.openStream();
                 FileOutputStream fos = new FileOutputStream(file)) {

                System.out.println("Downloading PDF and saving to file...");
                byte[] buffer = new byte[4096]; // Use a larger buffer for better performance
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    fos.write(buffer, 0, bytesRead);
                }
            }
            try(PDDocument document = Loader.loadPDF(file)){
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);

                // Creating Metadata
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("userId", String.valueOf(users.getUserId()));

                // Creating Documents
                List<Document> documents = Arrays.stream(text.split("\\r?\\n"))
                        .map(
                             line -> new Document(line, metadata)
                        ).toList();

                // deleting file
                if (!file.delete()){
                    Files.deleteIfExists(file.toPath());
                }
                vectorStore.add(documents);

                // Generating Email
                SendEmail sendEmail =  new SendEmail();
                sendEmail.setToEmail(users.getEmail());
                sendEmail.setSubject("Contract Embeddings Created Successfully");
                sendEmail.setEmailTemplateName(Constants.EMBEDDING_SUCCESSFULLY_CREATED_NAME);
                Map<String,String> emailContent = new HashMap<>();
                emailContent.put("documentName", docName);
                emailContent.put("name",users.getFullName());
                sendEmail.setContent(emailContent);
                userService.initiateEmail(sendEmail);
            }

        } catch (Exception e) {
           throw new Exception("Failed to create Embeddings");
        }
    }

    @Override
    public BaseResponse<List<GetContractsResponse>> updateContract(UpdateContractRequest updateContractRequest) throws Exception {
        try{
            Contract contract = contractRepository.findById(updateContractRequest.getContractId()).get();
            BeanUtils.copyProperties(updateContractRequest,contract);
            Folder folder = folderService.getFolderById(updateContractRequest.getFolderId());
            contract.setFolder(folder);
            contractRepository.save(contract);
            // Returning All Updated contracts for that folder
            GetContractsRequest getContractsRequest = new GetContractsRequest();
            getContractsRequest.setFolderId(updateContractRequest.getFolderId());
            return getAllContracts(getContractsRequest);
        }catch (Exception e){
            throw new Exception("Failed to update Contract");
        }
    }

    @Override
    public BaseResponse<List<GetContractsResponse>> deleteContract(DeleteContractRequest deleteContractRequest) throws Exception {
        try{
            contractRepository.deleteById(deleteContractRequest.getContractId());
            // Returning All Updated contracts for that folder
            GetContractsRequest getContractsRequest = new GetContractsRequest();
            getContractsRequest.setFolderId(deleteContractRequest.getFolderId());
            return getAllContracts(getContractsRequest);
        }catch (Exception e){
            throw new Exception("Failed to delete Contract");
        }
    }
}

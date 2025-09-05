package code.with.sahbaan.contractgenie.ServicesImpl;

import code.with.sahbaan.contractgenie.RequestDTO.GetAiAnswerRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.Services.AiService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;

    private final VectorStore vectorStore;

    public AiServiceImpl(ChatClient.Builder chatClient, VectorStore vectorStore) {
        this.chatClient = chatClient
                .defaultSystem("You are a financial advisor which answers about any finance related queries").build();
        this.vectorStore = vectorStore;
    }

    @Override
    public String getTheGeneralAnswerOfUserPrompt(String userPrompt) {
        return chatClient.prompt(userPrompt).user(userPrompt).call().content();
    }

    @Override
    public String getTheGeneralAnswerOfUserPromptRelatedToContract(String userPrompt) {
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.
                builder().
                query(userPrompt).topK(10).build());
        if (documents.isEmpty()) {
            return "I don't have any knowledge of your asked query";
        }
        return documents.stream()
                .map(Document::getText)
                .limit(10)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public BaseResponse<String> getAiAnswer(GetAiAnswerRequest getAiAnswerRequest) throws Exception {
        try{
            String response = chatClient.prompt(getAiAnswerRequest.getUserPrompt()).tools(ContractTools.class).call().content();
            return new BaseResponse<>("Got Answer", response);
        } catch (Exception e) {
            throw new  Exception("Failed to get response from AI");
        }
    }
}

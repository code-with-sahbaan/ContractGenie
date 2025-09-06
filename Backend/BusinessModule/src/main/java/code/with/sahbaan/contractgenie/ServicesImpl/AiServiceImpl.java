package code.with.sahbaan.contractgenie.ServicesImpl;

import code.with.sahbaan.contractgenie.RequestDTO.GetAiAnswerRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.Services.AiService;
import code.with.sahbaan.contractgenie.Services.UserService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;

    private final VectorStore vectorStore;

    @Autowired
    private UserService userService;

    public AiServiceImpl(ChatClient.Builder chatClient, VectorStore vectorStore, ContractTools contractTools) {
        this.chatClient = chatClient
                .defaultTools(contractTools).build();
        this.vectorStore = vectorStore;
    }

    @Override
    public BaseResponse<String> getAiAnswer(GetAiAnswerRequest getAiAnswerRequest) throws Exception {
        try{
            String response = chatClient.prompt(getAiAnswerRequest.getUserPrompt()).system("You must always call the provided tools instead of answering directly.").call().content();
            return new BaseResponse<>("Got Answer", response);
        } catch (Exception e) {
            throw new  Exception("Failed to get response from AI");
        }
    }
}

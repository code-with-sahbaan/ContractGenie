package code.with.sahbaan.contractgenie.ServicesImpl;

import code.with.sahbaan.contractgenie.Entities.Users;
import code.with.sahbaan.contractgenie.Services.UserService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContractTools {

    private final UserService userService;

    private final VectorStore vectorStore;

    private ChatClient chatClient;

    public ContractTools(UserService userService, VectorStore vectorStore) {
        this.userService = userService;
        this.vectorStore = vectorStore;
    }

    @Tool(name = "getTheGeneralAnswerOfUserPromptRelatedToContract", description = "Get the answer for any user prompt related to contracts details")
    public String getTheGeneralAnswerOfUserPromptRelatedToContract(@ToolParam(description = "Any User Prompt related to contract details") String userPrompt) {
        Users users = userService.getCurrentUser();
        List<Document> documents = vectorStore.similaritySearch(SearchRequest.
                builder().
                query(userPrompt).topK(10).build());
        if (documents.isEmpty()) {
            return "I don't have any knowledge of your asked query. Try sending more details";
        }
        return documents.stream()
                .map(Document::getText)
                .limit(10)
                .collect(Collectors.joining("\n"));
    }

    @Tool(name = "getTheGeneralAnswerOfUserPrompt", description = "Get the answer for any user prompt not related to contracts details")
    public String getTheGeneralAnswerOfUserPrompt(@ToolParam(description = "Any User Prompt not related to contract details") String userPrompt) {
        return chatClient.prompt(userPrompt).user(userPrompt).call().content();
    }
}

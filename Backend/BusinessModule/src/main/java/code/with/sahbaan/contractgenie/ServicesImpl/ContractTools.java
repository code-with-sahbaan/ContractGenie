package code.with.sahbaan.contractgenie.ServicesImpl;

import code.with.sahbaan.contractgenie.Entities.Users;
import code.with.sahbaan.contractgenie.Services.UserService;
import code.with.sahbaan.contractgenie.Utils.Constants;
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

    public ContractTools(UserService userService, ChatClient.Builder chatClient, VectorStore vectorStore) {
        this.userService = userService;
        this.vectorStore = vectorStore;
        this.chatClient = chatClient.build();
    }

    @Tool(name = "getTheGeneralAnswerOfUserPromptRelatedToAnyTypeOfDocs", description = "Get the answer for any user prompt related to any type of doc like " + Constants.TYPES_OF_DOCS)
    public String getTheGeneralAnswerOfUserPromptRelatedToContract(@ToolParam(description = "Any User Prompt related to contract details " + Constants.TYPES_OF_DOCS) String userPrompt) {
        Users users = userService.getCurrentUser();
        String fe = "userId == '" + users.getUserId() + "'";
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(userPrompt)
                        .topK(5)                   // keep top 5
                        .filterExpression(fe)       // optional
                        .build()
        );
        if (documents.isEmpty()) {
            return "I don't have any knowledge of your asked query. Try sending more details";
        }
        return documents.stream()
                .map(Document::getText)
                .limit(10)
                .collect(Collectors.joining("\n"));
    }

    @Tool(name = "getTheGeneralAnswerOfUserPromptNotRelatedToAnyTypeOfDocs", description = "Get the answer for any user prompt not related to any type of document like not related to " + Constants.TYPES_OF_DOCS + " but a general query")
    public String getTheGeneralAnswerOfUserPrompt(@ToolParam(description = "Any User Prompt not related to " + Constants.TYPES_OF_DOCS) String userPrompt) {
        return chatClient.prompt().user(userPrompt).call().content();
    }
}

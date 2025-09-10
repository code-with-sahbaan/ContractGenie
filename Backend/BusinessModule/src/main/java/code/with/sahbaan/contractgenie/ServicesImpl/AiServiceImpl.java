package code.with.sahbaan.contractgenie.ServicesImpl;

import code.with.sahbaan.contractgenie.Entities.ChatMessages;
import code.with.sahbaan.contractgenie.Entities.Users;
import code.with.sahbaan.contractgenie.Repositories.ChatMessageRepository;
import code.with.sahbaan.contractgenie.RequestDTO.GetAiAnswerRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.ResponseDTO.GetAiAnswerResponse;
import code.with.sahbaan.contractgenie.Services.AiService;
import code.with.sahbaan.contractgenie.Services.UserService;
import code.with.sahbaan.contractgenie.Utils.Constants;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;

    private final VectorStore vectorStore;

    @Value("${ai.message.limit}")
    public int MESSAGE_LIMIT;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private UserService userService;

    public AiServiceImpl(ChatClient.Builder chatClient, VectorStore vectorStore, ContractTools contractTools) {
        this.chatClient = chatClient
                .defaultTools(contractTools).build();
        this.vectorStore = vectorStore;
    }

    @Override
    public BaseResponse<GetAiAnswerResponse> getAiAnswer(GetAiAnswerRequest getAiAnswerRequest) throws Exception {
        try{
            Users users = userService.getCurrentUser();

            // fetching last 10 user and assistant messages
            List<ChatMessages> chatMessagesList = chatMessageRepository.getLastUserChatMessages(users.getUserId(), MESSAGE_LIMIT);

            // converting messages to AI messages
            List<Message> messages = new ArrayList<>();

            for (ChatMessages chatMessages : chatMessagesList) {
                Message userMessage = new UserMessage(chatMessages.getUserMessage());
                Message AiReply = new AssistantMessage(chatMessages.getAiReply());
                messages.add(userMessage);
                messages.add(AiReply);
            }

            String response = chatClient.
                    prompt().
                    user(getAiAnswerRequest.getUserPrompt()).
                    messages(messages).
                    system("You must always call the provided tools instead of answering directly.").
                    call().
                    content();

            // Saving reply to DB
            ChatMessages chatMessages = new ChatMessages();
            chatMessages.setUserMessage(getAiAnswerRequest.getUserPrompt());
            chatMessages.setAiReply(response);
            chatMessages.setCreatedAt(LocalDateTime.now());
            chatMessages.setUserId(users.getUserId());
            chatMessageRepository.save(chatMessages);

            // returning response
            GetAiAnswerResponse getAiAnswerResponse = new GetAiAnswerResponse();
            getAiAnswerResponse.setUserMessage(getAiAnswerRequest.getUserPrompt());
            getAiAnswerResponse.setAiMessage(response);
            return new BaseResponse<>("Got Answer", getAiAnswerResponse);
        } catch (Exception e) {
            throw new  Exception("Failed to get response from AI");
        }
    }

    @Override
    public BaseResponse<List<GetAiAnswerResponse>> getChatMessages() throws Exception {
        Users users = userService.getCurrentUser();
        List<GetAiAnswerResponse> aiAnswerResponses = chatMessageRepository.getAllChatMessages(users.getUserId());
        return new BaseResponse<>("Messages Fetched Successfully", aiAnswerResponses);
    }
}

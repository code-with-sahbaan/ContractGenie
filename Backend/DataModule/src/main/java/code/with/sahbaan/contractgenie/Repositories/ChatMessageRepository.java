package code.with.sahbaan.contractgenie.Repositories;

import code.with.sahbaan.contractgenie.Entities.ChatMessages;
import code.with.sahbaan.contractgenie.ResponseDTO.GetAiAnswerResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessages, Long> {

    @Query("SELECT cm FROM ChatMessages cm WHERE cm.userId = :userId ORDER BY cm.createdAt DESC LIMIT :limit")
    List<ChatMessages> getLastUserChatMessages(@Param("userId") long userId, @Param("limit") long limit);

    @Query("SELECT NEW code.with.sahbaan.contractgenie.ResponseDTO.GetAiAnswerResponse(cm.userMessage, cm.aiReply) FROM ChatMessages cm " +
            "WHERE cm.userId = :userId")
    List<GetAiAnswerResponse> getAllChatMessages(@Param("userId") long userId);
}

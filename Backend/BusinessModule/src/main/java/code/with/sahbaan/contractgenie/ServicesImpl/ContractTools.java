package code.with.sahbaan.contractgenie.ServicesImpl;

import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.Services.AiService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContractTools {

    @Autowired
    private AiService aiService;

    @Tool(description = "Get the answer for any user prompt not related to contracts details")
    public BaseResponse<String> getTheAnswerForGeneralUserPrompt(
            @ToolParam(description = "Any User Prompt not related to contract details") String userPrompt){
            return new BaseResponse<>("Response call from general service", aiService.getTheGeneralAnswerOfUserPrompt(userPrompt));
    }

    @Tool(description = "Get the answer for any user prompt related to contracts details")
    public BaseResponse<String> getTheAnswerForContractRelatedToUserPrompt(
            @ToolParam(description = "Any User Prompt related to contract details") String userPrompt){
        return new BaseResponse<>("Response call from contract detail service", aiService.getTheGeneralAnswerOfUserPromptRelatedToContract(userPrompt));
    }
}

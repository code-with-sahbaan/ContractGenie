package code.with.sahbaan.contractgenie.Services;

import code.with.sahbaan.contractgenie.RequestDTO.GetAiAnswerRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;

public interface AiService {

    public String getTheGeneralAnswerOfUserPrompt(String userPrompt);

    public String getTheGeneralAnswerOfUserPromptRelatedToContract(String userPrompt);

    BaseResponse<String> getAiAnswer(GetAiAnswerRequest getAiAnswerRequest) throws Exception;
}

package code.with.sahbaan.contractgenie.Services;

import code.with.sahbaan.contractgenie.RequestDTO.GetAiAnswerRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;

public interface AiService {

    BaseResponse<String> getAiAnswer(GetAiAnswerRequest getAiAnswerRequest) throws Exception;
}

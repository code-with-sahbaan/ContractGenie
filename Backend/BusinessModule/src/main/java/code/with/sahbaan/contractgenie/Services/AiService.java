package code.with.sahbaan.contractgenie.Services;

import code.with.sahbaan.contractgenie.RequestDTO.GetAiAnswerRequest;
import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import code.with.sahbaan.contractgenie.ResponseDTO.GetAiAnswerResponse;

import java.util.List;

public interface AiService {

    BaseResponse<GetAiAnswerResponse> getAiAnswer(GetAiAnswerRequest getAiAnswerRequest) throws Exception;

    BaseResponse<List<GetAiAnswerResponse>> getChatMessages() throws Exception;
}

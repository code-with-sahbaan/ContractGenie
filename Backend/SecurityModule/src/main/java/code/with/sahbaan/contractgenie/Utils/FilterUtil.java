package code.with.sahbaan.contractgenie.Utils;

import code.with.sahbaan.contractgenie.ResponseDTO.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class FilterUtil {

    public static void writeErrorResponse(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        BaseResponse<?> baseResponse = new BaseResponse<>();
        baseResponse.setResponseMessage(message);
        new ObjectMapper().writeValue(response.getOutputStream(), baseResponse);
    }
}

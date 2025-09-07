package code.with.sahbaan.contractgenie.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetAiAnswerResponse {

    private String userMessage;
    private String aiMessage;
}

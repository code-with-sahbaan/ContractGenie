package code.with.sahbaan.contractgenie.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<ResponseType> {

    private String responseMessage;

    private ResponseType responseBody;
}

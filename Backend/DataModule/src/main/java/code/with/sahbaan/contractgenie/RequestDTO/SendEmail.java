package code.with.sahbaan.contractgenie.RequestDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SendEmail implements Serializable {

    private String toEmail;
    private String subject;
    private String emailTemplateName;
    private Map<String, String> content;
}

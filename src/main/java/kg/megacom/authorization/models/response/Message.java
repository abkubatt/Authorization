package kg.megacom.authorization.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
public class Message {

    final String message;

    public static Message of(String message){
        return new Message(message);
    }
}

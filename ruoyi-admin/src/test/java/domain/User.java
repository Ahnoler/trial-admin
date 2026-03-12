package domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {
    private Long user_id;
    private Long dept_id;
    private String user_name;
    private String nick_name;
    private String email;
}

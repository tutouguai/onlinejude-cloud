package mo.entity.vo;

import lombok.*;
import mo.entity.po.main.User;

@Data
@NoArgsConstructor
public class UserWithRePwd {
    private User user;
    private String repwd;
}

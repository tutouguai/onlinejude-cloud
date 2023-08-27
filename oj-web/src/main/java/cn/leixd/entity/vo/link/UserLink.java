package mo.entity.vo.link;

import lombok.*;
import mo.entity.po.main.Privilege;
import mo.entity.po.main.User;

@Data
@NoArgsConstructor
public class UserLink {
    public UserLink(User user, Privilege privilege) {
        this.user = user;
        this.privilege = privilege;
    }

    private User user;
    private Privilege privilege;
}

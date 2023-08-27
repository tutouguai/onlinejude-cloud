package mo.entity.vo.link;

import lombok.*;
import mo.entity.po.main.Contest;
import mo.entity.po.main.ContestApply;
import mo.entity.po.main.User;

@Data
@NoArgsConstructor
public class ContestApplyLink {
    private ContestApply contestApply;
    private Contest contest;
    private User user;

    public ContestApplyLink(ContestApply contestApply, Contest contest, User user) {
        this.contestApply = contestApply;
        this.contest = contest;
        this.user = user;
    }
}

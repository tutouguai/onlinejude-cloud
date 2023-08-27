package mo.entity.vo;

import lombok.*;
import mo.entity.po.main.User;

@Data
@NoArgsConstructor
public class UserContestResult {
    private User user;
    private Integer correctNum;//用户正确的题目数
    private Integer correctSubmittionNum;//用户正确的题目数
    private Integer totalNum;//题目总数

    public UserContestResult(User user, Integer correctNum, Integer correctSubmittionNum, Integer totalNum) {
        this.user = user;
        this.correctNum = correctNum;
        this.correctSubmittionNum = correctSubmittionNum;
        this.totalNum = totalNum;
    }
}

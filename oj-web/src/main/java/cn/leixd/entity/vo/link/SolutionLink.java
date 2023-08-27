package mo.entity.vo.link;

import lombok.*;
import mo.entity.po.main.Problem;
import mo.entity.po.main.Solution;
import mo.entity.po.main.User;

@Data
@NoArgsConstructor
public class SolutionLink {
    private User user;
    private Solution solution;
    private Problem problem;

    public SolutionLink(User user, Solution solution, Problem problem) {
        this.user = user;
        this.solution = solution;
        this.problem = problem;
    }
}

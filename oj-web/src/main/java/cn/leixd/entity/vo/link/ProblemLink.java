package mo.entity.vo.link;

import lombok.*;
import mo.entity.po.main.Problem;
import mo.entity.po.main.User;

@Data
@NoArgsConstructor
public class ProblemLink {
    private Problem problem;
    private User created_by;

    public ProblemLink(Problem problem, User created_by) {
        this.problem = problem;
        this.created_by = created_by;
    }
}

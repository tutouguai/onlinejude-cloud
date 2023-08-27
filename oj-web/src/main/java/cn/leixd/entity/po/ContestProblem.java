package mo.entity.po.main;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
public class ContestProblem {
    private Integer problem_id;
    private Integer contest_id;
    private String title;
    private Integer num;
}

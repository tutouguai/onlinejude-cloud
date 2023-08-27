package mo.entity.vo;

import lombok.*;
import mo.entity.po.main.Problem;
import mo.entity.po.main.Tag;

import java.util.List;

@Data
@NoArgsConstructor
public class ProblemTagTestCase {
    Problem problem;
    List<Tag> tags;
    String testCaseId;
}

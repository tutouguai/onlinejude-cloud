package mo.entity.vo.link;

import lombok.*;
import mo.entity.po.main.ProblemTag;
import mo.entity.po.main.Tag;

@Data
@NoArgsConstructor
public class ProblemTagLink {
    private ProblemTag problemTag;
    private Tag tag;

    public ProblemTagLink(ProblemTag problemTag, Tag tag) {
        this.problemTag = problemTag;
        this.tag = tag;
    }
}

package mo.entity.vo;

import lombok.*;
import mo.entity.po.main.Contest;

@NoArgsConstructor
@Data
public class ContestNumber {
    private Contest contest;
    private int apply_number;

    public ContestNumber(Contest contest, int apply_number) {
        this.contest = contest;
        this.apply_number = apply_number;
    }
}

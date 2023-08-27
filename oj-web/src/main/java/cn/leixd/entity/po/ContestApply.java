package mo.entity.po.main;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class ContestApply {
    private Integer id;
    private Integer contest_id;
    private Integer user_id;
    private Timestamp apply_time;
    private Integer status;

    @Override
    public String toString() {
        return "ContestApply{" +
                "id=" + id +
                ", contest_id=" + contest_id +
                ", user_id=" + user_id +
                '}';
    }
}

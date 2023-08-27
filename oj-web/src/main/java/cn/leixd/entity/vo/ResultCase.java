package mo.entity.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultCase {

    private Integer status;

    private Integer timeUsed;

    private Integer memoryUsed;

    private String errorMessage;


}

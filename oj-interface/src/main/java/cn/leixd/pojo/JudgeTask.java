package cn.leixd.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JudgeTask {

    private String id;

    private Integer proId;

    private List<String> input;

    private List<String> output;

    private Integer timeLimit;

    private Integer memoryLimit;

    private Integer judgeId;

    private String src;

//    private String callBack;

    public JudgeTask(Integer proId, Integer timeLimit, Integer memoryLimit, Integer judgeId, String src) {
        this.proId = proId;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.judgeId = judgeId;
        this.src = src;
    }
}

package cn.leixd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JudgeResult {

    private String taskId;

    private String globalMsg;

    private List<ResultCase> result;


	public JudgeResult(String globalMsg, List<ResultCase> result) {
		this.globalMsg = globalMsg;
		this.result = result;
	}

}

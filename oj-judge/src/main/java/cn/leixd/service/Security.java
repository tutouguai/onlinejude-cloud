package mo.service;

import mo.entity.vo.JudgeResult;
import mo.entity.vo.JudgeTask;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Security {

	@Value("${judge.sensitive-key}")
	private String sensitive;

	public boolean checkSecurity(JudgeTask task, JudgeResult result) {
		for (String key: sensitive.split(",")) {
			if (task.getSrc().contains(key)) {
				result.setGlobalMsg("the src is't allowed to contains sensitive key : " + key);
				return false;
			}
		}
		return true;
	}
}

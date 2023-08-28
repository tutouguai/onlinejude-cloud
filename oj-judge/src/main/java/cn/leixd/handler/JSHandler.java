package mo.handler;

import mo.entity.vo.JudgeTask;
import mo.handler.base.Handler;
import mo.util.ExecutorUtil;
import mo.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class JSHandler extends Handler {

	@Value("${judge.JSrun}")
	private String runWord;

	@Override
	protected void createSrc(JudgeTask task, File path) throws IOException {
		File src = new File(path, "main.js");
		FileUtils.write(task.getSrc(), src);
	}

	@Override
	protected ExecutorUtil.ExecMessage HandlerCompiler(File path) {
		String cmd = runWord.replace("PATH",path.getPath());
		ExecutorUtil.ExecMessage msg = ExecutorUtil.exec(cmd, 2000);
		if (msg.getError() == null || msg.getError().equals("timeOut")){
			msg.setError(null);
		}
		return msg;
	}

	@Override
	protected String getRunCommand(File path) {
		return runWord.replace("PATH",path.getPath());
	}
}

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
public class GoHandler extends Handler {

	@Value("${judge.GoWord}")
	private String compilerWord;

	@Value("${judge.GoRun}")
	private String runWord;

	@Override
	protected void createSrc(JudgeTask task, File path) throws IOException {
		File src = new File(path, "main.go");
		FileUtils.write(task.getSrc(), src);
	}

	@Override
	protected ExecutorUtil.ExecMessage HandlerCompiler(File path) {
		String cmd = compilerWord.replace("PATH", path.getPath());
		return ExecutorUtil.exec(cmd, 2000);
	}

	@Override
	protected String getRunCommand(File path) {
		return runWord.replace("PATH",path.getPath());
	}
}

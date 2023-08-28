package mo.handler.cpphandler;


import mo.handler.base.CHandler;
import mo.util.ExecutorUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class GNUCPP98Handler extends CHandler {

	@Value("${judge.GNUCPP98}")
	private String compilerWord;

	@Override
	protected ExecutorUtil.ExecMessage HandlerCompiler(File path) {
		String cmd = compilerWord.replace("PATH",path.getPath());
		return ExecutorUtil.exec(cmd, 5000);
	}
}

package mo.handler.base;


import com.alibaba.fastjson.JSON;
import mo.entity.vo.JudgeResult;
import mo.entity.vo.JudgeTask;
import mo.entity.vo.ResultCase;
import mo.service.Security;
import mo.util.ExecutorUtil;
import mo.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

/**
 * Base Handler
 */
public abstract class Handler {
	/**
	 * 'Accepted',
	 * 'Presentation Error',
	 * 'Time Limit Exceeded',
	 * 'Memory Limit Exceeded',
	 * 'Wrong Answer',
	 * 'Runtime Error',
	 * 'Output Limit Exceeded',
	 * 'Compile Error',
	 * 'System Error'
	 */
	protected final int AC = 0;
	protected final int PE = 1;
	protected final int TLE = 2;
	protected final int MLE = 3;
	protected final int WA = 4;
	protected final int RE = 5;
	protected final int OLE = 6;
	protected final int CE = 7;
	protected final int SE = 8;

	@Value("${judge.judgePath}")
	private String judgePath;

	@Value("${judge.scriptPath}")
	private String script;

	@Value("${judge.download}")
	private String download;

	@Autowired
	private Security security;


	private final Logger LOGGER = LoggerFactory.getLogger(Handler.class);

	/**
	 * 验证参数是否合法
	 *
	 * @param task   task.
	 * @param result result.
	 * @return bool.
	 */
	private boolean checkTask(JudgeTask task, JudgeResult result) {
		if (task.getProId() == null) {
			if (task.getInput() == null || task.getOutput() == null
				|| task.getInput().size() == 0 || task.getOutput().size() == 0) {
				result.setGlobalMsg("测试数据不能为空!");
				return false;
			}
			if (task.getInput().size() != task.getOutput().size()) {
				result.setGlobalMsg("测试数据组数不对应!");
				return false;
			}
		}
//		if (task.getCallBack() == null) {
//			result.setGlobalMsg("CallBack为空!");
//			return false;
//		}
		if (task.getSrc() == null || task.getSrc().trim().equals("")) {
			result.setGlobalMsg("测试代码不能为空!");
			return false;
		}
		if (task.getTimeLimit() == null || task.getMemoryLimit() == null) {
			result.setGlobalMsg("时间消耗、空间消耗不能为空!");
			return false;
		}
		if (task.getTimeLimit() < 0 || task.getTimeLimit() > 5000) {
			result.setGlobalMsg("时间消耗应在范围0-5s内!");
			return false;
		}
		if (task.getMemoryLimit() < 0 || task.getMemoryLimit() > 524288) {
			result.setGlobalMsg("空间消耗应在范围524288kb内!");
			return false;
		}
		return true;
	}

	/**
	 * （模板方法）创建对应的源程序
	 *
	 * @throws IOException
	 */
	protected abstract void createSrc(JudgeTask task, File path) throws IOException;

	/**
	 * 编译（模板方法）
	 *
	 * @param path
	 * @return
	 */
	protected abstract ExecutorUtil.ExecMessage HandlerCompiler(File path);

	/**
	 * 运行命令（模板方法）
	 *
	 * @param path
	 * @return
	 */
	protected abstract String getRunCommand(File path);

	/**
	 * 创建工作目录
	 *
	 * @param task
	 * @param result
	 * @param path
	 * @return
	 */
	private boolean createWorkspace(JudgeTask task, JudgeResult result, File path, File judgePath) {
		try {
			if (!path.exists())
				path.mkdirs();
			if(!judgePath.exists())
				judgePath.mkdirs();
			if (task.getProId() == null) {//create input and output
				for (int i = 1; i < task.getInput().size(); i++) {
					File inFile = new File(judgePath, i + ".in");
					File outFile = new File(judgePath, i + ".out");
					inFile.createNewFile();
					FileUtils.write(task.getInput().get(i), inFile);
					outFile.createNewFile();
					FileUtils.write(task.getOutput().get(i), outFile);
				}
			}
			else {//复制一份文件
				for (int i = 1; ; i++) {
					Path sourceIn = Paths.get(path + File.separator + i + ".in");
					if (!Files.exists(sourceIn)) {
						break;
					}
					Path sourceOut = Paths.get(path + File.separator + i + ".out");
					Path targetIn = Paths.get(judgePath + File.separator + i + ".in");
					Path targetOut = Paths.get(judgePath + File.separator + i + ".out");
					Files.copy(sourceIn, targetIn, StandardCopyOption.REPLACE_EXISTING);
					Files.copy(sourceOut, targetOut, StandardCopyOption.REPLACE_EXISTING);
				}
			}
			createSrc(task, judgePath);
		} catch (IOException e) {
			result.setGlobalMsg("服务器工作目录出错:" + e);
			return false;
		}
		return true;
	}

	/**
	 * 编译程序
	 *
	 * @param result
	 * @param path
	 * @return
	 */
	private boolean compiler(JudgeResult result, File path) {
		ExecutorUtil.ExecMessage msg = HandlerCompiler(path);
		if (msg.getError() != null) {
			result.setGlobalMsg(msg.getError());
			return false;
		}
		return true;
	}

	/**
	 * 测试源程序
	 *
	 * @param task
	 * @param result
	 * @param path
	 */
	private void runSrc(JudgeTask task, JudgeResult result, File path) {
		//cmd : command timeLimit memoryLimit inFile tmpFile
		String cmd = "script process timeLimit memoryLimit inputFile tmpFile";
		cmd = cmd.replace("script", script);
		cmd = cmd.replace("process", getRunCommand(path).replace(" ", "@"));
		cmd = cmd.replace("timeLimit", task.getTimeLimit().toString());
		cmd = cmd.replace("memoryLimit", task.getMemoryLimit().toString());
		String tmpPath = path.getPath() + File.separator + "tmp.out";
		cmd = cmd.replace("tmpFile", tmpPath);//path.getPath() + File.separator + "tmp"+i+".out"
		List<ResultCase> cases = new ArrayList<>();
		for (int i = 1; ; i++) {
			String inPath = path.getPath() + File.separator + i + ".in";
			File inFile = new File(inPath); //path.getPath() + File.separator + i + ".in"
			File outFile = new File(path.getPath() + File.separator + i + ".out");//path.getPath() + File.separator + i + ".out"
			if (!inFile.exists() || !outFile.exists()) {
				break;
			}
			String cmdItem = cmd;
			cmdItem = cmdItem.replace("inputFile", inPath);//path.getPath() + File.separator + i + ".in"
			ExecutorUtil.ExecMessage msg = ExecutorUtil.exec(cmdItem, 50000);
			ResultCase caseOne = JSON.parseObject(msg.getStdout(), ResultCase.class);
			if (caseOne == null) {
				caseOne = new ResultCase(SE, 0, 0, null);
			}
			if (caseOne.getStatus() == AC) {
				diff(caseOne, new File(tmpPath), outFile);//path.getPath() + File.separator + "tmp"+i+".out"
			}
			//运行报错
			if (msg.getError() != null) {
//				System.out.println(msg.getError());
				caseOne.setStatus(RE);
				caseOne.setMemoryUsed(0);
				caseOne.setTimeUsed(0);
				caseOne.setErrorMessage(msg.getError());
			}
			cases.add(caseOne);
			ExecutorUtil.exec("rm -rf " + path.getPath() + File.separator + "tmp.out", 1000);
		}
		result.setResult(cases);
	}

	/**
	 * 比对结果
	 * @param caseOne
	 * @param tmpOut
	 * @param stdOut
	 */
	public void diff(ResultCase caseOne, File tmpOut, File stdOut) {
		String tem = FileUtils.read(tmpOut);
		String std = FileUtils.read(stdOut);
		if (tem.equals(std)) {
			caseOne.setStatus(AC);
			return;
		}
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		for (int i = 0; i < tem.length(); i++) {
			if (tem.charAt(i) != ' ' && tem.charAt(i) != '\n') {
				sb1.append(tem.charAt(i));
			}
		}
		for (int i = 0; i < std.length(); i++) {
			if (std.charAt(i) != ' ' && std.charAt(i) != '\n') {
				sb2.append(std.charAt(i));
			}
		}
		if (sb1.toString().equals(sb2.toString())) {
			caseOne.setStatus(PE);
		} else {
			System.out.println("user:"+sb1);
			System.out.println("out:"+sb2);
			caseOne.setStatus(WA);
		}
	}

	/**
	 * 判题主流程
	 *
	 * @param task
	 * @return
	 */
	public JudgeResult judge(JudgeTask task) {
		JudgeResult result = new JudgeResult();
		//检验输入是否合法
		if (!checkTask(task, result)) {
			return result;
		}
		if (!security.checkSecurity(task, result)) {
			return result;
		}
		//创建工作目录
		File judgePath = new File("/tmp/OnlineJudgeWorkspace"+ File.separator + System.currentTimeMillis());
		File testDataPath = new File("/home/lxd/problemData"+File.separator+task.getProId());
		//TODO:修改文件路径，使其是一个固定路径，或者在创建文件路径时，从固定路径复制数据,寻找保存文件地址
		if (!createWorkspace(task, result, testDataPath, judgePath)) {
			ExecutorUtil.exec("rm -rf " + judgePath.getPath(), 1000);
			return result;
		}
		//编译
		if (!compiler(result, judgePath)) {
			ExecutorUtil.exec("rm -rf " + judgePath.getPath(), 1000);
			return result;
		}
		runSrc(task, result, judgePath);
		ExecutorUtil.exec("rm -rf " + judgePath.getPath(), 1000);
		return result;
	}

}

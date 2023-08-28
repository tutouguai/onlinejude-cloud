package mo.service;


import com.alibaba.fastjson.JSON;
import mo.core.StatusEnums;
import mo.dao.main.CompileInfoMapper;
import mo.dao.main.SolutionMapper;
import mo.entity.po.main.Solution;
import mo.entity.vo.JudgeResult;
import mo.entity.vo.JudgeTask;
import mo.entity.vo.ResultCase;
import mo.handler.*;
import mo.handler.base.Handler;
import mo.handler.cpphandler.GNUCPP11Handler;
import mo.handler.cpphandler.GNUCPP14Handler;
import mo.handler.cpphandler.GNUCPP17Handler;
import mo.handler.cpphandler.GNUCPP98Handler;
import mo.handler.gcchandler.GNUC11Handler;
import mo.handler.gcchandler.GNUC90Handler;
import mo.handler.gcchandler.GNUC99Handler;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.List;

@Service
public class JudgeService {

	@Autowired
	private GNUC90Handler gnuc90Handler;

	@Resource
	private GNUC99Handler gnuc99Handler;

	@Resource
	private GNUC11Handler gnuc11Handler;

	@Resource
	private GNUCPP98Handler gnucpp98Handler;

	@Resource
	private GNUCPP11Handler gnucpp11Handler;

	@Resource
	private GNUCPP14Handler gnucpp14Handler;

	@Resource
	private GNUCPP17Handler gnucpp17Handler;

	@Resource
	private Py2Handler py2Handler;

	@Resource
	private Py3Handler py3Handler;

	@Resource
	private JavaHandler javaHandler;

	@Resource
	private JSHandler jsHandler;

	@Resource
	private MonoHandler monoHandler;

	@Resource
	private RubyHandler rubyHandler;

	@Resource
	private GoHandler goHandler;

	@Resource
	private SolutionMapper solutionMapper;

	@Resource
	private ProblemService problemService;

	@Resource
	private CompileInfoMapper compileInfoMapper;
//	@Resource
//	KafkaTemplate<String, String> kafkaTemplate;

	@Transactional
	public int judge(JudgeTask task, String solutionId) {
		JudgeResult result;
		if (task.getJudgeId() == null || task.getJudgeId() < 1 || task.getJudgeId() > 14) {
			result = new JudgeResult("编译选项有误!", null);
			return 0;
		} else {
			Handler handler;
			switch (task.getJudgeId()) {
				case 2:
					handler = gnuc99Handler;
					break;
				case 3:
					handler = gnuc11Handler;
					break;
				case 4:
					handler = gnucpp98Handler;
					break;
				case 5:
					handler = gnucpp11Handler;
					break;
				case 6:
					handler = gnucpp14Handler;
					break;
				case 7:
					handler = gnucpp17Handler;
					break;
				case 8:
					handler = javaHandler;
					break;
				case 9:
					handler = py2Handler;
					break;
				case 10:
					handler = py3Handler;
					break;
				case 11:
					handler = jsHandler;
					break;
				case 12:
					handler = monoHandler;
					break;
				case 13:
					handler = rubyHandler;
					break;
				case 14:
					handler = goHandler;
					break;
				default:
					handler = gnuc90Handler;
			}
			result = handler.judge(task);
			result.setTaskId(task.getId());
		}
		//返回result,改为修改数据库
		Solution solution = computerResult(result, solutionId);
		//修改problem表中的submit和accepted参数的值
		Boolean acFlag = solution.getResult()==4;
		problemService.updateProblemByProblemId(solution.getProblem_id(), acFlag);
		return solutionMapper.updateSolutionBySolutionId(solution.getSolution_id(), solution.getTime(),
				solution.getMemory(), solution.getResult(), new Timestamp(System.currentTimeMillis()));
	}

	public Solution computerResult(JudgeResult result, String solutionId){
		Solution solution = new Solution();
		solution.setSolution_id(solutionId);
		if(result==null){
			solution.setResult(7);
//				taskResult.setMessage("Time Limit Exceeded");
			return solution;
		}
		List<ResultCase> results = result.getResult();
		Integer timeUsed = 0, nums=0;
		Integer memoryUsed = 0;
		Integer status = -1;
		//计算时间与空间使用情况
		//判断results是否为空，为空则为运行时错误
		if(CollectionUtils.isEmpty(results)){
			solution.setResult(10);
//				taskResult.setMessage("Compile Error");
			return solution;
		}
		nums = results.size();
		int i=0;
		for (ResultCase resultCase : results) {
			i++;
			timeUsed+=resultCase.getTimeUsed();
			memoryUsed+=resultCase.getMemoryUsed();
			//计算程序运行结果
			if(status>0)
					continue;
			if(status<0){
				switch (resultCase.getStatus()){
					case 7:
						solution.setResult(11);
						status = 11;
//							globalMessage = "Compile Error";
						break;
					case 5:
						solution.setResult(10);
						status = 10;
//							globalMessage = "Runtime Error";
						//TODO:增加一个将编译错误信息写入表中你的操作
						compileInfoMapper.insertCompileInfoBySolutionId(solutionId, resultCase.getErrorMessage());
						break;
					case 8:
						solution.setResult(14);
						status = 14;
//							globalMessage = "System Error";
						break;
					case 6:
						solution.setResult(9);
						status = 9;
//							globalMessage = "Output Limit Exceeded";
						break;
					case 2:
						solution.setResult(7);
						status = 7;
//							globalMessage = "Time Limit Exceeded";
						break;
					case 3:
						solution.setResult(8);
						status = 8;
//							globalMessage = "Memory Limit Exceeded";
						break;
					case 4:
						solution.setResult(6);
						status = 6;
//							globalMessage = "Wrong Answer";
//						System.out.println("Result List-----------");
//						System.out.println(i+"组数据未通过");
						break;
					case 1:
						solution.setResult(5);
						status = 5;
//							globalMessage = "Presentation Error";
						break;
					default:
						status = -1;
						break;
				}
			}
		}
		if(status<0) {
			solution.setResult(4);
//				globalMessage = "Answer Accepted";
		}
//		System.out.println(timeUsed);
		solution.setTime(timeUsed/nums);
		solution.setMemory((Integer) (memoryUsed/(1024*nums)));
		return solution;
	}
}

package cn.leixd.controller;

import cn.leixd.annotation.AuthCheck;
import cn.leixd.entity.vo.Result;
import cn.leixd.enums.RequiredType;
import cn.leixd.enums.ResultCode;
import cn.leixd.service.ContestService;
import cn.leixd.service.SolutionService;
import cn.leixd.utils.HttpUtils;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
public class AdminSolutionControllerImpl {

    private static final Logger logger = LoggerFactory.getLogger(AdminSolutionControllerImpl.class);

    @Resource
    private SolutionService solutionService;

    @Resource
    private ContestService contestService;

    @AuthCheck({RequiredType.JWT, RequiredType.ADMIN})
    @RequestMapping(value = "/admin/contest/{contest_id}/solutions", method = RequestMethod.GET)
    public Result solutions(@PathVariable Integer contest_id,
                            @RequestParam(value = "page", defaultValue = "1") String page,
                            @RequestParam(value = "per_page", defaultValue = "10") String per_page) {
        Integer operatorId = HttpUtils.getJWTUserId();
        if (contestService.hasAccess(operatorId, contest_id)) {
            JSONObject solutions = new JSONObject();
            solutions.put("solutions", solutionService.getContestSolutions(contest_id, Integer.valueOf(page), Integer.valueOf(per_page)));
            return new Result().setData(solutions).setCode(ResultCode.OK);
        } else {
            return new Result().setCode(ResultCode.FORBIDDEN).setMessage("权限不足，无法查看!");
        }
    }
}

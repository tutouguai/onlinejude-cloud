package cn.leixd.service;


import mo.entity.po.main.ContestApply;
import mo.entity.vo.link.ContestApplyLink;

import java.util.List;

public interface ContestApplyService {
    /**
     * 查询竞赛申请
     *
     * @param contestId 竞赛Id
     * @param page      页码
     * @param per_page  每页数量
     * @return
     */
    List<ContestApplyLink> getContestAppliesByContestId(Integer contestId, int page, int per_page);

    /**
     * 查询指定竞赛的申请(待确定)
     *
     * @param contestId 竞赛Id
     * @return
     */
    int getUncheckedApplyNumberByContestId(int contestId);

    /**
     * 根据Id查询申请
     *
     * @param id 申请Id
     * @return
     */
    ContestApply findContestApplyById(Integer id);

    /**
     * 更新指定申请
     *
     * @param id     申请Id
     * @param status 状态
     * @return
     */
    boolean updateContestApply(Integer id, Integer status);

    /**
     * 申请竞赛
     *
     * @param user_id    用户Id
     * @param contest_id 竞赛Id
     * @return
     */
    boolean applyContest(Integer user_id, Integer contest_id);

    /**
     * 根据用户Id和竞赛Id查询是否存在申请
     *
     * @param user_id    用户Id
     * @param contest_id 竞赛Id
     * @return
     */
    boolean checkExistByContestIdAndUserId(Integer user_id, Integer contest_id);

    /**
     * 查询指定竞赛申请数量
     *
     * @param contest_id 竞赛Id
     * @return
     */
    int getContestApplyTotalNumber(int contest_id);

    /**
     * 询指定用户是否申请指定竞赛成功查
     * @param contest_id 竞赛Id
     * @param userId 用户Id
     * @return
     */
    boolean checkIsApplySuccessed(int contest_id, Integer userId);
}

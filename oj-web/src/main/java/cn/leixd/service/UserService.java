package mo.service;

import com.alibaba.fastjson.JSONObject;
import mo.core.Result;
import mo.entity.po.main.User;
import mo.entity.vo.UserContestResult;
import mo.entity.vo.UserWithRePwd;
import mo.entity.vo.link.UserLink;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface UserService {

    int WRONG_PASSWORD = -1;

    /**
     * 验证用户登录
     *
     * @param username 用户名
     * @param passwd   密码
     * @param session  session
     * @return 登录成功返回用户对象，否则返回错误代码和错误描述
     */
    Result checkLogin(String username, String passwd, HttpSession session);

    /**
     * 查询用户名是否存在
     *
     * @param username 用户名
     * @return
     */
    boolean checkUserNameExist(String username);

    /**
     * 用户注册
     *
     * @param user 用户实体
     * @return
     */
    Integer register(User user, HttpSession session);

    /**
     * 根据指定Id查找用户
     *
     * @param userId 用户Id
     * @return 返回用户实体
     */
    User findUserByUserId(Integer userId);

    /**
     * 查询用户
     *
     * @param page     页码
     * @param per_page 每页数量
     * @return 用户集
     */
    List<UserLink> findUsersByPageAndPerPage(Integer page, Integer per_page);

    /**
     * 查询用户
     *
     * @param page     页码
     * @param disabled 禁用状态
     * @param per_page 每页数量
     * @return 用户集
     */
    List<UserLink> findUsersByDisabledPageAndPerPage(int disabled, Integer page, Integer per_page);

    /**
     * 根据用户Id查找用户权限实体
     *
     * @param user_id 用户Id
     * @return 用户权限实体
     */
    UserLink findUserLinkByUserId(Integer user_id);

    /**
     * 删除指定用户
     *
     * @param user_id 用户Id
     * @return 操作结果
     */
    Integer deleteUserByUserId(Integer user_id);

    /**
     * 禁用用户
     *
     * @param user_id
     * @param state
     * @return 操作结果
     */
    Integer disableUser(Integer user_id, Integer state);

    /**
     * 修改用户
     *
     * @param user    用户
     * @param oldUser 旧用户信息
     * @return
     */
    Integer updateUserProfile(Map<String, String> user, UserLink oldUser);

    /**
     * 根据用户名查找用户给
     *
     * @param username 用户名
     * @return
     */
    User findUserByUsername(String username);

    /**
     * 查询指定竞赛的用户信息
     *
     * @param contest_id 竞赛Id
     * @param page       页码
     * @param per_page   每页数量
     * @return
     */
    List<UserContestResult> users(Integer contest_id, int page, int per_page);

    /**
     * 生成令牌
     *
     * @param userId 用户Id
     * @return
     */
    JSONObject makeJWT(Integer userId);

    /**
     * 查询用户数量
     *
     * @return
     */
    int getUserTotalNumer();

    /**
     * 查询指定禁用状态用户数量
     *
     * @param disabled 是否禁用
     * @return
     */
    int getUserTotalNumerByIsDisabled(int disabled);

    /**
     * 根据关键词查找用户
     *
     * @param keycode 关键词
     * @return
     */
    List<UserLink> findSimilarUserByUserNameAndNickName(String keycode);

    /**
     * 更新用户头像
     * @param path 路径
     * @param userId 用户Id
     * @return
     */
    boolean updateUserHeaderImage(String path, Integer userId);

    /**
     * 更新用户profile
     * @param user 用户实体
     * @param userId 用户编号
     * @return
     */
    boolean updateUserProfile(User user, Integer userId);

    /**
     * 更新用户Account
     * @param user 用户实体
     * @param userId 用户编号
     * @return
     */
    int updateUserAccount(UserWithRePwd user, Integer userId);
}

package mo.controller.index.impl;

import com.alibaba.fastjson.JSONObject;
import mo.controller.AbstractController;
import mo.controller.index.UserController;
import mo.core.Result;
import mo.core.ResultCode;
import mo.entity.po.main.User;
import mo.entity.vo.UserWithRePwd;
import mo.interceptor.annotation.AuthCheck;
import mo.interceptor.annotation.RequiredType;
import mo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static mo.utils.StringValue.ONLINEJUDGE_SESSION_UER;

@RestController
@SessionAttributes(value = {ONLINEJUDGE_SESSION_UER}, types = {User.class})
public class UserControllerImpl extends AbstractController implements UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserControllerImpl.class);

    @Resource
    private UserService userService;

    @Override
    @RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    @ResponseBody
    public Result login(User user, HttpServletRequest request) {
        logger.info("user[{}]", user.toString());
        Result res = userService.checkLogin(user.getUsername(), user.getPasswd(), request.getSession());
        if (res.getCode() == ResultCode.OK.code()) {
            logger.info("登录成功，创建session，id为[{}],user[{}]", request.getSession().getId(), request.getSession().getAttribute("user"));
        }
        return res;
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public Result logout(User user, SessionStatus sessionStatus) {
        logger.info("user[{}]", user.toString());
        sessionStatus.setComplete();
        return new Result().setCode(ResultCode.OK);
    }

    @Override
    @ResponseBody
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public Result user(@RequestParam(value = "username", defaultValue = "") String username) {
        JSONObject user = new JSONObject();
        User res = null;
        if ("".equals(username)) {
            res = userService.findUserByUserId(getJWTUserId());
            res.setPasswd("");
            user.put("user",res );
        } else {
            res = userService.findUserByUsername(username);
            res.setPasswd("");
            user.put("user", res);
        }
        return new Result().setCode(ResultCode.OK).setData(user);
    }

    @Override
    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "application/x-www-form-urlencoded")
    public Result register(User user, HttpServletRequest request) {
        logger.info("user[{}]", user.toString());
        if (!userService.checkUserNameExist(user.getUsername())) {
            Integer userId = userService.register(user, request.getSession());
            if (userId == -1) {
                return new Result().setCode(ResultCode.INTERNAL_SERVER_ERROR).setMessage("注册用户失败");
            } else {
                return new Result().setCode(ResultCode.OK).setData(userService.makeJWT(userId));
            }
        } else {
            return new Result().setCode(ResultCode.BAD_REQUEST).setMessage("用户名已存在");
        }
    }

    @Override
    @AuthCheck({RequiredType.JWT})
    @RequestMapping(value = "/user_head_image", method = RequestMethod.PUT)
    public Result updateHeaderImage(@RequestParam("head_url") String head_url) {
        //todo 删除原头像
        if(userService.updateUserHeaderImage(head_url,getJWTUserId())){
            return new Result().setCode(ResultCode.OK);
        }else{
            return new Result().setCode(ResultCode.BAD_REQUEST).setMessage("更新头像失败");
        }
    }

    @Override
    @AuthCheck({RequiredType.JWT})
    @RequestMapping(value = "/user/profile", method = RequestMethod.PUT)
    public Result updateProfile(@RequestBody User user) {
        if(userService.updateUserProfile(user,getJWTUserId())){
            return new Result().setCode(ResultCode.OK);
        }else{
            return new Result().setCode(ResultCode.BAD_REQUEST).setMessage("更新失败");
        }
    }

    @Override
    @AuthCheck({RequiredType.JWT})
    @RequestMapping(value = "/user/account", method = RequestMethod.PUT)
    public Result updateAccount(@RequestBody UserWithRePwd user) {
        switch (userService.updateUserAccount(user,getJWTUserId())){
            case -1:{
                return new Result().setCode(ResultCode.BAD_REQUEST).setMessage("密码错误！");
            }
            case 0:{
                return new Result().setCode(ResultCode.BAD_REQUEST).setMessage("修改失败！");
            }
            case 1:{
                return new Result().setCode(ResultCode.OK);
            }
        }
        return new Result().setCode(ResultCode.BAD_REQUEST).setMessage("修改失败！");
    }


}

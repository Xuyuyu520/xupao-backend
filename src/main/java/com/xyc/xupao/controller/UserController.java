package com.xyc.xupao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyc.xupao.common.BaseResponse;
import com.xyc.xupao.common.ErrorCode;
import com.xyc.xupao.common.ResultUtils;
import com.xyc.xupao.exception.BusinessException;
import com.xyc.xupao.model.domain.User;
import com.xyc.xupao.model.request.UserLoginRequest;
import com.xyc.xupao.model.request.UserRegisterRequest;
import com.xyc.xupao.model.vo.UserVO;
import com.xyc.xupao.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.xyc.xupao.contant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户控制器
 *
 * @author xuYuYu
 * @date 2024-04-29 12:56:33
 */
@Slf4j
@RestController
@Api(tags = "用户接口")
@RequestMapping("/user")
@CrossOrigin(origins = {"http://localhost:3000"})
public class UserController {

	@Resource
	private UserService userService;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	/**
	 * 用户注册
	 *
	 * @param userRegisterRequest
	 * @return
	 */
	@ApiOperation("用户注册")
	@PostMapping("/register")
	public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
		// 校验
		if (userRegisterRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		String userAccount = userRegisterRequest.getUserAccount();
		String userPassword = userRegisterRequest.getUserPassword();
		String checkPassword = userRegisterRequest.getCheckPassword();
		String planetCode = userRegisterRequest.getPlanetCode();
		if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
			return null;
		}
		long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
		return ResultUtils.success(result);
	}

	/**
	 * 用户登录
	 *
	 * @param userLoginRequest
	 * @param request
	 * @return
	 */
	@ApiOperation("用户登录")
	@PostMapping("/login")
	public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
		if (userLoginRequest == null) {
			return ResultUtils.error(ErrorCode.PARAMS_ERROR);
		}
		String userAccount = userLoginRequest.getUserAccount();
		String userPassword = userLoginRequest.getUserPassword();
		if (StringUtils.isAnyBlank(userAccount, userPassword)) {
			return ResultUtils.error(ErrorCode.PARAMS_ERROR);
		}
		User user = userService.userLogin(userAccount, userPassword, request);
		return ResultUtils.success(user);
	}

	/**
	 * 用户注销
	 *
	 * @param request
	 * @return
	 */
	@ApiOperation("用户登出")
	@PostMapping("/logout")
	public BaseResponse<Integer> userLogout(HttpServletRequest request) {
		if (request == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		int result = userService.userLogout(request);
		return ResultUtils.success(result);
	}

	/**
	 * 获取当前用户
	 *
	 * @param request
	 * @return
	 */
	@ApiOperation("获取当前用户")
	@GetMapping("/current")
	public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
		Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
		User currentUser = (User) userObj;
		if (currentUser == null) {
			throw new BusinessException(ErrorCode.NOT_LOGIN);
		}
		long userId = currentUser.getId();
		// TODO 校验用户是否合法
		User user = userService.getById(userId);
		User safetyUser = userService.getSafetyUser(user);
		return ResultUtils.success(safetyUser);
	}

	// https://github.com/Xuyuyu520/
	@ApiOperation("查询用户")
	@GetMapping("/search")
	public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
		if (!userService.isAdmin(request)) {
			throw new BusinessException(ErrorCode.NO_AUTH, "缺少管理员权限");
		}
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotBlank(username)) {
			queryWrapper.like("username", username);
		}
		List<User> userList = userService.list(queryWrapper);
		List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
		return ResultUtils.success(list);
	}

	@ApiOperation("推荐用户")
	@GetMapping("/recommend")
	public BaseResponse<Page<User>> recommendUsers(long pageSize, long pageNum, HttpServletRequest request) {
		User loginUser = userService.getLoginUser(request);
		String key = String.format("xupao:user:recommend:%s", loginUser.getId());
		// 有 缓存 查询缓存
		ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
		Page<User> userPage = (Page<User>) valueOperations.get(key);
		if (userPage != null) {
			return ResultUtils.success(userPage);
		}
		// 无 缓存 查询数据库
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		userPage = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
		// 然后存入缓存
		try {
			valueOperations.set(key, userPage, 30000, TimeUnit.MICROSECONDS);
		} catch (Exception e) {
			log.error("存入缓存失败-redis set key Error ", e);
		}
		return ResultUtils.success(userPage);
	}

	@ApiOperation("查询标签")
	@GetMapping("/search/tags")
	public BaseResponse<List<User>> searchUsersByTags(@RequestParam(required = false) List<String> tagNameList) {
		if (CollectionUtils.isEmpty(tagNameList)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		List<User> userList = userService.searchUserTags(tagNameList);
		return ResultUtils.success(userList);
	}

	@ApiOperation("更新用户")
	@PostMapping("/update")
	public BaseResponse<Integer> updateUser(@RequestBody User user, HttpServletRequest request) {
		if (user == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User loginUser = userService.getLoginUser(request);

		Integer result = userService.updateByUser(user, loginUser);
		return ResultUtils.success(result);
	}

	@ApiOperation("删除用户")
	@PostMapping("/delete")
	public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
		if (!userService.isAdmin(request)) {
			throw new BusinessException(ErrorCode.NO_AUTH);
		}
		if (id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		boolean b = userService.removeById(id);
		return ResultUtils.success(b);
	}

	/**
	 * 匹配用户
	 *
	 * @param num     数字
	 * @param request 请求
	 * @return {@link BaseResponse }<{@link User }>
	 */
	@ApiOperation("匹配用户")
	@GetMapping("/match")
	public BaseResponse<List<User>> matchUsers(long num, HttpServletRequest request) {
		if (num <= 0 || num > 20) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User loginUser = userService.getLoginUser(request);
		return ResultUtils.success(userService.matchUsers(num, loginUser));
	}
}

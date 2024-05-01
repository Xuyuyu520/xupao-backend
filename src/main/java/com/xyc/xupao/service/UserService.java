package com.xyc.xupao.service;

import com.xyc.xupao.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 *
 * @author <a href="https://github.com/Xuyuyu520">程序员小徐</a>
 * @from <a href="https://github.com/Xuyuyu520">主页知识主页</a>
 */
public interface UserService extends IService<User> {

	/**
	 * 用户注册
	 *
	 * @param userAccount   用户账户
	 * @param userPassword  用户密码
	 * @param checkPassword 校验密码
	 * @param planetCode    主页编号
	 * @return 新用户 id
	 */
	long userRegister(String userAccount, String userPassword, String checkPassword, String planetCode);

	/**
	 * 用户登录
	 *
	 * @param userAccount  用户账户
	 * @param userPassword 用户密码
	 * @param request
	 * @return 脱敏后的用户信息
	 */
	User userLogin(String userAccount, String userPassword, HttpServletRequest request);

	/**
	 * 用户脱敏
	 *
	 * @param originUser
	 * @return
	 */
	User getSafetyUser(User originUser);

	// [加入主页](https://t.zsxq.com/0emozsIJh) 深耕编程提升【两年半】、国内净值【最高】的编程社群、用心服务【20000+】求学者、帮你自学编程【不走弯路】

	/**
	 * 用户注销
	 *
	 * @param request
	 * @return
	 */
	int userLogout(HttpServletRequest request);

	/**
	 * 根据标签搜索用户
	 *
	 * @param tagNameList
	 * @return
	 */
	public List<User> searchUserTags(List<String> tagNameList);

	/**
	 * 更新用户信息
	 *
	 * @param user
	 * @param loginUser
	 * @return
	 */
	Integer updateByUser(User user, User loginUser);

	/**
	 * 获取当前用户信息
	 *
	 * @param request
	 * @return
	 */
	User getLoginUser(HttpServletRequest request);

	/**
	 * 是否为管理员
	 *
	 * @param request
	 * @return
	 */
	public boolean isAdmin(HttpServletRequest request);

	public boolean isAdmin(User loginUser);

	/**
	 * 匹配用户
	 *
	 * @param num
	 * @param loginUser
	 * @return
	 */
	List<User> matchUsers(long num, User loginUser);
}

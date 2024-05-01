package com.xyc.xupao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyc.xupao.common.ErrorCode;
import com.xyc.xupao.exception.BusinessException;
import com.xyc.xupao.mapper.TeamMapper;
import com.xyc.xupao.model.domain.Team;
import com.xyc.xupao.model.domain.User;
import com.xyc.xupao.model.domain.UserTeam;
import com.xyc.xupao.model.dto.TeamQuery;
import com.xyc.xupao.model.enums.TeamStatusEnum;
import com.xyc.xupao.model.request.TeamJoinRequest;
import com.xyc.xupao.model.request.TeamQuitRequest;
import com.xyc.xupao.model.request.TeamUpdateRequest;
import com.xyc.xupao.model.vo.TeamUserVO;
import com.xyc.xupao.model.vo.UserVO;
import com.xyc.xupao.service.TeamService;
import com.xyc.xupao.service.UserService;
import com.xyc.xupao.service.UserTeamService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author 27178
 * @description 针对表【team(队伍)】的数据库操作Service实现
 * @createDate 2024-04-29 12:20:32
 */
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team> implements TeamService {

	@Resource
	private UserTeamService userTeamService;
	@Resource
	private UserService userService;

	/**
	 * 创建队伍
	 *
	 * @param team
	 * @param loginUser
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public long addTeam(Team team, User loginUser) {
		// 1.参数不能为空
		if (team == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		// 2.是否登录 未登录不能创建
		if (loginUser == null) {
			throw new BusinessException(ErrorCode.NOT_LOGIN);
		}
		final Long userId = loginUser.getId();
		// 3.校验
		// 3.1 队伍人数不能--> num<1||num>20
		Integer maxNum = Optional.ofNullable(team.getMaxNum()).orElse(Integer.valueOf(0));
		if (maxNum < 1 || maxNum > 20) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍数量不满足要求");
		}
		// 3.2 队伍名称不能为空·且不能大于20
		String name = team.getName();
		if (StringUtils.isBlank(name) || name.length() > 20) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍名称长不满足要求");
		}
		// 3.3 队伍描述 <512
		String description = team.getDescription();
		if (StringUtils.isNotBlank(description) && team.getDescription().length() > 512) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍描述不满足要求");
		}
		// 3.4 队伍状态
		Integer status = Optional.ofNullable(team.getStatus()).orElse(Integer.valueOf(0));
		TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
		if (statusEnum == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍状态不满足要求");
		}
		// 3.5如果status 是加密状态 一定要密码 密码最大32位
		String password = team.getPassword();
		if (TeamStatusEnum.SECRET.equals(statusEnum) && StringUtils.isBlank(password) || password.length() > 512) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码设置不对");
		}
		// 3.6 超时时间 大于当前时间
		Date expireTime = team.getExpireTime();
		if (new Date().after(expireTime)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "超时时间 大于当前时间");
		}
		// 3.7 校验当前用户 最多创建5个表  todo
		QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
		queryWrapper.like("userId", userId);
		long hasTeamNum = this.count(queryWrapper);
		if (hasTeamNum >= 5) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍数量已超过5");
		}
		// 4 插入到队伍表
		team.setId(null);
		team.setUserId(userId);
		boolean result = this.save(team);
		if (!result) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍失败");
		}

		// 5插入用户 队伍关系表
		UserTeam userTeam = new UserTeam();
		userTeam.setUserId(userId);
		Long teamId = team.getId();
		userTeam.setTeamId(teamId);
		userTeam.setJoinTime(new Date());
		result = userTeamService.save(userTeam);
		if (!result) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "创建队伍关系失败");
		}
		return teamId;
	}

	/**
	 * 搜索队伍
	 *
	 * @param teamQuery
	 * @param isAdmin
	 * @return
	 */
	public List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin) {
		QueryWrapper<Team> queryWrapper = new QueryWrapper<>();
		// 组合查询条件
		if (teamQuery != null) {
			Long id = teamQuery.getId();
			if (id != null && id > 0) {
				queryWrapper.eq("id", id);
			}
			List<Long> idList = teamQuery.getIdList();
			if (CollectionUtils.isNotEmpty(idList)) {
				queryWrapper.in("id", idList);
			}
			String searchText = teamQuery.getSearchText();
			if (StringUtils.isNotBlank(searchText)) {
				queryWrapper.and(qw -> qw.like("name", searchText).or().like("description", searchText));
			}
			String name = teamQuery.getName();
			if (StringUtils.isNotBlank(name)) {
				queryWrapper.like("name", name);
			}
			String description = teamQuery.getDescription();
			if (StringUtils.isNotBlank(description)) {
				queryWrapper.like("description", description);
			}
			Integer maxNum = teamQuery.getMaxNum();
			// 查询最大人数相等的
			if (maxNum != null && maxNum > 0) {
				queryWrapper.eq("maxNum", maxNum);
			}
			Long userId = teamQuery.getUserId();
			// 根据创建人来查询
			if (userId != null && userId > 0) {
				queryWrapper.eq("userId", userId);
			}
			// 根据状态来查询
			Integer status = teamQuery.getStatus();
			TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
			if (statusEnum == null) {
				statusEnum = TeamStatusEnum.PUBLIC;
			}
			if (!isAdmin && statusEnum.equals(TeamStatusEnum.PRIVATE)) {
				throw new BusinessException(ErrorCode.NO_AUTH);
			}
			queryWrapper.eq("status", statusEnum.getValue());
		}
		// 不展示已过期的队伍
		// expireTime is null or expireTime > now()
		queryWrapper.and(qw -> qw.gt("expireTime", new Date()).or().isNull("expireTime"));
		List<Team> teamList = this.list(queryWrapper);
		if (CollectionUtils.isEmpty(teamList)) {
			return new ArrayList<>();
		}
		List<TeamUserVO> teamUserVOList = new ArrayList<>();
		// 关联查询创建人的用户信息
		for (Team team : teamList) {
			Long userId = team.getUserId();
			if (userId == null) {
				continue;
			}
			User user = userService.getById(userId);
			TeamUserVO teamUserVO = new TeamUserVO();
			BeanUtils.copyProperties(team, teamUserVO);
			// 脱敏用户信息
			if (user != null) {
				UserVO userVO = new UserVO();
				BeanUtils.copyProperties(user, userVO);
				teamUserVO.setCreateUser(userVO);
			}
			teamUserVOList.add(teamUserVO);
		}
		return teamUserVOList;
	}

	/**
	 * 修改队伍
	 *
	 * @param teamUpdateRequest
	 * @param loginUser
	 * @return
	 */
	@Override
	public boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser) {
		if (teamUpdateRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		// 更新人的id
		Long requestId = teamUpdateRequest.getId();
		if (requestId == null || requestId <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		// 查询队伍是否存在
		Team oldTeam = this.getById(requestId);
		if (oldTeam == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR);
		}
		// 是否登录
		if (oldTeam.getUserId() != loginUser.getId() && !userService.isAdmin(loginUser)) {
			throw new BusinessException(ErrorCode.NO_AUTH);
		}
		TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(teamUpdateRequest.getStatus());
		if (statusEnum.equals(TeamStatusEnum.SECRET)) {
			if (StringUtils.isBlank(teamUpdateRequest.getPassword())) {

				throw new BusinessException(ErrorCode.PARAMS_ERROR, "请设置密码");
			}
		}
		Team updateTeam = new Team();
		BeanUtils.copyProperties(teamUpdateRequest, updateTeam);
		boolean result = this.updateById(updateTeam);
		return result;
	}

	/**
	 * 加入队伍
	 *
	 * @param teamJoinRequest
	 * @param loginUser
	 * @return
	 */
	@Override
	public boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser) {
		// 校验规则
		if (teamJoinRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Long teamId = teamJoinRequest.getTeamId();
		Team team = getTeamById(teamId);
		if (team.getExpireTime() != null && team.getExpireTime().before(new Date())) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已过期");
		}

		Integer status = team.getStatus();
		TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
		if (statusEnum.equals(TeamStatusEnum.PRIVATE)) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "禁止加入私有队伍");
		}
		if (statusEnum.equals(TeamStatusEnum.SECRET)) {
			String password = teamJoinRequest.getPassword();
			if (StringUtils.isBlank(password) || !password.equals(team.getPassword())) {
				throw new BusinessException(ErrorCode.PARAMS_ERROR, "必须要密码才能加入");
			}
		}
		// 查询数据库 改用户加入的队伍 数量是否已经超过5个
		Long userId = loginUser.getId();
		// 本人不能加入自己的队伍
		if (team.getUserId() == userId) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "不能加入自己的队伍");
		}
		QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
		userTeamQueryWrapper.eq("userId", userId);
		long hasJoinNum = userTeamService.count(userTeamQueryWrapper);
		if (hasJoinNum > 5) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "最多加入和创建队伍有5个");
		}
		// 幂等性判断 不能重复加入
		userTeamQueryWrapper = new QueryWrapper<>();
		userTeamQueryWrapper.eq("userId", userId);
		userTeamQueryWrapper.eq("teamId", teamId);

		long hasUseJoinTeam = userTeamService.count(userTeamQueryWrapper);
		if (hasUseJoinTeam > 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户已经加入队伍");
		}
		// 不能
		// 查询 改队伍人数 是否已经超过 5人
		long teamHasJoinNum = this.countTeamUserByTeamId(teamId);
		if (teamHasJoinNum >= team.getMaxNum()) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "队伍已将满员");
		}
		// 修改队伍信息
		UserTeam userTeam = new UserTeam();
		userTeam.setUserId(userId);
		userTeam.setTeamId(teamId);
		userTeam.setJoinTime(new Date());
		return userTeamService.save(userTeam);
	}

	/**
	 * 退出队伍
	 *
	 * @param teamQuitRequest
	 * @param loginUser
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser) {
		// 1.校验是否为空
		if (teamQuitRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		// 2.校验队伍是否存在
		Long teamId = teamQuitRequest.getId();
		Team team = getTeamById(teamId);
		// 3.校验是否加入队伍
		Long userId = loginUser.getId();
		UserTeam userTeam = new UserTeam();
		userTeam.setUserId(userId);
		userTeam.setTeamId(teamId);
		QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>(userTeam);
		long count = this.userTeamService.count(queryWrapper);
		if (count == 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR, "未加入队伍");
		}
		// 4.校验 队伍人数
		long teamHasJoinNum = this.countTeamUserByTeamId(teamId);
		// 队伍只剩一人 解散队伍
		if (teamHasJoinNum == 1) {
			// 删除队伍
			this.removeById(teamId);
		} else {
			// 队伍不是 1 人
			// 判断 是否是队长
			if (team.getUserId() == userId) {
				// 查询队伍的人数是否满足
				queryWrapper = new QueryWrapper<UserTeam>().eq("teamId", teamId);
				queryWrapper.last("order by id asc limit 2");
				List<UserTeam> userTeamList = userTeamService.list(queryWrapper);
				if (CollectionUtils.isEmpty(userTeamList) || userTeamList.size() <= 1) {
					throw new BusinessException(ErrorCode.SYSTEM_ERROR);
				}
				// 获取第二条数据
				UserTeam nextUserTeam = userTeamList.get(1);
				Long nextTeamLeaderId = nextUserTeam.getUserId();
				// 更新当前队伍队长
				Team updateTeam = new Team();
				updateTeam.setId(teamId);
				updateTeam.setUserId(nextTeamLeaderId);
				boolean result = this.updateById(updateTeam);
				if (!result) {
					throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新队伍失败");
				}
			}
		}
		// 删除之前队伍
		return userTeamService.remove(queryWrapper);
	}

	/**
	 * 删除队伍
	 *
	 * @param id
	 * @param loginUser
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean deleteTeam(Long id, User loginUser) {
		// 校验 队伍是否存在
		Team team = getTeamById(id);
		// 查询是否是队长
		if (team.getUserId() != loginUser.getId()) {
			throw new BusinessException(ErrorCode.FORBIDDEN);
		}
		// 删除队伍关系
		QueryWrapper<UserTeam> queryWrapper = new QueryWrapper<>();
		Long teamId = team.getId();
		queryWrapper.eq("teamId", teamId);
		boolean result = userTeamService.remove(queryWrapper);
		if (!result) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除队伍关系失败");
		}
		// 删除队伍
		return this.removeById(teamId);
	}

	/**
	 * 查询队伍是否存在
	 *
	 * @param teamId
	 * @return
	 */
	private Team getTeamById(Long teamId) {
		if (teamId == null || teamId <= 0) {
			throw new BusinessException(ErrorCode.NULL_ERROR);
		}
		Team team = this.getById(teamId);
		if (team == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
		}
		return team;
	}

	/**
	 * 统计当前队伍人数
	 *
	 * @param teamId
	 * @return
	 */
	public long countTeamUserByTeamId(Long teamId) {
		return this.userTeamService.count(new QueryWrapper<UserTeam>().eq("teamId", teamId));
	}
}





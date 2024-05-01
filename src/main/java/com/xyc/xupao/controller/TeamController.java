package com.xyc.xupao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyc.xupao.common.BaseResponse;
import com.xyc.xupao.common.DeleteRequest;
import com.xyc.xupao.common.ErrorCode;
import com.xyc.xupao.common.ResultUtils;
import com.xyc.xupao.exception.BusinessException;
import com.xyc.xupao.model.domain.Team;
import com.xyc.xupao.model.domain.User;
import com.xyc.xupao.model.domain.UserTeam;
import com.xyc.xupao.model.dto.TeamQuery;
import com.xyc.xupao.model.request.TeamAddRequest;
import com.xyc.xupao.model.request.TeamJoinRequest;
import com.xyc.xupao.model.request.TeamQuitRequest;
import com.xyc.xupao.model.request.TeamUpdateRequest;
import com.xyc.xupao.model.vo.TeamUserVO;
import com.xyc.xupao.service.TeamService;
import com.xyc.xupao.service.UserService;
import com.xyc.xupao.service.UserTeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 团队控制器
 *
 * @author xuYuYu
 * @date 2024-04-29 12:56:27
 */
@Slf4j
@RestController
@Api(tags = "队伍页面接口")
@RequestMapping("/team")
@CrossOrigin(origins = {"http://localhost:3000"})
public class TeamController {

	@Resource
	private UserService userService;

	@Resource
	private TeamService teamService;

	@Resource
	private UserTeamService userTeamService;

	@ApiOperation("创建队伍")
	@PostMapping("/add")
	public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
		if (teamAddRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User loginUser = userService.getLoginUser(request);
		Team team = new Team();
		BeanUtils.copyProperties(teamAddRequest, team);
		long teamId = teamService.addTeam(team, loginUser);
		return ResultUtils.success(teamId, "队伍创建成功");
	}

	@ApiOperation("更新队伍")
	@PostMapping("/update")
	public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest, HttpServletRequest request) {
		if (teamUpdateRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User loginUser = userService.getLoginUser(request);
		boolean result = teamService.updateTeam(teamUpdateRequest, loginUser);
		if (!result) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
		}
		return ResultUtils.success(true, "更新成功");
	}

	@ApiOperation("获取队伍")
	@GetMapping("/get")
	public BaseResponse<Team> getTeamById(Long id) {
		if (id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Team team = teamService.getById(id);
		if (team == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR);
		}
		return ResultUtils.success(team, "获取成功");
	}

	@ApiOperation("查询所有队伍")
	@GetMapping("/list")
	public BaseResponse<List<TeamUserVO>> listTeam(TeamQuery teamQuery, HttpServletRequest request) {
		if (teamQuery == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		boolean admin = userService.isAdmin(request);
		List<TeamUserVO> teamList = teamService.listTeams(teamQuery, admin);
		// 判断是否加入队伍
		List<Long> teamIdList = teamList.stream().map(TeamUserVO::getId).collect(Collectors.toList());

		try {
			User loginUser = userService.getLoginUser(request);
			List<UserTeam> userTeamList = userTeamService.list(new QueryWrapper<UserTeam>()
					.eq("userId", loginUser.getId())
					.in("teamId", teamIdList));
			Set<Long> hasJoinTeamIdSet = userTeamList.stream().map(UserTeam::getId).collect(Collectors.toSet());
			teamList.forEach(team -> {
				boolean hasJoin = hasJoinTeamIdSet.contains(team.getId());
				team.setHasJoin(hasJoin);
			});
		} catch (Exception e) {}
		return ResultUtils.success(teamList, "获取分页数据成功");
	}

	@ApiOperation("分页查询")
	@GetMapping("/list/page")
	public BaseResponse<Page<Team>> listTeamPage(TeamQuery teamQuery) {
		if (teamQuery == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Team team = new Team();

		BeanUtils.copyProperties(teamQuery, team);

		Page<Team> teamPage = new Page<Team>(teamQuery.getPageNum(), teamQuery.getPageSize());
		QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
		Page<Team> resultPage = teamService.page(teamPage, queryWrapper);
		return ResultUtils.success(resultPage);
	}

	@ApiOperation("加入队伍")
	@PostMapping("join")
	public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
		if (teamJoinRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User loginUser = userService.getLoginUser(request);
		boolean result = teamService.joinTeam(teamJoinRequest, loginUser);
		return ResultUtils.success(result, "加入成功");
	}

	@ApiOperation("退出队伍")
	@PostMapping("/quit")
	public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest, HttpServletRequest request) {
		if (teamQuitRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User loginUser = userService.getLoginUser(request);
		boolean result = teamService.quitTeam(teamQuitRequest, loginUser);
		return ResultUtils.success(result, "删除成功");
	}

	@ApiOperation("删除队伍")
	@PostMapping("/delete")
	public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
		if (deleteRequest == null || deleteRequest.getId() <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Long deleteRequestId = deleteRequest.getId();
		User loginUser = userService.getLoginUser(request);
		boolean result = teamService.deleteTeam(deleteRequestId, loginUser);
		if (!result) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "插入失败");
		}
		return ResultUtils.success(true, "删除成功");
	}

	@ApiOperation("查询我创建的队伍")
	@GetMapping("/list/my/create")
	public BaseResponse<List<TeamUserVO>> listMyCreateTeams(TeamQuery teamQuery, HttpServletRequest request) {
		if (teamQuery == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User loginUser = userService.getLoginUser(request);
		teamQuery.setUserId(loginUser.getId());
		List<TeamUserVO> teamList = teamService.listTeams(teamQuery, true);
		return ResultUtils.success(teamList, "获取分页数据成功");
	}

	@ApiOperation("查询我加入的队伍")
	@GetMapping("/list/my/join")
	public BaseResponse<List<TeamUserVO>> listMyJoinTeams(TeamQuery teamQuery, HttpServletRequest request) {
		if (teamQuery == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User loginUser = userService.getLoginUser(request);
		List<UserTeam> userTeamList = userTeamService.list(new QueryWrapper<UserTeam>().eq("userId", loginUser.getId()));
		List<Long> idList = new ArrayList<>(userTeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId)).keySet());
		teamQuery.setIdList(idList);
		List<TeamUserVO> teamList = teamService.listTeams(teamQuery, true);
		return ResultUtils.success(teamList, "获取分页数据成功");
	}

}

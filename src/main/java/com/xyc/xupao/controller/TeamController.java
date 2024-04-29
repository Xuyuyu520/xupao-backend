package com.xyc.xupao.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyc.xupao.common.BaseResponse;
import com.xyc.xupao.common.ErrorCode;
import com.xyc.xupao.common.ResultUtils;
import com.xyc.xupao.exception.BusinessException;
import com.xyc.xupao.model.domain.Team;
import com.xyc.xupao.model.domain.User;
import com.xyc.xupao.model.dto.TeamQuery;
import com.xyc.xupao.model.request.TeamAddRequest;
import com.xyc.xupao.model.request.TeamJoinRequest;
import com.xyc.xupao.model.request.TeamUpdateRequest;
import com.xyc.xupao.model.vo.TeamUserVO;
import com.xyc.xupao.service.TeamService;
import com.xyc.xupao.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
@CrossOrigin(origins = {"http://localhost:5173"})
public class TeamController {

	@Resource
	private UserService userService;

	@Resource
	private TeamService teamService;

	@ApiOperation("队伍创建方法")
	@PostMapping("/add")
	public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest, HttpServletRequest request) {
		if (teamAddRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User loginUser = userService.getLoginUser(request);
		Team team = new Team();
		BeanUtils.copyProperties(teamAddRequest, team);
		long teamId = teamService.addTeam(team, loginUser);
		return ResultUtils.success(teamId);
	}

	@PostMapping("/delete/{id}")
	public BaseResponse<Boolean> deleteTeam(@PathVariable Long id) {
		if (id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		boolean result = teamService.removeById(id);
		if (!result) {
			throw new BusinessException(ErrorCode.SYSTEM_ERROR, "插入失败");
		}
		return ResultUtils.success(true);
	}

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
		return ResultUtils.success(true);
	}

	@GetMapping("/get")
	public BaseResponse<Team> getTeamById(Long id) {
		if (id <= 0) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		Team team = teamService.getById(id);
		if (team == null) {
			throw new BusinessException(ErrorCode.NULL_ERROR);
		}
		return ResultUtils.success(team);
	}

	@GetMapping("/list")
	public BaseResponse<List<TeamUserVO>> listTeam(TeamQuery teamQuery, HttpServletRequest request) {
		if (teamQuery == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		boolean admin = userService.isAdmin(request);
		List<TeamUserVO> teamList = teamService.listTeams(teamQuery, admin);
		return ResultUtils.success(teamList);
	}

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

	@PostMapping("join")
	public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest, HttpServletRequest request) {
		if (teamJoinRequest == null) {
			throw new BusinessException(ErrorCode.PARAMS_ERROR);
		}
		User loginUser = userService.getLoginUser(request);
		boolean result = teamService.joinTeam(teamJoinRequest,loginUser);
		return ResultUtils.success(result);
	}
}

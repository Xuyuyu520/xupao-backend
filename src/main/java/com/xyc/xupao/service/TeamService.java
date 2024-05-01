package com.xyc.xupao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xyc.xupao.model.domain.Team;
import com.xyc.xupao.model.domain.User;
import com.xyc.xupao.model.dto.TeamQuery;
import com.xyc.xupao.model.request.TeamJoinRequest;
import com.xyc.xupao.model.request.TeamQuitRequest;
import com.xyc.xupao.model.request.TeamUpdateRequest;
import com.xyc.xupao.model.vo.TeamUserVO;

import java.util.List;

/**
* @author 27178
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-04-29 12:20:32
*/
public interface TeamService extends IService<Team> {

	/**
	 * 创建队伍
	 * @param team
	 * @param loginUser
	 * @return
	 */
	public long addTeam(Team team, User loginUser);

	/**
	 * 搜索队伍
	 *
	 * @param teamQuery
	 * @param admin
	 * @return
	 */
	List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean admin);

	/**
	 * 修改队伍
	 *
	 * @param teamUpdateRequest
	 * @param loginUser
	 * @return
	 */
	boolean updateTeam(TeamUpdateRequest teamUpdateRequest, User loginUser);

	/**
	 * 加入队伍
	 *
	 * @param teamJoinRequest
	 * @param loginUser
	 * @return
	 */
	boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

	/**
	 * 退出队伍
	 * @param teamQuitRequest
	 * @param loginUser
	 * @return
	 */
	boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

	/**
	 * 删除队伍
	 *
	 * @param id
	 * @param loginUser
	 * @return
	 */
	boolean deleteTeam(Long id, User loginUser);
}

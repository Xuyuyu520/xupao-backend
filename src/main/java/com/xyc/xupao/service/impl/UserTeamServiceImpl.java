package com.xyc.xupao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xyc.xupao.model.domain.UserTeam;
import com.xyc.xupao.service.UserTeamService;
import com.xyc.xupao.mapper.UserTeamMapper;
import org.springframework.stereotype.Service;

/**
* @author 27178
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2024-04-29 12:23:26
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService{

}





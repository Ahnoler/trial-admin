package com.ruoyi.trial.service.impl;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.trial.mapper.HomePageMapper;
import com.ruoyi.trial.mapper.TrialTaskDetailProdMapper;
import com.ruoyi.trial.service.HomePageService;
import com.ruoyi.trial.domain.VO.HomePageVO;
import com.ruoyi.trial.domain.VO.ProjectVO;
import com.ruoyi.trial.domain.VO.TrialCardVO;
import com.ruoyi.trial.domain.VO.TrialTaskVO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class HomePageServiceImpl implements HomePageService {
    private final HomePageMapper homePageMapper;
    private final TrialTaskDetailProdMapper trialTaskDetailProdMapper;
    @Override
    public HomePageVO selectHomePage(LoginUser loginUser,String year,String month) {
        String deptId = loginUser.getUser().getDeptId().toString();

        //查询任务开启和结束
        ProjectVO projectVO = homePageMapper.selectTaskBeginAndEnd(year, month);
        //查询项目统计数据
        projectVO.setTotal(homePageMapper.selectProjectCount(year,month));

        //查询卡片填写
        ProjectVO cardFill = homePageMapper.selectCradFillBeginAndEnd(year,month);
        projectVO.setCardBegin(cardFill.getCardBegin());
        projectVO.setCardEnd(cardFill.getCardEnd());
        //查询任务变更
        ProjectVO taskChange = homePageMapper.selectChangeAndShunt(year,month);
        projectVO.setTaskChangeBegin(taskChange.getTaskChangeBegin());
        projectVO.setTaskChangeEnd(taskChange.getTaskChangeEnd());
        projectVO.setTaskShuntBegin(taskChange.getTaskShuntBegin());
        projectVO.setTaskShuntEnd(taskChange.getTaskShuntEnd());

        //查询项目各月数据
        TrialCardVO projectNum = homePageMapper.selectProjectMonth(year, null);
        //查零件各月数据
        TrialCardVO card = homePageMapper.selectTaskDetailMonth(year, null);


        HomePageVO homePageVO = new HomePageVO();

        homePageVO.setProjectVO(projectVO);
        homePageVO.setParoject(projectNum);
        homePageVO.setCard(card);
        return homePageVO;
    }

    @Override
    public List<TrialTaskVO> selectExamineTask(LoginUser loginUser, String year, String month) {
        return homePageMapper.selectExamineTask(year, month);
    }
}

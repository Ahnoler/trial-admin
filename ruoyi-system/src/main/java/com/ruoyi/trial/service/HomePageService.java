package com.ruoyi.trial.service;

import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.trial.domain.VO.HomePageVO;
import com.ruoyi.trial.domain.VO.TrialTaskVO;

import java.util.List;


public interface HomePageService {

    HomePageVO selectHomePage(LoginUser loginUser, String year,String month);

    List<TrialTaskVO> selectExamineTask(LoginUser loginUser, String year, String month);
}

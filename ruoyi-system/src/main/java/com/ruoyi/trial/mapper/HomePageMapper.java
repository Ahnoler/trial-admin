package com.ruoyi.trial.mapper;


import com.ruoyi.trial.domain.VO.ProjectVO;
import com.ruoyi.trial.domain.VO.TrialCardVO;
import com.ruoyi.trial.domain.VO.TrialTaskVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HomePageMapper {
    //查询项目总量
    Integer selectProjectCount(@Param("year") String year, @Param("month") String month);
    //查询项目各月新建
    TrialCardVO selectProjectMonth(@Param("year") String year, @Param("month") String month);
    //查询任务启动/结束
    ProjectVO selectTaskBeginAndEnd(@Param("year") String year, @Param("month") String month);
    //查询卡片填写
    ProjectVO selectCradFillBeginAndEnd(@Param("year") String year, @Param("month") String month);
    //查询程序变更和零件分流
    ProjectVO selectChangeAndShunt(@Param("year") String year, @Param("month") String month);
    //查询零件每月数量
    TrialCardVO selectTaskDetailMonth(@Param("year") String year, @Param("month") String month);

    List<TrialTaskVO>  selectExamineTask(@Param("year") String year, @Param("month") String month);


}

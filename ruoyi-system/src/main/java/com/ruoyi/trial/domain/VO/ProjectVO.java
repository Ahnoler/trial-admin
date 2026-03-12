package com.ruoyi.trial.domain.VO;

import lombok.Data;

@Data
public class ProjectVO {
    //项目总量
    private Integer total;
    //项目总量
    private Integer taskTotal;
    //任务启动
    private Integer taskBegin;
    //任务结束
    private Integer taskEnd;
    //卡片填写完成
    private Integer cardBegin;
    //卡片填写未完成
    private Integer cardEnd;
    //程序变更完成
    private Integer taskChangeBegin;
    //程序变更未完成
    private Integer taskChangeEnd;
    //零件分流完成
    private Integer taskShuntBegin;
    //零件分流未完成
    private Integer taskShuntEnd;
}

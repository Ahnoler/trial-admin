package com.ruoyi.trial.domain.VO;

import lombok.Data;
import com.ruoyi.trial.domain.TrialTaskProd;
import com.ruoyi.trial.domain.TrialTaskDetailProd;

import java.util.List;

@Data
public class TrialTaskVO extends TrialTaskProd {
    private List<TrialTaskDetailProd> list;
}

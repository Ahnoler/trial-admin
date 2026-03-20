package com.ruoyi.trial.mapper;

import java.util.List;
import com.ruoyi.trial.domain.OverdueAlert;
import org.apache.ibatis.annotations.Param;

public interface OverdueAlertMapper
{
    public OverdueAlert selectOverdueAlertById(Long alertId);

    public List<OverdueAlert> selectOverdueAlertList(OverdueAlert overdueAlert);

    public int insertOverdueAlert(OverdueAlert overdueAlert);

    public int updateOverdueAlert(OverdueAlert overdueAlert);

    public int deleteOverdueAlertById(Long alertId);

    public int deleteOverdueAlertByIds(Long[] alertIds);

    public OverdueAlert selectExistingAlert(@Param("alertType") String alertType, @Param("targetId") Long targetId, @Param("alertStatus") String alertStatus);

    public int updateAlertStatusBatch(@Param("alertIds") List<Long> alertIds, @Param("alertStatus") String alertStatus, @Param("handler") String handler, @Param("handleRemark") String handleRemark);

    public int countPendingAlerts();
}

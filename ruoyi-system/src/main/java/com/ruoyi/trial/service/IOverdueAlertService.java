package com.ruoyi.trial.service;

import java.util.List;
import com.ruoyi.trial.domain.OverdueAlert;

public interface IOverdueAlertService
{
    public OverdueAlert selectOverdueAlertById(Long alertId);

    public List<OverdueAlert> selectOverdueAlertList(OverdueAlert overdueAlert);

    public int insertOverdueAlert(OverdueAlert overdueAlert);

    public int updateOverdueAlert(OverdueAlert overdueAlert);

    public int deleteOverdueAlertById(Long alertId);

    public int deleteOverdueAlertByIds(Long[] alertIds);

    public int handleAlerts(Long[] alertIds, String handler, String handleRemark);

    public int ignoreAlerts(Long[] alertIds, String handler, String handleRemark);

    public int countPendingAlerts();

    public int executeOverdueCheck();
}

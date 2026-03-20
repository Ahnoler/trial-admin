package com.ruoyi.trial.service.impl;

import com.ruoyi.common.exception.OptimisticLockException;
import com.ruoyi.trial.domain.TrialTaskProd;
import com.ruoyi.trial.mapper.TrialTaskProdMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OptimisticLockTest {

    @Mock
    private TrialTaskProdMapper trialTaskProdMapper;

    @InjectMocks
    private TrialTaskProdServiceImpl trialTaskProdService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateTrialTaskProd_Success() {
        TrialTaskProd task = new TrialTaskProd();
        task.setTaskId(1L);
        task.setVersion(0);
        task.setTitle("测试任务");

        when(trialTaskProdMapper.updateTrialTaskProd(any())).thenReturn(1);

        int result = trialTaskProdService.updateTrialTaskProd(task);

        assertEquals(1, result);
        verify(trialTaskProdMapper, times(1)).updateTrialTaskProd(any(TrialTaskProd.class));
    }

    @Test
    void testUpdateTrialTaskProd_OptimisticLockFailure() {
        TrialTaskProd task = new TrialTaskProd();
        task.setTaskId(1L);
        task.setVersion(0);
        task.setTitle("测试任务");

        when(trialTaskProdMapper.updateTrialTaskProd(any())).thenReturn(0);

        assertThrows(OptimisticLockException.class, () -> {
            trialTaskProdService.updateTrialTaskProd(task);
        });
    }

    @Test
    void testUpdateTrialTaskProd_VerifyUpdateTimeSet() {
        TrialTaskProd task = new TrialTaskProd();
        task.setTaskId(1L);
        task.setVersion(5);

        when(trialTaskProdMapper.updateTrialTaskProd(any())).thenReturn(1);

        trialTaskProdService.updateTrialTaskProd(task);

        verify(trialTaskProdMapper).updateTrialTaskProd(argThat(t -> {
            TrialTaskProd updatedTask = (TrialTaskProd) t;
            return updatedTask.getUpdateTime() != null;
        }));
    }

    @Test
    void testUpdateTrialTaskProd_MapperCalledWithCorrectTaskId() {
        TrialTaskProd task = new TrialTaskProd();
        task.setTaskId(100L);
        task.setVersion(0);

        when(trialTaskProdMapper.updateTrialTaskProd(any())).thenReturn(1);

        trialTaskProdService.updateTrialTaskProd(task);

        verify(trialTaskProdMapper).updateTrialTaskProd(argThat(t -> {
            TrialTaskProd argTask = (TrialTaskProd) t;
            return argTask.getTaskId().equals(100L);
        }));
    }
}

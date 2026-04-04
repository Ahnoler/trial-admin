package com.ruoyi.web.controller.trial;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.framework.web.service.TokenService;
import com.ruoyi.trial.domain.TaskStatus;
import com.ruoyi.trial.domain.TrialTaskProd;
import com.ruoyi.trial.service.ITrialTaskProdService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * TrialTaskProdController unit tests (MockMvc standalone).
 */
@ExtendWith(MockitoExtension.class)
class TrialTaskProdControllerTest {

    @Mock
    private ITrialTaskProdService trialTaskProdService;

    @Mock
    private TokenService tokenService;

    private TrialTaskProdController controller;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        RequestContextHolder.setRequestAttributes(
                new ServletRequestAttributes(new MockHttpServletRequest()));
        controller = new TrialTaskProdController();
        ReflectionTestUtils.setField(controller, "trialTaskProdService", trialTaskProdService);
        ReflectionTestUtils.setField(controller, "tokenService", tokenService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setMessageConverters(new MappingJackson2HttpMessageConverter())
                .build();
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void scanQrCode_whenNotFound_returnsError() throws Exception {
        when(trialTaskProdService.selectTrialTaskProdByQrCode("unknown")).thenReturn(null);

        mockMvc.perform(get("/trial/prod/scan/unknown"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value(containsString("\u672a\u627e\u5230")));
    }

    @Test
    void scanQrCode_whenFinished_returnsError() throws Exception {
        TrialTaskProd task = new TrialTaskProd();
        task.setTaskId(1L);
        task.setStatus(TaskStatus.FINISHED.getCode());
        when(trialTaskProdService.selectTrialTaskProdByQrCode("q1")).thenReturn(task);

        mockMvc.perform(get("/trial/prod/scan/q1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value(containsString("\u5df2\u7ed3\u675f")));
    }

    @Test
    void scanQrCode_whenOk_returnsTask() throws Exception {
        TrialTaskProd task = new TrialTaskProd();
        task.setTaskId(10L);
        task.setStatus(TaskStatus.NORMAL.getCode());
        task.setTitle("card-a");
        when(trialTaskProdService.selectTrialTaskProdByQrCode("q1")).thenReturn(task);

        mockMvc.perform(get("/trial/prod/scan/q1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.taskId").value(10))
                .andExpect(jsonPath("$.data.title").value("card-a"));
    }

    @Test
    void edit_whenOldTaskFinished_returnsErrorAndSkipsUpdate() throws Exception {
        TrialTaskProd body = new TrialTaskProd();
        body.setTaskId(1L);
        body.setTitle("new");

        TrialTaskProd oldNode = new TrialTaskProd();
        oldNode.setTaskId(1L);
        oldNode.setStatus(TaskStatus.FINISHED.getCode());
        when(trialTaskProdService.selectTrialTaskProdByTaskId(1L)).thenReturn(oldNode);

        mockMvc.perform(put("/trial/prod")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.msg").value(
                        "\u4efb\u52a1\u5df2\u7ed3\u675f\uff0c\u65e0\u6cd5\u4fee\u6539"));

        verify(trialTaskProdService, never()).updateTrialTaskProd(any());
    }

    @Test
    void disable_whenNotFound_returnsError() throws Exception {
        TrialTaskProd body = new TrialTaskProd();
        body.setTaskId(99L);
        when(trialTaskProdService.selectTrialTaskProdByTaskId(99L)).thenReturn(null);

        mockMvc.perform(post("/trial/prod/disable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(
                        "\u672a\u627e\u5230\u5bf9\u5e94\u4efb\u52a1"));

        verify(trialTaskProdService, never()).updateTrialTaskProd(any());
    }

    @Test
    void disable_whenFinished_returnsError() throws Exception {
        TrialTaskProd body = new TrialTaskProd();
        body.setTaskId(2L);
        TrialTaskProd oldNode = new TrialTaskProd();
        oldNode.setTaskId(2L);
        oldNode.setStatus(TaskStatus.FINISHED.getCode());
        when(trialTaskProdService.selectTrialTaskProdByTaskId(2L)).thenReturn(oldNode);

        mockMvc.perform(post("/trial/prod/disable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(
                        "\u4efb\u52a1\u5df2\u7ed3\u675f\uff0c\u65e0\u6cd5\u505c\u7528"));

        verify(trialTaskProdService, never()).updateTrialTaskProd(any());
    }

    @Test
    void enable_whenFinished_returnsError() throws Exception {
        TrialTaskProd body = new TrialTaskProd();
        body.setTaskId(3L);
        TrialTaskProd oldNode = new TrialTaskProd();
        oldNode.setTaskId(3L);
        oldNode.setStatus(TaskStatus.FINISHED.getCode());
        when(trialTaskProdService.selectTrialTaskProdByTaskId(3L)).thenReturn(oldNode);

        mockMvc.perform(post("/trial/prod/enable")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(
                        "\u4efb\u52a1\u5df2\u7ed3\u675f\uff0c\u65e0\u6cd5\u542f\u7528"));
    }

    @Test
    void over_setsStatusFinishedOnUpdate() throws Exception {
        TrialTaskProd body = new TrialTaskProd();
        body.setTaskId(5L);

        TrialTaskProd oldNode = new TrialTaskProd();
        oldNode.setTaskId(5L);
        oldNode.setStatus(TaskStatus.NORMAL.getCode());
        when(trialTaskProdService.selectTrialTaskProdByTaskId(5L)).thenReturn(oldNode);
        when(trialTaskProdService.updateTrialTaskProd(any())).thenReturn(1);

        mockMvc.perform(post("/trial/prod/over")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        ArgumentCaptor<TrialTaskProd> captor = ArgumentCaptor.forClass(TrialTaskProd.class);
        verify(trialTaskProdService).updateTrialTaskProd(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(TaskStatus.FINISHED.getCode());
    }

    @Test
    void flow_whenSuccess_returnsOk() throws Exception {
        TrialTaskProd body = new TrialTaskProd();
        body.setTaskId(7L);
        body.setCurrentSerialNo(1);
        when(trialTaskProdService.flowTrialTaskProd(any(), anyString())).thenReturn(1);

        LoginUser loginUser = buildLoginUser(100L);
        when(tokenService.getLoginUser(any())).thenReturn(loginUser);

        mockMvc.perform(post("/trial/prod/flow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));
    }

    @Test
    void flow_whenServiceThrows_returnsGenericError() throws Exception {
        TrialTaskProd body = new TrialTaskProd();
        body.setTaskId(7L);
        when(trialTaskProdService.flowTrialTaskProd(any(), anyString()))
                .thenThrow(new RuntimeException("x"));

        LoginUser loginUser = buildLoginUser(100L);
        when(tokenService.getLoginUser(any())).thenReturn(loginUser);

        mockMvc.perform(post("/trial/prod/flow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value(
                        "\u53d8\u66f4\u64cd\u4f5c\u5931\u8d25\uff01"));
    }

    @Test
    void fork_returnsServiceMessageOnException() throws Exception {
        TrialTaskProd body = new TrialTaskProd();
        body.setTaskId(8L);
        when(trialTaskProdService.forkTrialTaskProdWithLog(any(), anyString()))
                .thenThrow(new RuntimeException("fork-invalid"));

        LoginUser loginUser = buildLoginUser(100L);
        when(tokenService.getLoginUser(any())).thenReturn(loginUser);

        mockMvc.perform(post("/trial/prod/fork")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("fork-invalid"));
    }

    @Test
    void printDetail_returnsViewNameAndModel() throws Exception {
        Map<String, Object> printData = new HashMap<>();
        printData.put("foo", "bar");
        when(trialTaskProdService.preparePrintData(eq(1L), eq(1L), eq("100"))).thenReturn(printData);

        LoginUser loginUser = buildLoginUser(100L);
        when(tokenService.getLoginUser(any())).thenReturn(loginUser);

        mockMvc.perform(get("/trial/prod/printDetail/1").param("printType", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("print-trial-task"));
    }

    private static LoginUser buildLoginUser(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(sysUser);
        return loginUser;
    }
}

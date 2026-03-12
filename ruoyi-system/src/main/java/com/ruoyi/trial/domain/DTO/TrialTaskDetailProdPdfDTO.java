package com.ruoyi.trial.domain.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "TrialTaskDetailProdPdfDTO", description = "试制任务产品详情Pdf数据传输类")
public class TrialTaskDetailProdPdfDTO {
    /**
     * ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long id;
    /**
     * 流转程序
     */
    @Schema(description = "流转程序", example = "领料1")
    private String program;
    /**
     * 名称
     */
    @Schema(description = "名称", example = "测试")
    private String name;
    /**
     * 图号
     */
    @Schema(description = "图号", example = "K0001")
    private String figure;
    /**
     * 试制数量
     */
    @Schema(description = "试制数量", example = "10")
    private String trial_quantity;
    /**
     * 送检数量
     */
    @Schema(description = "送检数量", example = "8")
    private String inspection_quantity;
    /**
     * 制造质量状态
     */
    @Schema(description = "制造质量状态", example = "")
    private String manufacturing_quality_status;
    /**
     * 制造区域
     */
    @Schema(description = "制造区域", example = "第一车间")
    private String manufacturing_area;
    /**
     * 负责人
     */
    @Schema(description = "负责人", example = "张三")
    private String director;
    /**
     * 负责人电话
     */
    @Schema(description = "负责人电话", example = "12345678910")
    private String director_tel;
    /**
     * 工艺质量状态
     */
    @Schema(description = "工艺质量状态", example = "")
    private String process_quality_status;
    /**
     * me负责人
     */
    @Schema(description = "me负责人", example = "李四")
    private String me_director;
    /**
     * me负责人电话
     */
    @Schema(description = "me负责人电话", example = "12345678910")
    private String me_director_tel;
    /**
     * 备注
     */
    @Schema(description = "备注", example = "无")
    private String notes;
}

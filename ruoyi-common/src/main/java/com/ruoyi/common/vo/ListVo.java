package com.ruoyi.common.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ListVo {
    String left_index;
    List<TestVO> listInfo = new ArrayList<>();
}

package com.lx.kettle.common.tootik;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 运行KettleJOb任务状态信息
 */
@Getter
@AllArgsConstructor
public enum JobStatusEnum {
    RUNING(1, "正在运行"), STOP(2, "停止");
    private int code;
    private String desc;
}

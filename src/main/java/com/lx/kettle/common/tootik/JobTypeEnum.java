package com.lx.kettle.common.tootik;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 *
 */
@Getter
@AllArgsConstructor
public enum JobTypeEnum {
    DB(1, "数据库资源库"), FILE(2, "上传的文件");
    private int code;
    private String desc;
}

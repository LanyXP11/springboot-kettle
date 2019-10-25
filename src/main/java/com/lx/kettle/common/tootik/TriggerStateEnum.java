package com.lx.kettle.common.tootik;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TriggerStateEnum {
    NONE("停止", "NONE"), NORMAL("空闲", "NORMAL"), PAUSED("暂停", "PAUSED"), COMPLETE("完成", "COMPLETE"), ERROR("错误", "ERROR"), BLOCKED("正在运行", "BLOCKED");
    private String desc;
    private String code;

    /**
     * 根据code返回相关描述
     *
     * @param code
     * @return
     */
    public static String getTriggerStateDescByCode(String code) {
        for (TriggerStateEnum companyCategory : TriggerStateEnum.values()) {
            if (companyCategory.getCode().equals(code)) {
                return companyCategory.desc;
            }
        }
        return null;
    }
}

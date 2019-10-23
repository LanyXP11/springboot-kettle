package com.lx.kettle.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lx.kettle.common.tootik.Constant;
import com.lx.kettle.core.model.KUser;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;

/***
 * create by chenjiang on 2019/10/19 0019
 */
@Slf4j
public class JSONUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成JSON
     *
     * @param object
     * @return
     */
    public static String objectToJson(Object object) {
        try {
            MAPPER.setDateFormat(new SimpleDateFormat(Constant.DEFAULT_DATE_FORMAT));
            return MAPPER.writeValueAsString(object);
        } catch (Exception e) {
            log.error("[转换JSON出现异常:{}]", e);
        }
        return null;
    }

    public static void main(String[] args) {
        KUser user = KUser.builder().addUser(1).uAccount("11").build();
        System.out.println(JSONUtils.objectToJson(user));
    }
}

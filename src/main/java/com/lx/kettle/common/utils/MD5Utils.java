package com.lx.kettle.common.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Created by chenjiang on 2019/10/21
 * <P>
 *     加密
 * </P>
 */
public class MD5Utils {
    private static final String salt = "kettlespringboot";
    /**
     * 加密字符串
     * @param password 要加密的明文
     * @param isAddSalt 是否加默认盐
     * @return 加密之后的结果
     */
    public static String Encrypt(String password, boolean isAddSalt){
        if (StringUtils.isNotEmpty(password)){
            if (isAddSalt){
                return DigestUtils.md5Hex(DigestUtils.md5(password + salt));
            } else {
                return DigestUtils.md5Hex(DigestUtils.md5(password));
            }
        }
        return null;
    }
    /**
     * MD5加盐加密
     * @param password 要加密的明文
     * @param salt 盐
     * @return 加密之后的结果
     */
    public static String Encrypt(String password, String salt){
        if (StringUtils.isNotEmpty(password)){
            return DigestUtils.md5Hex(DigestUtils.md5(password + salt));
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(MD5Utils.Encrypt("admin", true).equals("2a14bb3f432498ca48866b4642df480f"));
    }

}

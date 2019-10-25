package com.lx.kettle.web.quartz.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by chenjiang on 2019/10/25
 */
@Builder
@AllArgsConstructor
@Getter
@Setter
public class DBConnectionModel {
    private String connectionDriveClassName;
    private String connectionUrl;
    private String connectionUser;
    private String connectionPassword;
}

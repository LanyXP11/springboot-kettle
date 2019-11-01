package com.lx.kettle.core.dto.kettle;

import lombok.*;

import java.io.Serializable;

/**
 * Created by chenjiang on 2019/10/31
 */

@Getter
@Setter
@ToString
public class RepositoryTree implements Serializable {
    private String id;
    private String parent;
    private String text;
    private String icon;
    private Object state;
    private String type;
    private boolean isLasted;
    private String path;

}

package com.lx.kettle.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import org.beetl.sql.core.annotatoin.Table;

@Builder
@Table(name = "k_repository_type")
@AllArgsConstructor
@ToString
public class KRepositoryType extends BaseModel {

    private Integer repositoryTypeId;
    private String repositoryTypeCode;
    private String repositoryTypeDes;

    public KRepositoryType() {

    }

    public Integer getRepositoryTypeId() {
        return repositoryTypeId;
    }

    public void setRepositoryTypeId(Integer repositoryTypeId) {
        this.repositoryTypeId = repositoryTypeId;
    }

    public String getRepositoryTypeCode() {
        return repositoryTypeCode;
    }

    public void setRepositoryTypeCode(String repositoryTypeCode) {
        this.repositoryTypeCode = repositoryTypeCode;
    }

    public String getRepositoryTypeDes() {
        return repositoryTypeDes;
    }

    public void setRepositoryTypeDes(String repositoryTypeDes) {
        this.repositoryTypeDes = repositoryTypeDes;
    }


}
package com.lx.kettle.core.mapper;

import com.lx.kettle.core.model.KJob;
import org.beetl.sql.core.annotatoin.SqlStatement;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.List;

/**
 * Created by chenjiang on 2019/10/23
 */
public interface KJobDao extends BaseMapper<KJob> {

    /**
     * 分页查询显示数据
     */
    @SqlStatement(params = "kJob,start,size")
    List<KJob> pageQuery(KJob template, Integer start, Integer size);

    /**
     * 根据条件统计数量
     *
     * @param kJob
     * @return
     */
    @SqlStatement(params = "kJob")
    long allCount(KJob kJob);
}

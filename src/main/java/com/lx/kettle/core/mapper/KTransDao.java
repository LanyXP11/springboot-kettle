package com.lx.kettle.core.mapper;

import com.lx.kettle.core.model.KTrans;
import org.beetl.sql.core.annotatoin.SqlStatement;
import org.beetl.sql.core.mapper.BaseMapper;

import java.util.Date;
import java.util.List;

/**
 * Created by chenjiang on 2019/10/23
 */
public interface KTransDao extends BaseMapper<KTrans> {
    /**
     * 统计条数
     *
     * @param kTrans
     * @return
     */
    @SqlStatement(params = "kTrans")
    Long allCount(KTrans kTrans);

    /**
     * 加载转换首页
     *
     * @param kTrans
     * @param start
     * @param size
     * @return
     */
    @SqlStatement(params = "kTrans,start,size")
    List<KTrans> pageQuery(KTrans kTrans, Integer start, Integer size);
}

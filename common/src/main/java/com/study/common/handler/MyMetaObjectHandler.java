package com.study.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.study.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 元对象处理 -- 填充器
 * 自动填充公共字段
 *
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Autowired
    private HttpServletRequest request;

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("新增时填充---");
        // 获取到需要被填充的字段值
        Object createBy = getFieldValByName("createBy", metaObject);
        Object createDate = getFieldValByName("createTime", metaObject);
        // 如果不手动设置，就自动填充默认值
        if (createBy == null){
            this.setFieldValByName("createBy", JwtUtils.getUserNameByRequest(request), metaObject);
        }
        // 如果不手动设置，就自动填充默认值
        if (createDate == null){
            this.setFieldValByName("createTime", new Date(), metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("修改时填充---");
        // 获取到需要被填充的字段值
        Object updateBy = getFieldValByName("updateBy", metaObject);
        Object updateDate = getFieldValByName("updateDate", metaObject);
        // 如果不手动设置，就自动填充默认值
        if (updateBy == null){
            this.setFieldValByName("updateBy", JwtUtils.getUserNameByRequest(request), metaObject);
        }
        // 如果不手动设置，就自动填充默认值
        if (updateDate == null){
            this.setFieldValByName("updateTime",new Date(), metaObject);
        }
    }
}

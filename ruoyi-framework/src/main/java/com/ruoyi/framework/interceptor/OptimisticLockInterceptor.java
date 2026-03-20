package com.ruoyi.framework.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Properties;

@Slf4j
@Intercepts({
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class OptimisticLockInterceptor implements Interceptor {

    private static final String VERSION_FIELD = "version";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        
        if (SqlCommandType.UPDATE.equals(sqlCommandType)) {
            BoundSql boundSql = statementHandler.getBoundSql();
            String originalSql = boundSql.getSql();
            Object parameterObject = boundSql.getParameterObject();
            
            if (parameterObject != null && hasVersionField(parameterObject)) {
                Integer version = getVersion(parameterObject);
                if (version != null) {
                    String newSql = addOptimisticLockCondition(originalSql, version);
                    setFieldValue(boundSql, "sql", newSql);
                    incrementVersion(parameterObject);
                    log.debug("Optimistic lock applied: version = {}, sql = {}", version, newSql);
                }
            }
        }
        
        return invocation.proceed();
    }

    private boolean hasVersionField(Object obj) {
        try {
            Field versionField = obj.getClass().getDeclaredField(VERSION_FIELD);
            versionField.setAccessible(true);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

    private Integer getVersion(Object obj) {
        try {
            Field versionField = obj.getClass().getDeclaredField(VERSION_FIELD);
            versionField.setAccessible(true);
            return (Integer) versionField.get(obj);
        } catch (Exception e) {
            return null;
        }
    }

    private void incrementVersion(Object obj) {
        try {
            Field versionField = obj.getClass().getDeclaredField(VERSION_FIELD);
            versionField.setAccessible(true);
            Integer currentVersion = (Integer) versionField.get(obj);
            if (currentVersion != null) {
                versionField.set(obj, currentVersion + 1);
            }
        } catch (Exception e) {
            log.warn("Failed to increment version field", e);
        }
    }

    private String addOptimisticLockCondition(String originalSql, Integer version) {
        String lowerSql = originalSql.toLowerCase();
        
        if (lowerSql.contains("where")) {
            return originalSql + " AND version = " + version;
        } else {
            return originalSql + " WHERE version = " + version;
        }
    }

    private void setFieldValue(Object object, String fieldName, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);
        } catch (Exception e) {
            log.error("Failed to set field value: {}", fieldName, e);
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}

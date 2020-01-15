package com.bbs.annotation;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 配合com.bbs.util.EntityUtil使用，用户设置字段别名
 */
@Target(ElementType.FIELD)
@Retention(RUNTIME)
public @interface FieldAlias {
    String value();
}

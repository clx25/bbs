package com.bbs.util;

import com.bbs.annotation.FieldAlias;
import com.bbs.exception.custom.ReflectionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class EntityUtil {


    /**
     * 判断对象中是否所有字段为空，或所有基本类型都为0
     *
     * @param t 判断的目标对象
     * @return 所有字段全部是null或基本类型都是0返回true，有一个不为null或0返回false
     */
    public static boolean fieldsIsNull(Object t) {
        Class<?> tClass = t.getClass();

        Field[] tField = tClass.getDeclaredFields();
        for (Field field : tField) {
            field.setAccessible(true);
            if (!field.getType().isPrimitive()) {
                Object o;
                try {
                    o = field.get(t);
                } catch (IllegalAccessException e) {
                    throw new ReflectionException("从" + t + "获取" + field + "失败", e);
                }
                if (o != null) {
                    return false;
                }

            } else {

                int i;
                try {
                    i = (int) field.get(t);
                } catch (IllegalAccessException e) {
                    throw new ReflectionException("从" + t + "获取" + field + "失败", e);
                }

                if (i != 0) {
                    return false;
                }

            }

        }
        return true;
    }


    /**
     * 把一个list中的A实体类的字段的值拷贝到另一个实体类B的同名字段中，并返回B的list
     *
     * @param sl   源list
     * @param t    目标类型的字节码对象
     * @param <SL> 源list包含的实体类类型
     * @param <T>  目标实体类类型
     * @return 目标实体类list
     */
    public static <SL, T> List<T> copyToEntityList(List<SL> sl, Class<T> t) {
        List<T> list = new ArrayList<>();
        for (SL s : sl) {
            T tObject;
            try {
                tObject = t.newInstance();
            } catch (ReflectiveOperationException e) {
                throw new ReflectionException(t + "实例化失败", e);
            }
            CopyToEntity(s, tObject);
            list.add(tObject);

        }
        return list;
    }

    /**
     * 把s中字段的值赋值给t中的同名字段
     * org.apache.commons.beanutils.BeanUtils#copyProperties(java.lang.Object, java.lang.Object)可实现同功能
     *  如果使用@FieldAlias注解设置了别名，那么原名就无效
     *
     * @param s   数据来源对象
     * @param t   目标类的字节码对象
     * @param <S> 来源对象类型
     * @param <T> 目标对象类型
     */

    public static <S, T> void CopyToEntity(S s, T t) {

        //获取字节码对象
        Class<?> sClass = s.getClass();

        Class<?> tClass = t.getClass();
        Class<?> tSuperclass = tClass.getSuperclass();


        //获取字段
        Field[] sfields = sClass.getDeclaredFields();


        Field[] tFields = tClass.getDeclaredFields();
        if (!tSuperclass.getName().equals(Object.class.getName())) {
            Field[] tSuperclassFields = tSuperclass.getDeclaredFields();
            tFields = ArrayUtils.addAll(tFields, tSuperclassFields);
        }

        for (Field sField : sfields) {
            sField.setAccessible(true);
            String sFieldName = sField.getName();
            Object sValue;

            try {
                sValue = sField.get(s);
            } catch (IllegalAccessException e) {
                throw new ReflectionException("从" + s + "获取" + sField + "失败", e);
            }

            FieldAlias annotation = sField.getAnnotation(FieldAlias.class);
            if (annotation != null) {
                sFieldName = annotation.value();
            }

            for (Field tField : tFields) {
                tField.setAccessible(true);
                String tFieldName = tField.getName();
                //如果来源类字段名与目标类字段名相同，那么就给目标对象赋值
                if (sFieldName.equals(tFieldName)) {
                    try {
                        tField.set(t, sValue);
                    } catch (IllegalAccessException e) {
                        throw new ReflectionException("往" + t + "的" + tField + "字段注入" + sValue + "失败", e);
                    }
                }
            }
        }
    }


}

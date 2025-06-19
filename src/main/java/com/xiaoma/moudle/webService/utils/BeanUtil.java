package com.xiaoma.moudle.webService.utils;

import cn.hutool.core.collection.CollUtil;
import com.xiaoma.moudle.webService.entry.Diff;
import com.xiaoma.moudle.webService.support.BaseBeanCopier;
import com.xiaoma.moudle.webService.support.BeanProperty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.cglib.core.CodeGenerationException;
import org.springframework.cglib.core.Converter;
import org.springframework.util.Assert;

import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class BeanUtil extends BeanUtils {
    public BeanUtil() {
    }

    public static <T> T newInstance(Class<?> clazz) {
        return (T) instantiateClass(clazz);
    }

    public static <T> T newInstance(String clazzStr) {
        try {
            Class<?> clazz = Class.forName(clazzStr);
            return newInstance(clazz);
        } catch (ClassNotFoundException var2) {
            throw new RuntimeException(var2);
        }
    }

    public static Object getProperty(Object bean, String propertyName) {
        Assert.notNull(bean, "bean Could not null");
//        return BeanMap.create(bean).get(propertyName);
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(bean.getClass(), propertyName);
        Method readMethod = propertyDescriptor.getReadMethod();
        Assert.notNull(readMethod, bean.getClass() + "." + propertyName + " 没有get方法");
        try {
            return readMethod.invoke(bean);
        } catch (Exception e) {
            throw new RuntimeException("获取对象的值失败！" + bean.getClass() + "." + propertyName, e);
        }
    }

    public static void setProperty(Object bean, String propertyName, Object value) {
        Assert.notNull(bean, "bean Could not null");
//        return BeanMap.create(bean).get(propertyName);
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(bean.getClass(), propertyName);
        Method readMethod = propertyDescriptor.getWriteMethod();
        Assert.notNull(readMethod, bean.getClass() + "." + propertyName + " 没有get方法");
        try {
            readMethod.invoke(bean, value);
        } catch (Exception e) {
            throw new RuntimeException("获取对象的值失败！"+ bean.getClass() + "." + propertyName, e);
        }
    }

    public static <T> T clone(T source) {
        return (T) copy(source, source.getClass());
    }

    public static <T> T copy(Object source, Class<T> clazz) {
        BaseBeanCopier copier = BaseBeanCopier.create(source.getClass(), clazz, false);
        T to = newInstance(clazz);
        copier.copy(source, to, (Converter)null);
        return to;
    }

    public static <T> List<T> copyList(List<?> sourceList, Class<T> clazz) {
        List<T> resultList = new ArrayList<>(sourceList.size());
        if (CollectionUtil.isNotEmpty(sourceList)) {
//            resultList = sourceList.stream().map(source -> (T)BeanUtil.copy(source, clazz)).collect(Collectors.toList());
            for (Object source : sourceList) {
                resultList.add((T)copy(source, clazz));
            }
        }
        return resultList;
    }

    public static void copy(Object source, Object targetBean) {
        BaseBeanCopier copier = BaseBeanCopier.create(source.getClass(), targetBean.getClass(), false);
        copier.copy(source, targetBean, (Converter)null);
    }

    public static <T> T copyProperties(Object source, Class<T> target) throws BeansException {
        T to = newInstance(target);
        copyProperties(source, to);
        return to;
    }

    public static Map<String, Object> toMap(Object bean) {
        return BeanMap.create(bean);
    }

    public static <T> T toBean(Map<String, Object> beanMap, Class<T> valueType) {
        T bean = newInstance(valueType);
        BeanMap.create(bean).putAll(beanMap);
        return bean;
    }

    public static Object generator(Object superBean, BeanProperty... props) {
        Class<?> superclass = superBean.getClass();
        Object genBean = generator(superclass, props);
        copy(superBean, genBean);
        return genBean;
    }

    public static Object generator(Class<?> superclass, BeanProperty... props) {
        BeanGenerator generator = new BeanGenerator();
        generator.setSuperclass(superclass);
        generator.setUseCache(true);
        BeanProperty[] var3 = props;
        int var4 = props.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            BeanProperty prop = var3[var5];
            generator.addProperty(prop.getName(), prop.getType());
        }

        return generator.create();
    }

    public static PropertyDescriptor[] getBeanGetters(Class type) {
        return getPropertiesHelper(type, true, false);
    }

    public static PropertyDescriptor[] getBeanSetters(Class type) {
        return getPropertiesHelper(type, false, true);
    }

    private static PropertyDescriptor[] getPropertiesHelper(Class type, boolean read, boolean write) {
        try {
            PropertyDescriptor[] all = getPropertyDescriptors(type);
            if (read && write) {
                return all;
            } else {
                List<PropertyDescriptor> properties = new ArrayList(all.length);
                PropertyDescriptor[] var5 = all;
                int var6 = all.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    PropertyDescriptor pd = var5[var7];
                    if (read && pd.getReadMethod() != null) {
                        properties.add(pd);
                    } else if (write && pd.getWriteMethod() != null) {
                        properties.add(pd);
                    }
                }

                return (PropertyDescriptor[])properties.toArray(new PropertyDescriptor[0]);
            }
        } catch (BeansException var9) {
            throw new CodeGenerationException(var9);
        }
    }

    public static <T> List<T> deepCopy(List<T> src) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        List<T> dest = null;
        try {
            out = new ObjectOutputStream(byteOut);
            out.writeObject(src);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            in = new ObjectInputStream(byteIn);
            dest = (List<T>) in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return dest;
    }


    public static boolean checkFields(Object entity,String fieldName){
        try {
            Field field=entity.getClass().getDeclaredField(fieldName);
        }catch (NoSuchFieldException e) {
            return false;
        }
        return true;
    }

    public static <T> T toDtoBean(Map<String, Object> beanMap, Class<T> valueType) {
        T bean = newInstance(valueType);
        cn.hutool.core.bean.BeanUtil.fillBeanWithMapIgnoreCase(beanMap, bean, false);
        return bean;
    }

    public static <T> List<Diff> diff(T oldObj, T newObj, String... ignores) {
        if (Objects.isNull(oldObj) || Objects.isNull(newObj)) {
            throw new RuntimeException("比较差异的对象不能为空");
        }
        if (oldObj.getClass() != newObj.getClass()) {
            throw new RuntimeException("比较差异的对象类型必须相同");
        }
        Class<?> aClass = oldObj.getClass();
        List<Diff> list = new ArrayList<>();
        // 获取所有属性定义，并设置为允许访问
        Map<String, Field> fieldMap = getFieldMap(aClass);

        Set<String> defaultIgnoreSet = CollUtil.newHashSet("serialVersionUID");
        Set<String> ignoreSet = CollUtil.newHashSet(ignores);
        ignoreSet.addAll(defaultIgnoreSet);

        // 遍历 map，比较2个属性值
        fieldMap.forEach((k, v) -> {
            if (!ignoreSet.contains(k)) {
                try {
                    Object newVal = v.get(newObj);
                    Object oldVal = v.get(oldObj);
                    if( !((newVal==null && oldVal==null) || (newVal != null && newVal.equals(oldVal)))){
                        Diff diff = new Diff();
                        diff.setField(k);
                        if(oldVal!=null){
                            diff.setBefore(String.valueOf(oldVal));
                        }
                        if(newVal!=null){
                            diff.setAfter(String.valueOf(newVal));
                        }
                        list.add(diff);
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });
        return list;
    }

    /**
     * Object 转成 String 格式
     */
    private static String obj2str(Object obj) {
        String str;
        if (Objects.isNull(obj)) {
            str = null;
        } else if (obj instanceof Date) {
            str = DateUtil.formatDate((Date) obj);
        } else {
            str = obj.toString();
        }
        return str;
    }

    /**
     * 将类的所有 field，存入到 Map
     */
    private static Map<String, Field> getFieldMap(Class<?> cls) {
        HashMap<String, Field> map = new HashMap<>();
        for (Field field : cls.getDeclaredFields()) {
            field.setAccessible(true);
            map.put(field.getName(), field);
        }
        return map;
    }
}

package org.dee.file.excel.factory.impl;

import cn.hutool.core.bean.BeanUtil;
import org.dee.annotations.GetKey;
import org.dee.annotations.GetValue;
import org.dee.file.excel.entity.DeeExcelDist;
import org.dee.file.excel.factory.AbstractDeeExcelDistFactory;
import org.dee.utils.ConvertUtil;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EnumFactory extends AbstractDeeExcelDistFactory {

    @Override
    public List<DeeExcelDist> queryExcelDists(String classPath) {
        try{
            Map<String, String> enumValue = getEnumValue(classPath);
            //将枚举的key-value放入数组
            List<DeeExcelDist> list = new ArrayList<>();
            enumValue.forEach((k,v) -> {
                DeeExcelDist dist = new DeeExcelDist();
                dist.setType(classPath);
                dist.setKey(k);
                dist.setValue(v);
                list.add(dist);
            });
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public Map<String, String> getEnumValue(String classPath) {
        Map<String, String> enumValue = new HashMap<>();
        try{
            Class emClass = Class.forName(classPath);
            Object[] objects = emClass.getEnumConstants();

            String key = "";
            String value = "";
            Field[] fields = emClass.getDeclaredFields();
            for(Field field : fields){
                GetKey getkey = field.getAnnotation(GetKey.class);
                if(getkey != null){
                    key = field.getName();
                }
                GetValue getvalue = field.getAnnotation(GetValue.class);
                if(getvalue != null){
                    value = field.getName();
                }
            }

            for(Object object : objects){
                Map<String, Object> map = BeanUtil.beanToMap(object);
                enumValue.put(ConvertUtil.returnString(map.get(key)), ConvertUtil.returnString(map.get(value)));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return enumValue;
    }

}

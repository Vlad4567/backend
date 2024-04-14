package com.example.beautybook.model;

import com.example.beautybook.annotetion.IgnoreToSting;
import com.example.beautybook.annotetion.OnlyIdToString;
import java.lang.reflect.Field;
import lombok.SneakyThrows;

public abstract class BaseModel {
    @SneakyThrows
    public String toString(Object object) {
        Class<?> clazz = object.getClass();
        StringBuilder sb = new StringBuilder(clazz.getTypeName()).append("(");
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(IgnoreToSting.class)) {
                continue;
            }
            if (field.isAnnotationPresent(OnlyIdToString.class)) {
                Object idValue = null;
                Object fieldValue = field.get(object);

                if (fieldValue != null) {
                    try {
                        Field idField = fieldValue.getClass().getDeclaredField("id");
                        idField.setAccessible(true);
                        idValue = idField.get(fieldValue);
                    } catch (NoSuchFieldException e) {
                        idValue = "not found id";
                    }
                }

                sb.append(field.getName()).append("Id=").append(idValue).append(", ");
            } else {
                Object value;
                if (field.get(object) == null) {
                    value = null;
                } else {
                    value = field.get(object);
                }
                sb.append(field.getName()).append("=").append(value).append(", ");
            }
        }
        if (fields.length > 0) {
            sb.setLength(sb.length() - 2);
        }
        sb.append(")");
        return sb.toString();
    }
}

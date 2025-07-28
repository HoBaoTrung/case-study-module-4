package com.codegym.mobilestore.annotation;
import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Unique {
    String message() default "Giá trị đã tồn tại";
    Class<?> entityClass();  // Entity cần kiểm tra
    String fieldName();      // Tên trường trong entity
    String idField() default "id"; // Trường ID để loại trừ khi update
}
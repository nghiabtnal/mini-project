package org.example.miniproject.libs.datasource;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ReadOnlyDataSource
{

}


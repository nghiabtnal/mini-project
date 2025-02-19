package org.example.miniproject.libs.datasource;

import lombok.NonNull;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DynamicDataSourceTargetHolder
{
    private static final DynamicDataSourceTargetHolder singleton = new DynamicDataSourceTargetHolder();

    private final ThreadLocal<Object> target = new ThreadLocal<>();

    /**
     * コンストラクタ (生成を禁止).
     */
    private DynamicDataSourceTargetHolder()
    {

    }

    public static DynamicDataSourceTargetHolder get()
    {
        return singleton;
    }

    public Object getTarget()
    {
        return this.target.get();
    }

    public void setTarget(@NonNull Object _target)
    {
        this.target.set(_target);
    }

    public void clearTarget()
    {
        this.target.set(null);
    }
}


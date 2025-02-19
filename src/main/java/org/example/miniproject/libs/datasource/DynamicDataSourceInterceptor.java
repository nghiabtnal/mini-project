package org.example.miniproject.libs.datasource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DynamicDataSourceInterceptor implements Ordered
{
    private int order = 20;

    @Override
    public int getOrder()
    {
        return this.order;
    }

    public void setOrder(int _order)
    {
        this.order = _order;
    }

    @Around("@annotation(_readOnlyDataSource) && execution(* *(..))")
    public Object invokeAroundReadOnly(ProceedingJoinPoint _joinPoint, ReadOnlyDataSource _readOnlyDataSource) throws Throwable
    {
        Object currentTarget = DynamicDataSourceTargetHolder.get().getTarget();
//        Validate.validState(currentTarget == null, "dataSource is aready resolved. current dataSource is \"%s\"", currentTarget);

        DynamicDataSourceTargetHolder.get().setTarget(DynamicDataSource.Target.SLAVE);

        try {
            return _joinPoint.proceed();
        } finally {
            DynamicDataSourceTargetHolder.get().clearTarget();
        }
    }
}


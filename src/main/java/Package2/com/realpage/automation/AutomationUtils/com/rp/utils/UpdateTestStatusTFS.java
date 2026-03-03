package Package2.com.realpage.automation.AutomationUtils.com.rp.utils;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateTestStatusTFS {
    String productName() default "";
    String testCaseIds() default "";
}
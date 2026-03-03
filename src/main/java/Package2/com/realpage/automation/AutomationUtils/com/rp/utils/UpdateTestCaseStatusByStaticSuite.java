package Package2.com.realpage.automation.AutomationUtils.com.rp.utils;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface UpdateTestCaseStatusByStaticSuite {
    String testCaseIds() default "";
    String suiteId() default "";
}

package Package2.com.realpage.automation.AutomationUtils.com.rp.utils;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.*;

@Documented
@ExtendWith({UpdateTestCaseStatusExtension.class})
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface UpdateTestCaseStatus {
    String testCaseIds() default "";
    String suiteId() default "";
}

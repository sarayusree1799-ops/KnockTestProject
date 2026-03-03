package FrameWorkPackage.com.rp.automation.framework.annotations;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(METHOD)
public @interface AzureTestCaseId {
    String productName() default "";
    String[] testCaseIds() default {};
}

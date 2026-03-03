package FrameWorkPackage.com.rp.automation.framework.annotations;

import java.lang.annotation.*;

@Documented
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface AzureSuiteIDTestCaseId {

    /**The name of the project this test class should be place in. This attribute is ignored if @Test is
     * not at class level.
     *
     * @return The value (default empty)
     */
    String productName() default "";

    /* The list of test case ID belongs to
    *  @return The value
    */
    String[] testCaseIds() default {};
}

package me.dhcpcd.lisek.utils.commands.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FallbackMethod {
    /**
     * @deprecated Suggests players
     * @return
     */
    boolean acceptsPlayer() default false;
    /**
     * @description Allows to use by console
     * @return
     */
    boolean consoleAllowed() default true;
    /**
     * 
     * @description Suggest method.
     * @return Method name, accepts in invocation
     */
    String autoComplete() default "";
}

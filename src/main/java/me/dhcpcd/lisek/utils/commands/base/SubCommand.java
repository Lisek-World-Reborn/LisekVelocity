package me.dhcpcd.lisek.utils.commands.base;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface SubCommand {
    String name();

    /**
     * @description Suggests player for this subcommand
     * @return
     */
    boolean acceptsPlayer() default false;
    /**
     * @description Allows console to use this subcommand
     * @return
     */
    boolean acceptsConsole() default false;
    /**
     * @description Autocomplete method.
     * @return Method, accepts invocation as argument
     */
    String autoComplete() default "";

    /**
     * @description Calls on any argument
     * @return
     */
    boolean callOnAnyArgument() default false;
}

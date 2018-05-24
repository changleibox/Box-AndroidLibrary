/*
 * Copyright © 2017 CHANGLEI. All rights reserved.
 */

package net.box.app.library.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Box
 * @version v1.0
 * @since Jdk1.6 或 Jdk1.7
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IViewInject {

    int value() default -1;

}

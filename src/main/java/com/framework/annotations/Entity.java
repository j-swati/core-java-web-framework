package com.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
}


// @Retention(RetentionPolicy.RUNTIME) -- Keep this annotation available at runtime. Without runtime retention, reflection generally won't be able to see it at runtime.

// @Target(ElementType.TYPE) -- this annotation can be put on a type, such as a class, interface, enum, etc.

// @interface -- I'm defining a custom annotation called Entity.

// body empty -- marker annotation
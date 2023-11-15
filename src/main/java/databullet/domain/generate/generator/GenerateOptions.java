package databullet.domain.generate.generator;

import databullet.domain.definition.data.options.Options;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GenerateOptions {

  Class<? extends Options> value();
}

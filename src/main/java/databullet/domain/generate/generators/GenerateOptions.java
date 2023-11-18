package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.DataSpecType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface GenerateOptions {

  Class<? extends DataSpecType> value();
}

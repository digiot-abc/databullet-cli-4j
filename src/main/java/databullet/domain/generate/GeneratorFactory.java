package databullet.domain.generate;

import databullet.domain.definition.dataspec.DataSpecColumn;
import databullet.domain.definition.dataspec.DataSpecType;
import databullet.domain.definition.dataspec.types.*;
import databullet.domain.definition.generate.GenerateColumn;
import databullet.domain.definition.table.Column;
import databullet.domain.generate.generators.*;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public final class GeneratorFactory {
  private static final HashMap<Class<? extends DataSpecType>, Function<DataSpecColumn, Generator<?, ?>>>
      generatorCreationMap = new HashMap<>();

  static {

    Reflections reflections = new Reflections(Generator.class.getPackage().getName());
    Set<Class<? extends Generator>> generatorClasses = reflections.getSubTypesOf(Generator.class);

    for (Class<? extends Generator> generatorClass : generatorClasses) {
      GenerateOptions generateOptions = generatorClass.getAnnotation(GenerateOptions.class);
      if (generateOptions != null) {
        Class<? extends DataSpecType> typeClass = generateOptions.value();
        generatorCreationMap.put(typeClass, column -> createGeneratorInstance(generatorClass, typeClass, column));
      }
    }
  }

  public static Generator<?, ?> create(GenerateColumn column) {
    return create(column.getDataSpecColumn()).orElse(
        create(column.getTableColumn()).orElse(new StringGenerator(new StringType(1))));
  }

  public static Optional<Generator<?, ?>> create(DataSpecColumn dataSpecColumn) {
    if (dataSpecColumn.isEmpty()) {
      return Optional.empty();
    }
    Class<? extends DataSpecType> optionsClassInput = dataSpecColumn.getType().getClass();
    return Optional.of(generatorCreationMap.get(optionsClassInput).apply(dataSpecColumn));
  }

  public static Optional<Generator<?, ?>> create(Column column) {

    String type = column.getType().toLowerCase();
    switch (type) {
      // 文字列型
      case "bpchar":
      case "character varying":
      case "string":
      case "varchar":
      case "char":
      case "text":
        // 文字列ジェネレータを返す
        StringType stringType = new StringType(column.getDigit().getDigit());
        return Optional.of(new StringGenerator(stringType));

      // 数値型
      case "int":
      case "int4":
      case "int8":
      case "integer":
        // int/integer: 通常、-2,147,483,648 から 2,147,483,
        IntType intType1 = new IntType(-2147483648L, 2147483647L);
        return Optional.of(new IntGenerator(intType1));

      case "bigint":
        // bigint: 通常、-9,223,372,036,854,775,808 から 9,223,372,036,854,775,807
        IntType intType2 = new IntType(-9223372036854775808L, 9223372036854775807L);
        return Optional.of(new IntGenerator(intType2));

      case "smallint":
        // smallint: 通常、-32,768 から 32,767
        IntType intType3 = new IntType(-32768L, 32767L);
        return Optional.of(new IntGenerator(intType3));

      case "decimal":
      case "float8":
      case "numeric":
        // decimal/numeric: 桁数と小数点以下の桁数を指定する
        // 例: 最大10桁、小数点以下2桁の場合
        DecimalType decimalType = new DecimalType(10, 2);
        return Optional.of(new DecimalGenerator(decimalType));

      case "serial":
        SequenceType sequenceType = new SequenceType();
        sequenceType.setStart(1);
        sequenceType.setIncrement(1);
        return Optional.of(new SequenceGenerator(sequenceType));

      case "time": // TODO
      case "date":
        // 日付ジェネレータを返す
        DateType dateType = new DateType();
        return Optional.of(new DateGenerator(dateType));

      case "timestamp":
      case "datetime": // MySQL特有
        // タイムスタンプジェネレータを返す
        DateTimeType dateTimeType = new DateTimeType();
        return Optional.of(new DateTimeGenerator(dateTimeType));

      case "boolean":
      case "bool":
      case "bit": // OracleとMySQLで使用
        // ブーリアンジェネレータを返す
        BoolType boolType = new BoolType();
        return Optional.of(new BoolGenerator(boolType));

      case "bytea":
        ByteType byteType = new ByteType();
        return Optional.of(new ByteGenerator(byteType));

      default:
        System.out.println("未知の型: " + type);
        return Optional.empty();
//          throw new IllegalArgumentException("未知の数値型: " + type);
    }
  }

  private static Generator<?, ?> createGeneratorInstance(Class<? extends Generator> generatorClass,
                                                         Class<? extends DataSpecType> typeSpecClass,
                                                         DataSpecColumn column) {
    try {
      return generatorClass.getConstructor(typeSpecClass).newInstance(typeSpecClass.cast(column.getType()));
    } catch (Exception e) {
      throw new RuntimeException("Failed to create generator instance", e);
    }
  }
}

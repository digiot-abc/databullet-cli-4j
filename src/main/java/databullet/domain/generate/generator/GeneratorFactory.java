package databullet.domain.generate.generator;

import databullet.domain.definition.data.DataSpecColumn;
import databullet.domain.definition.data.options.*;
import databullet.domain.definition.generate.GenerateColumn;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Set;
import java.util.function.Function;

public final class GeneratorFactory {

  private static final HashMap<GenerateColumn, Generator<?, ?>> generatorMap = new HashMap<>();

  private static final HashMap<Class<? extends Options>, Function<DataSpecColumn, Generator<?, ?>>> generatorFactoryMap = new HashMap<>();

  static {

    Reflections reflections = new Reflections(Generator.class.getPackage().getName());
    Set<Class<? extends Generator>> generatorClasses = reflections.getSubTypesOf(Generator.class);

    for (Class<? extends Generator> generatorClass : generatorClasses) {
      GenerateOptions generateOptions = generatorClass.getAnnotation(GenerateOptions.class);
      if (generateOptions != null) {
        Class<? extends Options> optionsClass = generateOptions.value();
        generatorFactoryMap.put(optionsClass, column -> createGeneratorInstance(generatorClass, optionsClass, column));
      }
    }
  }

  private static Generator<?, ?> createGeneratorInstance(Class<? extends Generator> generatorClass,
                                                         Class<? extends Options> optionsClass,
                                                         DataSpecColumn column) {
    try {
      return generatorClass.getConstructor(optionsClass).newInstance(optionsClass.cast(column.getType().getOptions()));
    } catch (Exception e) {
      throw new RuntimeException("Failed to create generator instance", e);
    }
  }

  public static Generator<?, ?> create(GenerateColumn column) {

    if (!generatorMap.containsKey(column)) {

      DataSpecColumn dataSpecColumn = column.getDataSpecColumn();

      // データ生成仕様からデータ生成
      if (!column.getDataSpecColumn().isEmpty()) {
        Class<? extends Options> optionsClassInput = dataSpecColumn.getType().getOptions().getClass();
        generatorMap.put(column, generatorFactoryMap.get(optionsClassInput).apply(column.getDataSpecColumn()));
        return generatorMap.get(column);
      }
      // テーブル仕様からデータ生成
      else {
        String type = column.getTableColumn().getType();
        switch (type) {
          // 文字列型
          case "string":
          case "varchar":
          case "char":
          case "text":
            // 文字列ジェネレータを返す
            StringOptions stringOptions = new StringOptions(column.getTableColumn().getDigit().getDigit());
            generatorMap.put(column, new StringGenerator(stringOptions));
            break;

          // 数値型
          case "int":
          case "integer":
            // int/integer: 通常、-2,147,483,648 から 2,147,483,
            IntOptions intOptions1 = new IntOptions(-2147483648L, 2147483647L);
            generatorMap.put(column, new IntGenerator(intOptions1));
            break;

          case "bigint":
            // bigint: 通常、-9,223,372,036,854,775,808 から 9,223,372,036,854,775,807
            IntOptions intOptions2 = new IntOptions(-9223372036854775808L, 9223372036854775807L);
            generatorMap.put(column, new IntGenerator(intOptions2));
            break;

          case "smallint":
            // smallint: 通常、-32,768 から 32,767
            IntOptions intOptions3 = new IntOptions(-32768L, 32767L);
            generatorMap.put(column, new IntGenerator(intOptions3));
            break;

          case "decimal":
          case "numeric":
            // decimal/numeric: 桁数と小数点以下の桁数を指定する
            // 例: 最大10桁、小数点以下2桁の場合
            DecimalOptions decimalOptions = new DecimalOptions(10, 2);
            generatorMap.put(column, new DecimalGenerator(decimalOptions));
            break;

          // 日付型
          case "date":
            // 日付ジェネレータを返す
            DateOptions dateOptions = new DateOptions();
            generatorMap.put(column, new DateGenerator(dateOptions));
            break;

          case "timestamp":
          case "datetime": // MySQL特有
            // タイムスタンプジェネレータを返す
            DateTimeOptions dateTimeOptions = new DateTimeOptions();
            generatorMap.put(column, new DateTimeGenerator(dateTimeOptions));
            break;

          case "boolean":
          case "bit": // OracleとMySQLで使用
            // ブーリアンジェネレータを返す
            throw new IllegalArgumentException("未知の数値型: " + type);

            // その他の特有の型はここで追加
            // 例: PostgreSQLのJSON型、OracleのRAW型、MySQLのENUM型など

          default:
            throw new IllegalArgumentException("未知の数値型: " + type);
        }
      }
    }

    return generatorMap.get(column);
  }

}

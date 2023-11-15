package databullet.domain.generate.generator;

import databullet.domain.definition.data.DataSpecColumn;
import databullet.domain.definition.data.options.*;
import databullet.domain.generate.table.GenerateColumn;
import databullet.infrastructure.JsonMapper;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import java.util.function.Function;

public class DataGeneratorFactory {

  private static HashMap<Class<? extends Options>, Function<DataSpecColumn, Generator<?>>> generatorFactoryMap;

  private static HashMap<GenerateColumn, Generator<?>> generatorMap = new HashMap<>();

  public static Generator<?> create(GenerateColumn column) {

    if (!generatorMap.containsKey(column)) {

      DataSpecColumn dataSpecColumn = column.getDataSpecColumn();
      if (column.getDataSpecColumn() != null) {
        Options options = dataSpecColumn.getType().getOptions();
        try {

          Generator<?> generator = getGeneratorFactoryMap().get(options.getClass()).apply(dataSpecColumn);
          generatorMap.put(column, generator);
        } catch (Exception e) {
          e.printStackTrace();
        }
        return generatorMap.get(column);
      }

      String type = column.getTableColumn().getType().toLowerCase(Locale.ROOT);
      switch (type) {
        // 文字列型
        case "varchar":
        case "char":
        case "text":
          // 文字列ジェネレータを返す
          StringOptions stringOptions = new StringOptions();
          stringOptions.setLength(column.getTableColumn().getDigit().getDigit());
          return new StringGenerator(stringOptions);

        // 数値型
        case "int":
        case "integer":
          // int/integer: 通常、-2,147,483,648 から 2,147,483,
          generatorMap.put(column, new NumericGenerator(new NumericOptions(-2147483648L, 2147483647L)));
          break;

        case "bigint":
          // bigint: 通常、-9,223,372,036,854,775,808 から 9,223,372,036,854,775,807
          generatorMap.put(column, new NumericGenerator(new NumericOptions(-9223372036854775808L, 9223372036854775807L)));
          break;

        case "smallint":
          // smallint: 通常、-32,768 から 32,767
          generatorMap.put(column, new NumericGenerator(new NumericOptions(-32768L, 32767L)));
          break;

        case "decimal":
        case "numeric":
          // decimal/numeric: 桁数と小数点以下の桁数を指定する
          // 例: 最大10桁、小数点以下2桁の場合
          generatorMap.put(column, new DecimalGenerator(new DecimalOptions(10, 2)));
          break;

        // 日付型
        case "date":
          // 日付ジェネレータを返す
          generatorMap.put(column, new DateGenerator(new DateOptions()));
          break;

        case "timestamp":
        case "datetime": // MySQL特有
          // タイムスタンプジェネレータを返す
          throw new IllegalArgumentException("未知の数値型: " + type);

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

    return generatorMap.get(column);
  }


  private static HashMap<Class<? extends Options>, Function<DataSpecColumn, Generator<?>>> getGeneratorFactoryMap() {

    if (generatorFactoryMap == null) {

      generatorFactoryMap = new HashMap<>();

      Reflections reflections = new Reflections(Generator.class.getPackage().getName());
      Set<Class<? extends Generator>> classes = reflections.getSubTypesOf(Generator.class);

      for (Class<? extends Generator> clazz : classes) {
        Class<? extends Options> optionsClazz = clazz.getAnnotation(GenerateOptions.class).value();
        generatorFactoryMap.put(optionsClazz, column -> {
          try {
            return clazz.getConstructor(optionsClazz).newInstance(column.getType().getOptions());
          } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
          }
        });
      }
    }

    return generatorFactoryMap;
  }
}

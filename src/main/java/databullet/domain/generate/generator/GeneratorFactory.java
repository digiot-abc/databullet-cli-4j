package databullet.domain.generate.generator;

import databullet.domain.definition.data.DataSpecColumn;
import databullet.domain.definition.data.options.*;
import databullet.domain.generate.table.GenerateColumn;

import java.util.HashMap;
import java.util.Locale;
import java.util.function.Function;

public class GeneratorFactory {

  private static HashMap<Class<? extends Options>, Function<DataSpecColumn, Generator<?>>> generatorFactoryMap;

  private static HashMap<GenerateColumn, Generator<?>> generatorMap = new HashMap<>();

  public static Generator<?> create(GenerateColumn column) {

    if (!generatorMap.containsKey(column)) {

      DataSpecColumn dataSpecColumn = column.getDataSpecColumn();
      
      // DB Type
      String type = dataSpecColumn != null ? dataSpecColumn.getType().getName() : column.getTableColumn().getType().toLowerCase(Locale.ROOT);
      switch (type) {
        // 個別定義
        case "sequence":
          generatorMap.put(column, new SequenceGenerator((SequenceOptions) dataSpecColumn.getType().getOptions()));
          break;
        case "email":
          generatorMap.put(column, new EmailGenerator((EmailOptions) dataSpecColumn.getType().getOptions()));
          break;
        case "reference":
          generatorMap.put(column, new ReferenceGenerator((ReferenceOptions) dataSpecColumn.getType().getOptions()));
          break;
        // 文字列型
        case "string":
        case "varchar":
        case "char":
        case "text":
          // 文字列ジェネレータを返す
          StringOptions stringOptions = getOptions(column, StringOptions.class, new StringOptions(column.getTableColumn().getDigit().getDigit()));
          generatorMap.put(column, new StringGenerator(stringOptions));
          break;

        // 数値型
        case "int":
        case "integer":
          // int/integer: 通常、-2,147,483,648 から 2,147,483,
          generatorMap.put(column, new NumericGenerator(getOptions(column, IntOptions.class, new IntOptions(-2147483648L, 2147483647L))));
          break;

        case "bigint":
          // bigint: 通常、-9,223,372,036,854,775,808 から 9,223,372,036,854,775,807
          generatorMap.put(column, new NumericGenerator(getOptions(column, IntOptions.class, new IntOptions(-9223372036854775808L, 9223372036854775807L))));
          break;

        case "smallint":
          // smallint: 通常、-32,768 から 32,767
          generatorMap.put(column, new NumericGenerator(getOptions(column, IntOptions.class, new IntOptions(-32768L, 32767L))));
          break;

        case "decimal":
        case "numeric":
          // decimal/numeric: 桁数と小数点以下の桁数を指定する
          // 例: 最大10桁、小数点以下2桁の場合
          generatorMap.put(column, new DecimalGenerator(getOptions(column, DecimalOptions.class, new DecimalOptions(10, 2))));
          break;

        // 日付型
        case "date":
          // 日付ジェネレータを返す
          generatorMap.put(column, new DateGenerator(getOptions(column, DateOptions.class, new DateOptions())));
          break;

        case "timestamp":
        case "datetime": // MySQL特有
          // タイムスタンプジェネレータを返す
          generatorMap.put(column, new DateTimeGenerator(getOptions(column, DateTimeOptions.class, new DateTimeOptions())));
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

    return generatorMap.get(column);
  }

  private static <T extends Options> T getOptions(GenerateColumn column, Class<T> clazz, T defaultValue) {
    if (column.getDataSpecColumn() != null) {
      return clazz.cast(column.getDataSpecColumn().getType().getOptions());
    }
    return defaultValue;
  }
}

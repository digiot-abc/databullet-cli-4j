package databullet.domain.definition.data;

import databullet.domain.definition.table.TableDefinition;
import lombok.Data;

import java.util.List;

@Data
public class DataSpecification {

    private Double scale;
    private List<TableSpecification> tables;

    @Data
    static class TableSpecification {
        private String name;
        private int rowCount;
        private List<ColumnSpecification> columns;
    }

    @Data
    static class ColumnSpecification {

        private String name;
        private DataType type;
        private Boolean primaryKey;
        private Boolean nullable;

//        public void setType(Object type) {
//            this.type = new DataType(type);
//        }
    }

    @Data
    static class ColumnDigit {

        private Integer digit;
        private Integer afterDecimalPointDigit;

        ColumnDigit(Object size) {
            if (size instanceof Integer) {
                this.digit = (Integer) size;
            } else if (size instanceof List) {
                List<Integer> list = (List<Integer>) size;
                if (list.size() != 2) {
                    throw new NumberFormatException();
                }
                this.digit = list.get(0);
                this.afterDecimalPointDigit = list.get(1);
            }
        }
    }
}

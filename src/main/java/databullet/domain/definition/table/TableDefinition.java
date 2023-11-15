package databullet.domain.definition.table;

import lombok.Data;

import java.util.List;

@Data
public class TableDefinition {

    private List<TableSpecification> tables;

    @Data
    static class TableSpecification {
        private String name;
        private List<ColumnSpecification> columns;
    }

    @Data
    static class ColumnSpecification {

        private String name;
        private String type;
        private ColumnDigit digit;
        private Boolean primaryKey;
        private Boolean nullable;

        public void setDigit(Object size) {
            digit = new ColumnDigit(size);
        }
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

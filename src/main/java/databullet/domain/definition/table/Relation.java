package databullet.domain.definition.table;

import lombok.Data;

@Data
public class Relation {

    private String fromTable;
    private String fromColumn;
    private String toTable;
    private String toColumn;
}
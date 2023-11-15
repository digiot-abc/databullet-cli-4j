package databullet.domain.definition.table;

import lombok.Data;

import java.util.List;

@Data
public class RelationInfo {

    private List<Relation> relations;

    @Data
    static class Relation {

        private String fromTable;
        private String fromColumn;
        private String toTable;
        private String toColumn;
    }
}

package databullet.domain.definition.data.options;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

@JsonTypeName("reference")
@Data
public class ReferenceOptions implements Options {

    private String referencedTable;
    private String referencedColumn;

    public String getFQDNReferencedColumnName() {
        return String.join(".", referencedTable, referencedColumn);
    }
}

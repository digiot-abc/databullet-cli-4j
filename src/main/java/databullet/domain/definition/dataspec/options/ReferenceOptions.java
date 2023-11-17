package databullet.domain.definition.dataspec.options;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;

import java.util.List;

@JsonTypeName("reference")
@Data
public class ReferenceOptions implements Options {

    private String referencedTable;
    private String referencedColumn;

    @JsonIgnore
    public String getFQDNReferencedColumnName() {
        return String.join(".", referencedTable, referencedColumn);
    }
}

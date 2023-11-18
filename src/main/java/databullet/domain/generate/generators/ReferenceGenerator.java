package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.types.ReferenceType;
import databullet.domain.definition.generate.GenerateColumn;
import databullet.domain.generate.GenerateOptions;
import databullet.domain.generate.GenerateStore;

@GenerateOptions(ReferenceType.class)
public class ReferenceGenerator extends Generator<String, ReferenceType> {

    public ReferenceGenerator(ReferenceType referenceType) {
        super(referenceType);
    }

    @Override
    public String generate(GenerateStore store) {
        GenerateColumn column = store.getProcessingColumns().get();
        int index = store.getProcessingRowIndex().get();
        Object value = store.getColumnValue(column, index);
        return value.toString();
    }
}

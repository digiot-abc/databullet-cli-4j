package databullet.domain.definition.data;

import databullet.infrastructure.JsonMapper;
import lombok.Data;

@Data
public class DataType {

    private String name;
    private Options options;

    public void setOptions(Object options) {
        System.out.println(options);

        this.options = new SequenceOptions();
    }

    interface Options {

        boolean is(String name);
    }

    @Data
    static class SequenceOptions implements Options {

        private Integer start;
        private Integer increment;
    }
}


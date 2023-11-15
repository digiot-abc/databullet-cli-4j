package databullet.domain.generate.table;

import lombok.Data;

import java.util.List;

@Data
public class GenerateTable {

  private List<GenerateTable> children;
}

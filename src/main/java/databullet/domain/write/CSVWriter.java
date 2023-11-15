package databullet.domain.write;

import java.nio.file.Path;
import java.util.List;

public class CSVWriter implements DataWriter<Path> {

  @Override
  public Path write(Path output, List<DataRecord> record) {
//    System.out.println(record);
    return null;
  }
}

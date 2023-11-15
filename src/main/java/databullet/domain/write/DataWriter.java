package databullet.domain.write;

import java.nio.file.Path;
import java.util.List;

public interface DataWriter<T> {

  T write(Path output, List<DataRecord> record);
}

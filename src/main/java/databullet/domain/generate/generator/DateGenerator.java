package databullet.domain.generate.generator;

import databullet.domain.definition.data.options.DateOptions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@GenerateOptions(DateOptions.class)
public class DateGenerator implements Generator<String> {

    private long start = LocalDate.of(2000, 1, 1).toEpochDay();
    private long end = LocalDate.of(2023, 1, 1).toEpochDay();

    private DateTimeFormatter formatter;

    public DateGenerator(DateOptions dateOptions) {
        this.formatter = dateOptions.getFormatter();
    }

    @Override
    public String generate() {
        long randomDay = ThreadLocalRandom.current().nextLong(start, end);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return randomDate.format(formatter);
    }
}

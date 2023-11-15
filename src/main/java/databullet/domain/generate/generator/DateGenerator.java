package databullet.domain.generate.generator;

import databullet.domain.definition.data.options.DateOptions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@GenerateOptions(DateOptions.class)
public class DateGenerator implements Generator<String> {

    private long start;
    private long end;

    private DateTimeFormatter formatter;

    public DateGenerator(DateOptions dateOptions) {
        this.formatter = dateOptions.getFormatter();
        this.start = dateOptions.getStart().toEpochDay();
        this.end = dateOptions.getEnd().toEpochDay();
    }

    @Override
    public String generate() {
        long randomDay = ThreadLocalRandom.current().nextLong(start, end);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return randomDate.format(formatter);
    }
}

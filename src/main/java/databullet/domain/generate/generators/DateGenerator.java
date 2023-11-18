package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.types.DateType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@GenerateOptions(DateType.class)
public class DateGenerator extends Generator<String, DateType> {

    private long start;
    private long end;

    private DateTimeFormatter formatter;

    public DateGenerator(DateType dateType) {
        super(dateType);
        this.formatter = dateType.getFormatter();
        this.start = dateType.getStart().toEpochDay();
        this.end = dateType.getEnd().toEpochDay();
    }

    @Override
    public String generate() {
        long randomDay = ThreadLocalRandom.current().nextLong(start, end);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return randomDate.format(formatter);
    }
}

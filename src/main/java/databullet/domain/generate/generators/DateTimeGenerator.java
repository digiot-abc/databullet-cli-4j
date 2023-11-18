package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.types.DateTimeType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@GenerateOptions(DateTimeType.class)
public class DateTimeGenerator extends Generator<String, DateTimeType> {

    private final long start;
    private final long end;
    private final DateTimeFormatter formatter;
    private final ThreadLocalRandom random = ThreadLocalRandom.current();

    public DateTimeGenerator(DateTimeType dateOptions) {
        super(dateOptions);
        this.formatter = dateOptions.getFormatter();
        this.start = dateOptions.getStart().toLocalDate().toEpochDay();
        this.end = dateOptions.getEnd().toLocalDate().toEpochDay();
    }

    @Override
    public String generate() {
        long randomDay = random.nextLong(start, end);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        int secondOfDay = random.nextInt(86400); // 一日の秒数
        LocalTime randomTime = LocalTime.ofSecondOfDay(secondOfDay);
        LocalDateTime randomDateTime = LocalDateTime.of(randomDate, randomTime);
        return formatter.format(randomDateTime);
    }
}

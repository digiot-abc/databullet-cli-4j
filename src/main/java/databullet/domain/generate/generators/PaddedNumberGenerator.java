package databullet.domain.generate.generators;

import databullet.domain.definition.dataspec.types.PaddedNumberType;

import java.util.Random;

@GenerateOptions(PaddedNumberType.class)
public class PaddedNumberGenerator extends Generator<String, PaddedNumberType> {

    private final int totalLength;
    private final long maxValue;
    private final Random random = new Random();

    public PaddedNumberGenerator(PaddedNumberType options) {
        super(options);
        this.totalLength = options.getTotalLength();
        this.maxValue = (long) Math.pow(10, options.getDigits()) - 1;
    }

    @Override
    public String generate() {
        long number = Math.abs(random.nextLong()) % (maxValue + 1);
        return String.format("%0" + totalLength + "d", number);
    }
}

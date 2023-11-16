package databullet.domain.generate.generator;

import databullet.domain.definition.data.options.PaddedNumberOptions;

import java.util.Random;

@GenerateOptions(PaddedNumberOptions.class)
public class PaddedNumberGenerator extends Generator<String, PaddedNumberOptions> {

    private final int totalLength;
    private final long maxValue;
    private final Random random = new Random();

    public PaddedNumberGenerator(PaddedNumberOptions options) {
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

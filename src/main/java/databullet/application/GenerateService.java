package databullet.application;

import databullet.domain.definition.generate.GenerateDefinition;
import databullet.domain.generate.GenerateProcessor;
import databullet.domain.generate.GenerateStore;
import databullet.domain.generate.persistance.CSVPersistence;
import databullet.domain.generate.persistance.Persistence;
import databullet.domain.generate.persistance.PrintPersistence;

import java.nio.file.Path;

public class GenerateService {

    public void generateCsv(GenerateDefinition definitions, Path outputBase) {
        generate(definitions, new CSVPersistence(outputBase));
    }

    public void generatePrint(GenerateDefinition definitions) {
        generate(definitions, new PrintPersistence());
    }

    void generate(GenerateDefinition definitions, Persistence persistence) {

        System.out.println(definitions.getTableDef());
        System.out.println(definitions.getDataSpec());

        // 計測
        long st = System.currentTimeMillis();

        // 生成を行う
        try {
            GenerateStore store = new GenerateStore(500);
            GenerateProcessor processor = new GenerateProcessor(store);
            processor.generate(definitions, persistence);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Execute time: " + (System.currentTimeMillis() - st) + "ms");
    }
}

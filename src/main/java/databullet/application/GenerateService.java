package databullet.application;

import databullet.domain.definition.generate.GenerateDefinitions;
import databullet.domain.generate.GenerateProcessor;
import databullet.domain.generate.GenerateStore;
import databullet.domain.generate.persistance.Persistence;
import databullet.domain.generate.persistance.PrintPersistence;

import java.util.concurrent.ExecutionException;

public class GenerateService {

    public void generatePrint(GenerateDefinitions definitions) {
        generate(definitions, new PrintPersistence());
    }

    void generate(GenerateDefinitions definitions, Persistence persistence) {

        System.out.println(definitions.getTableDef());
        System.out.println(definitions.getDataSpec());
        System.out.println(definitions.getRelationInfo());

        // 計測
        long st = System.currentTimeMillis();

        // 生成を行う
        GenerateStore store = new GenerateStore();
        GenerateProcessor processor = new GenerateProcessor(store);
        try {
            processor.generate(definitions, persistence);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Execute time: " + (System.currentTimeMillis() - st) + "ms");
    }
}

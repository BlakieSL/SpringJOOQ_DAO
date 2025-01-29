package org.example.springjooq.config;

import org.jooq.codegen.DefaultGeneratorStrategy;
import org.jooq.meta.Definition;

public class CustomJooqGeneratorStrategy extends DefaultGeneratorStrategy {

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        String name = super.getJavaClassName(definition, mode);

        if (mode == Mode.POJO) {
            return name + "Pojo";
        }

        return name;
    }
}
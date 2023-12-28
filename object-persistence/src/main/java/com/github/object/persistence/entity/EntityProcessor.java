package com.github.object.persistence.entity;

import com.google.auto.service.AutoService;
import org.atteo.classindex.ClassIndex;
import org.atteo.classindex.processor.ClassIndexProcessor;

import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import javax.persistence.Entity;
import java.util.Set;

@AutoService(Processor.class)
public class EntityProcessor extends ClassIndexProcessor {

    public EntityProcessor() {
        indexAnnotations(Entity.class);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        boolean result = super.process(annotations, roundEnv);
        if (result) {
            ClassIndex.getAnnotated(Entity.class).forEach(item -> EntityValidator.getInstance().validateEntity(item));
        }

        return result;
    }
}

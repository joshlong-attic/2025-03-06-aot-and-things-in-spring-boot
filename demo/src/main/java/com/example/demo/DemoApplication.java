package com.example.demo;

import org.springframework.aot.generate.GenerationContext;
import org.springframework.aot.hint.TypeReference;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotContribution;
import org.springframework.beans.factory.aot.BeanFactoryInitializationAotProcessor;
import org.springframework.beans.factory.aot.BeanFactoryInitializationCode;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashSet;


@SpringBootApplication
public class DemoApplication {



    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    static BFIAP bfiap() {
        return new BFIAP();
    }

    static class BFIAP implements BeanFactoryInitializationAotProcessor {

        @Override
        public BeanFactoryInitializationAotContribution processAheadOfTime(
                ConfigurableListableBeanFactory beanFactory) {

            var serializable = new HashSet<Class<?>>();

            var beanDefinitionNames = beanFactory.getBeanDefinitionNames();
            for (var beanName : beanDefinitionNames) {
                var beanDefinition = beanFactory.getBeanDefinition(beanName);
                System.out.println("processing bean definition [" + beanDefinition.getBeanClassName() + "] " +
                        "named [" + beanName + "]");
                var type = (Class<?>) beanFactory.getType(beanName);
                assert type != null : "the type is not null";
                if (Serializable.class.isAssignableFrom(type)) { // ln -s dest source
                    serializable.add(type);
                }
            }

            return (generationContext, beanFactoryInitializationCode) -> {

                // write out json
                var runtimeHints = generationContext.getRuntimeHints();
                for (var s : serializable) {
                    System.out.println("registering hint for [" + s + "]");
                    runtimeHints.serialization().registerType(TypeReference.of(s.getName()));
                }

                // write out java

                beanFactoryInitializationCode.getMethods()
                        .add("devnexus", builder-> {
                            builder.addCode("""
                                    System.out.println("howdy Devnexus");
                                    """);
                        }) ;

            };
        }
    }

}


@Component
class ShoppingCart implements Serializable {
}

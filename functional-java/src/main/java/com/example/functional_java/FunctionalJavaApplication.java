package com.example.functional_java;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.GenericApplicationContext;

@SpringBootApplication
public class FunctionalJavaApplication {

    public static void main(String[] args) {
        //SpringApplication.run(FunctionalJavaApplication.class, args);
        var ac = new SpringApplicationBuilder(FunctionalJavaApplication.class)
                .initializers((ApplicationContextInitializer<GenericApplicationContext>)
                        applicationContext -> applicationContext.registerBean(CustomerService.class, CustomerService::new))
                .main(FunctionalJavaApplication.class)
                .run(args);

    }

    @Bean
    static MyBFPP myBFPP() {
        return new MyBFPP();
    }

    static class MyBFPP implements BeanFactoryPostProcessor {

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            for (var beanName: beanFactory.getBeanDefinitionNames()) {
                var bd = beanFactory.getBeanDefinition(beanName );
                var clzzName = bd.getBeanClassName() ;
                var type = beanFactory.getType(beanName);
                System.out.println("className: " + clzzName + ", type: " + type);
            }
        }
    }
}

class CustomerService {
    CustomerService() {
        System.out.println("CustomerService created");
    }
}
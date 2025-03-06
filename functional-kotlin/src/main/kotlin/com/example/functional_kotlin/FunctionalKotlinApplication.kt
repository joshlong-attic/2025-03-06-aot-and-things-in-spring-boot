package com.example.functional_kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

@SpringBootApplication
class FunctionalKotlinApplication

fun main(args: Array<String>) {
    runApplication<FunctionalKotlinApplication>(*args) {
        addInitializers(beans {
            bean {
                CustomerService()
            }
            bean {
                val cs: CustomerService = ref<CustomerService>()
                router {
                    GET("/hi") {
                        ServerResponse.ok().body(cs)
                    }
                }
            }
        })
    }
}

class CustomerService
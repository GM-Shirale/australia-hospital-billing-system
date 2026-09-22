package com.hospital.hospital_billing_system.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    public OpenAPI hospitalBillingOpenAPI(){

        return (new OpenAPI()
                .info(new Info()
                        .title("Australian Hospital Billing System API")
                        .version("1.0.0")
                        .description(
                                "REST API documentation for the Australian Hospital Billing System. "
                                        + "Includes Laboratory, Pharmacy, Doctor, Patient, Billing and related modules."
                        )
                        .contact(new Contact()
                                .name("Hospital Billing System Team"))));
    }

}

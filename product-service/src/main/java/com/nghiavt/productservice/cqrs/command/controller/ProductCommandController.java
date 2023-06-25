package com.nghiavt.productservice.cqrs.command.controller;

import com.nghiavt.productservice.core.model.CreateProductModel;
import com.nghiavt.productservice.cqrs.command.CreateProductCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductCommandController {

    private final Environment env;
    private final CommandGateway commandGateway;

    @Autowired
    public ProductCommandController(Environment env, CommandGateway commandGateway) {
        this.env = env;
        this.commandGateway = commandGateway;
    }
    @PostMapping
    public String createProduct(@RequestBody CreateProductModel model) {
        CreateProductCommand createProductCommand = CreateProductCommand.builder()
                .price(model.getPrice())
                .quantity(model.getQuantity())
                .title(model.getTitle())
                .productId(UUID.randomUUID().toString())
                .build();
        String res;
        try {
            res = commandGateway.sendAndWait(createProductCommand);
        } catch (Exception e) {
            res = e.getLocalizedMessage();
        }
        return res;
    }
}

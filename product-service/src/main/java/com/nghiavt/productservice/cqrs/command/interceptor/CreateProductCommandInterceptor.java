package com.nghiavt.productservice.cqrs.command.interceptor;

import com.nghiavt.productservice.core.database.repository.ProductLookupRepository;
import com.nghiavt.productservice.core.model.ProductLookupEntity;
import com.nghiavt.productservice.cqrs.command.CreateProductCommand;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
public class CreateProductCommandInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final ProductLookupRepository productLookupRepository;
    private static final Logger LOG = LoggerFactory.getLogger(CreateProductCommandInterceptor.class);

    public CreateProductCommandInterceptor(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(
            @Nonnull List<? extends CommandMessage<?>> messages) {
        return (index, command) ->{
            LOG.info("Interceptor command: " + command.getPayloadType());

            if (CreateProductCommand.class.equals(command.getPayloadType())){
                CreateProductCommand cpc = (CreateProductCommand) command.getPayload();
                ProductLookupEntity e =
                        productLookupRepository.findByProductIdOrTitle(cpc.getProductId(), cpc.getTitle());
                if (e!=null){
                    throw new IllegalStateException("Product already exists.");
                }
            }
            return command;
        };
    }
}

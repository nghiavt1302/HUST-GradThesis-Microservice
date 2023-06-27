package com.nghiavt.userservice.cqrs.query;

import com.nghiavt.common.model.PaymentDetail;
import com.nghiavt.common.model.User;
import com.nghiavt.common.query.FetchUserPaymentDetailQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
public class UserEventHandler {
    @QueryHandler
    public User findUserPaymentDetails(FetchUserPaymentDetailQuery query) {

        PaymentDetail paymentDetails = PaymentDetail.builder()
                .cardNumber("123Card")
                .cvv("123")
                .name("SERGEY KARGOPOLOV")
                .validUntilMonth(12)
                .validUntilYear(2030)
                .build();

        User user = User.builder()
                .firstName("Sergey")
                .lastName("Kargopolov")
                .userId(query.getUserId())
                .paymentDetail(paymentDetails)
                .build();

        return user;
    }
}

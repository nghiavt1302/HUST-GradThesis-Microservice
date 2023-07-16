package com.nghiavt.userservice.cqrs.query;

import com.nghiavt.common.model.PaymentDetail;
import com.nghiavt.common.model.User;
import com.nghiavt.common.query.FetchUserPaymentDetailQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserEventHandler {
    @QueryHandler
    public User findUserPaymentDetails(FetchUserPaymentDetailQuery query) {
        // Hard code, not using DB
        PaymentDetail paymentDetails = PaymentDetail.builder().cardNumber("123456789").cvv("678")
                .name("VU TRONG NGHIA").validUntilMonth(12).validUntilYear(2030).build();

        User user = User.builder().firstName("Nghia").lastName("Vu Trong").userId(query.getUserId())
                .paymentDetail(paymentDetails).build();
        return user;
    }
}

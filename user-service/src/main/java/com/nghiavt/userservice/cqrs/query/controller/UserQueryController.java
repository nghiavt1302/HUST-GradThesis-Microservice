package com.nghiavt.userservice.cqrs.query.controller;

import com.nghiavt.common.model.User;
import com.nghiavt.common.query.FetchUserPaymentDetailQuery;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserQueryController {
    @Autowired
    QueryGateway queryGateway;

    @GetMapping("/{userId}/payment-details")
    public User getUserPaymentDetails(@PathVariable String userId) {

        FetchUserPaymentDetailQuery query = new FetchUserPaymentDetailQuery(userId);

        return queryGateway.query(query, ResponseTypes.instanceOf(User.class)).join();
    }
}


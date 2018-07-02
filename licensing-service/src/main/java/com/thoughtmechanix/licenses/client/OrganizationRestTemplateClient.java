package com.thoughtmechanix.licenses.client;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.thoughtmechanix.licenses.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Component
public class OrganizationRestTemplateClient {
    @Autowired
    RestTemplate restTemplate;

    private void sleep() {
        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void randomlyRunLong() {
        Random rand = new Random();
        int randomNum = rand.nextInt((3 - 1) + 1) + 1;

        if (randomNum == 3) {
            System.out.println("Going to sleep!");
            sleep();
        }
    }

    @HystrixCommand
    public Organization getOrganization(String organizationId) {
        randomlyRunLong();
        ResponseEntity<Organization> restExchange = restTemplate.exchange(
                "http://organizationservice/v1/organizations/{organizationId}",
                HttpMethod.GET,
                null, Organization.class, organizationId);

        return restExchange.getBody();
    }
}

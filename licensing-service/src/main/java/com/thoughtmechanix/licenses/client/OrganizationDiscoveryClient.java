package com.thoughtmechanix.licenses.client;

import com.thoughtmechanix.licenses.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class OrganizationDiscoveryClient {

    // DiscoveryClient is used to interact with Ribbon
    // Normally you should not be using the DiscoveryClient directly unless you need to
    // query Ribbon to understand what services and service instances are registered.
    @Autowired
    private DiscoveryClient discoveryClient;

    public Organization getOrganization(String organizationId) {

        // Normally you should not be instantiating the RestTemplate as this is normally @Autowired.
        // The reason we did this here is because using @EnableDiscoveryClient in the bootstrap (Application.class) then
        // makes the Spring Framework inject a Ribbon-enabled interceptor and change how URL's are created
        RestTemplate restTemplate = new RestTemplate();

        // Get a list of all instances of organization services
        List<ServiceInstance> instances = discoveryClient.getInstances("organizationservice");

        if (instances.size() == 0) return null;

        // This is bad practice as it's only using the first service instance.
        String serviceUri = String.format("%s/v1/organizations/%s", instances.get(0).getUri().toString(), organizationId);

        // Uses a standard Spring REST Template class to call the service
        ResponseEntity<Organization> restExchange = restTemplate.exchange(
                        serviceUri,
                        HttpMethod.GET,
                        null,
                        Organization.class,
                        organizationId);

        return restExchange.getBody();
    }
}

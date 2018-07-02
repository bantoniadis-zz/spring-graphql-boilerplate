package com.thoughtmechanix.licenses.controllers;

import com.thoughtmechanix.licenses.config.ServiceConfig;
import com.thoughtmechanix.licenses.model.License;
import com.thoughtmechanix.licenses.services.LicenseService;
import com.thoughtmechanix.licenses.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value="/v1/organizations/{organizationId}/licenses")
public class LicenseServiceController {
    private static final Logger logger = LoggerFactory.getLogger(LicenseServiceController.class);

    @Autowired
    private LicenseService licenseService;

    @Autowired
    private ServiceConfig serviceConfig;

    @GetMapping("/")
    public List<License> getLicenses(@PathVariable("organizationId") String organizationId) {
        logger.debug("LicenseServiceController.getLicenses id: {}", UserContextHolder.getContext().getCorrelationId());
        return licenseService.getLicenseByOrg(organizationId);
    }

    @GetMapping("/{licenseId}")
    public License getLicenses(@PathVariable("organizationId") String organizationId,
                               @PathVariable("licenseId") String licenseId) {
        logger.debug("LicenseServiceController.getLicense Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

        return licenseService.getLicense(organizationId, licenseId, "");
    }

    /*
     * This helper route allows you to specify the type of client you want to invoke the service with.
     * The clientType can be Discovery|Rest|Feign.
     */
    @GetMapping(value="/{licenseId}/{clientType}")
    public License getLicensesWithClient( @PathVariable("organizationId") String organizationId,
                                          @PathVariable("licenseId") String licenseId,
                                          @PathVariable("clientType") String clientType) {

        return licenseService.getLicense(organizationId,licenseId, clientType);
    }

    @PutMapping(value="{licenseId}")
    public void updateLicenses( @PathVariable("licenseId") String licenseId, @RequestBody License license) {
        licenseService.updateLicense(license);
    }

    @PostMapping(value="/")
    public void saveLicenses(@RequestBody License license) {
        licenseService.saveLicense(license);
    }

    @DeleteMapping(value="{licenseId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLicenses( @PathVariable("licenseId") String licenseId, @RequestBody License license) {
        licenseService.deleteLicense(license);
    }
}
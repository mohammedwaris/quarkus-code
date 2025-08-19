package org.acme.controller;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.acme.dto.FullCountryInfoRequest;
import org.acme.dto.FullCountryInfoResponse;
import org.acme.service.CountryService;

@Path("/")
public class CountryController {

    @Inject
    CountryService countryService;

    @Path("/fullcountryinfo")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public FullCountryInfoResponse fullCountryInfo(FullCountryInfoRequest fullCountryInfoRequest) {
        return countryService.processRequest(fullCountryInfoRequest);
    }

}

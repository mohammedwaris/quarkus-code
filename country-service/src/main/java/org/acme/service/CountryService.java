package org.acme.service;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import org.acme.dto.FullCountryInfoRequest;
import org.acme.dto.FullCountryInfoResponse;
import org.acme.dto.FullCountryInfoResult;
import org.acme.dto.Language;
import org.acme.dto.xml.EnvelopeXml;
import org.acme.util.JsonFormatter;
import java.io.StringReader;
import java.util.ArrayList;

@ApplicationScoped
public class CountryService {

    @Inject
    CountrySoapService countrySoapService;

    @Inject
    JsonFormatter jsonFormatter;

    public FullCountryInfoResponse processRequest(FullCountryInfoRequest fullCountryInfoRequest) {

        String response = countrySoapService.callFullCountryInfoSoapOperation(fullCountryInfoRequest.getCountryISOCode());
        return prepareResponse(response);

    }

    private FullCountryInfoResponse prepareResponse(String response) {
        FullCountryInfoResponse fullCountryInfoResponse = new FullCountryInfoResponse();
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(EnvelopeXml.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            EnvelopeXml envelopeXml = (EnvelopeXml) unmarshaller.unmarshal(new StringReader(response));
            Log.info(jsonFormatter.format(envelopeXml));
            FullCountryInfoResult fullCountryInfoResult = new FullCountryInfoResult();
            fullCountryInfoResult.isoCode = envelopeXml.bodyXml.fullCountryInfoResponseXml.fullCountryInfoResultXml.isoCode;
            fullCountryInfoResult.name = envelopeXml.bodyXml.fullCountryInfoResponseXml.fullCountryInfoResultXml.name;
            fullCountryInfoResult.capitalCity = envelopeXml.bodyXml.fullCountryInfoResponseXml.fullCountryInfoResultXml.capitalCity;
            fullCountryInfoResult.phoneCode = envelopeXml.bodyXml.fullCountryInfoResponseXml.fullCountryInfoResultXml.phoneCode;
            fullCountryInfoResult.continentCode = envelopeXml.bodyXml.fullCountryInfoResponseXml.fullCountryInfoResultXml.continentCode;
            fullCountryInfoResult.currencyISOCode = envelopeXml.bodyXml.fullCountryInfoResponseXml.fullCountryInfoResultXml.currencyISOCode;
            fullCountryInfoResult.countryFlag = envelopeXml.bodyXml.fullCountryInfoResponseXml.fullCountryInfoResultXml.countryFlag;
            fullCountryInfoResult.languages = new ArrayList<>();
            envelopeXml.bodyXml.fullCountryInfoResponseXml.fullCountryInfoResultXml.languagesXml.languageList.forEach(tLanguageXml -> {
                Language language = new Language();
                language.isoCode = tLanguageXml.isoCode;
                language.name = tLanguageXml.name;
                fullCountryInfoResult.languages.add(language);
            });
            fullCountryInfoResponse.setFullCountryInfoResult(fullCountryInfoResult);
        }catch(Exception e) {
            Log.error(e);
        }
        return fullCountryInfoResponse;
    }

}

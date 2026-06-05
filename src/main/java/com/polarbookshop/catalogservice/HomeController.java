package com.polarbookshop.catalogservice;

import com.polarbookshop.catalogservice.config.PolarProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final PolarProperties polarProperties;

    @Autowired
    public HomeController( PolarProperties polarProperties ) {
        this.polarProperties = polarProperties;
    }

    @GetMapping( "/" )
    public String getGreeting() {
        return polarProperties.getGreeting();
    }
}

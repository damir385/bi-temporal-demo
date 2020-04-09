package com.example.bitemporal.test

import org.springframework.jdbc.core.JdbcTemplate

trait PartnerDbTestUtilities {

    static def causes(Throwable e) {
        List<Throwable> exceptions = new ArrayList<>()
        exceptions.add(e)
        if (e.cause != null)
            exceptions.addAll(causes(e.cause))
        exceptions
    }

    def cleanUp(JdbcTemplate jdbcTemplate){

    }

    def cleanUpPartners(JdbcTemplate jdbcTemplate){

    }

    def cleanUpAddresses(JdbcTemplate jdbcTemplate){

    }

    def cleanUpPartnerAddresses(JdbcTemplate jdbcTemplate){

    }

}

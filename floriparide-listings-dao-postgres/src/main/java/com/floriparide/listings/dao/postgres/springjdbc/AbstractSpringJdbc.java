package com.floriparide.listings.dao.postgres.springjdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

abstract public class AbstractSpringJdbc {

    private NamedParameterJdbcTemplate namedJdbcTemplate;

    private JdbcTemplate jdbcTemplate;

    protected AbstractSpringJdbc(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    protected NamedParameterJdbcTemplate getNamedJdbcTemplate() {
        return namedJdbcTemplate;
    }

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
}
package com.floriparide.listings.dao.postgres;

import com.floriparide.listings.dao.IRawDataDao;
import com.floriparide.listings.dao.postgres.springjdbc.AbstractSpringJdbc;
import com.floriparide.listings.model.RawData;
import com.floriparide.listings.model.Schema;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Mikhail Bragin
 */
public class RawDataDao extends AbstractSpringJdbc implements IRawDataDao {

    private static final String table = Schema.TABLE_RAW_DATA_DATA;

    public RawDataDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate) {
        super(namedJdbcTemplate, jdbcTemplate);
    }

    @NotNull
    @Override
    public void create(final @NotNull List<RawData> rawData) throws Exception {

        String query = "INSERT INTO " + table + " " +
                "(" + Schema.TABLE_RAW_DATA_DATA_FIELD_SOURCE + ","
                + Schema.FIELD_DATA + ") VALUES (?::raw_data_source, ?::json)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().batchUpdate(query, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement stmt, int i) throws SQLException {
                RawData data = rawData.get(i);
                stmt.setString(1, data.getSource().getSource());
                stmt.setString(2, data.getData());
            }

            @Override
            public int getBatchSize() {
                return rawData.size();
            }
        });
    }

    @NotNull
    @Override
    public Long create(@NotNull RawData rawData) throws Exception {
        throw new UnsupportedOperationException("not implemented yet");
    }
}

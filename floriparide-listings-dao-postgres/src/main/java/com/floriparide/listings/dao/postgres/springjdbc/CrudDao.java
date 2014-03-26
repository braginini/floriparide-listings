package com.floriparide.listings.dao.postgres.springjdbc;

import com.floriparide.listings.dao.IBaseEntityDao;
import com.floriparide.listings.data.Filter;
import com.floriparide.listings.data.Query;
import com.google.common.base.Joiner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrei Tupitcyn
 */
public class CrudDao<E> extends AbstractSpringJdbc implements IBaseEntityDao<E> {

    protected String table;

    protected String idField = "id";

    protected Map<String, String> fieldsMap;

    protected RowMapper<E> rowMapper;

    protected boolean hasUpdatedField = false;

    protected static Logger log;

    private String updateSql;

    private String insertSql;

    protected CrudDao(NamedParameterJdbcTemplate namedJdbcTemplate, JdbcTemplate jdbcTemplate) {
        super(namedJdbcTemplate, jdbcTemplate);

        log = LoggerFactory.getLogger(getClass());

        Class<E> genericClass = (Class<E>) GenericTypeResolver.resolveTypeArgument(getClass(), CrudDao.class);

        if (table == null) {
            Table tableDescription = AnnotationUtils.findAnnotation(genericClass, Table.class);
            if (tableDescription != null) {
                table = tableDescription.name();
            } else {
                table = genericClass.getSimpleName().toLowerCase();
            }
        }

        fieldsMap = new HashMap<>();
        List<String> existsFields = jdbcTemplate.queryForList("SELECT column_name FROM information_schema.\"columns\" WHERE \"table_name\"= ?", String.class, table);

        String id = null;
        for (Field f : genericClass.getDeclaredFields()) {
            Column columnDescription = AnnotationUtils.findAnnotation(genericClass, Column.class);
            String name = f.getName();
            if (columnDescription != null) {
                fieldsMap.put(name, columnDescription.name());
            } else if (existsFields.indexOf(name) > -1) {
                fieldsMap.put(name, name);
            }

            if (id == null && AnnotationUtils.findAnnotation(genericClass, Id.class) != null) {
                id = name;
            }
        }

        if (id != null) {
            idField = id;
        }
    }

    protected String getUpdateSql() {
        if (updateSql == null) {
            List<String> set = new ArrayList<>();
            for (Map.Entry<String, String> e : fieldsMap.entrySet()) {
                String field = e.getKey();
                String column = e.getValue();

                set.add("\"" + column + "\" = :" + field);

            }

            if (hasUpdatedField) {
                set.add("\"updated\" = :updated");
            }
            StringBuffer sb = new StringBuffer();
            sb.append("UPDATE \"").append(table).append("\" \nSET ").append(Joiner.on(",\n").join(set))
                    .append(" \nWHERE \"").append(idField).append("\" = :old_").append(fieldsMap.get(idField));
            updateSql = sb.toString();
        }
        return updateSql;
    }

    protected String getInsertSql() {
        if (insertSql == null) {
            List<String> names = new ArrayList<>();
            List<String> values = new ArrayList<>();
            for (Map.Entry<String, String> e : fieldsMap.entrySet()) {
                String field = e.getKey();
                String column = e.getValue();
                if (column != idField) {
                    names.add('"' + column + '"');
                    values.add(':' + field);
                }
            }
            if (hasUpdatedField) {
                names.add("\"updated\"");
                names.add("\"created\"");
                values.add("\":updated");
                values.add("\":created");
            }
            StringBuffer sb = new StringBuffer();
            sb.append("INSERT INTO \"").append(table).append("\" (\n").append(Joiner.on(",\n").join(names))
                    .append("\n) \nVALUES (\n").append(Joiner.on(",\n").join(values)).append("\n)");
            insertSql = sb.toString();
        }
        return insertSql;
    }

    @Override
    public long create(@NotNull E entity) throws Exception {
        Assert.notNull(entity, "Parameter " + entity.getClass().getSimpleName() + "must not be null");
        KeyHolder keyHolder = new GeneratedKeyHolder();

        getNamedJdbcTemplate().update(getInsertSql(),
                getParamSource(entity).addValue(idField, null),
                keyHolder);

        return (Long) keyHolder.getKeys().get(idField);
    }

    @Override
    public void delete(long entityId) throws Exception {
        String query = "DELETE FROM \"" + table + "\" WHERE " + idField + " = ?";

        getJdbcTemplate().update(query, entityId);
    }

    @Override
    public void delete(Long[] entityIds) throws Exception {
        String query = "DELETE FROM \"" + table + "\" WHERE " + idField + " in (" + Joiner.on(",").join(entityIds) + ")";

        getJdbcTemplate().update(query);
    }

    @Override
    public void update(@NotNull E entity) throws Exception {
        Assert.notNull(entity, "Parameter " + entity.getClass().getSimpleName() + "must not be null");

        MapSqlParameterSource params = getParamSource(entity);
        params.addValue("old_" + fieldsMap.get(idField), params.getValue(idField));

        getNamedJdbcTemplate().update(getUpdateSql(), params);
    }

    @Override
    public void update(@NotNull E entity, long entityId) throws Exception {
        Assert.notNull(entity, "Parameter " + entity.getClass().getSimpleName() + "must not be null");

        MapSqlParameterSource params = getParamSource(entity);
        params.addValue("old_" + fieldsMap.get(idField), entityId);

        getNamedJdbcTemplate().update(getUpdateSql(), params);
    }

    @Nullable
    @Override
    public E get(long entityId) throws Exception {
        String query = "SELECT * FROM \"" + table + "\" WHERE \"" + idField + "\" = ?";
        List<E> rs = getJdbcTemplate().query(query, rowMapper, entityId);
        if (rs.size() == 0) {
            log.debug("Empty result exception has been swallowed");
            return null;
        }
        return rs.get(0);
    }

    @Override
    public List<E> getList() {
        String sql = "SELECT * FROM \"" + table + "\"";
        return getJdbcTemplate().query(sql, rowMapper);
    }

    @Override
    public List<E> getList(@NotNull Query query) {
        List params = new ArrayList();
        String sql = "SELECT * FROM \"" + table + "\"" + buildQuery(query, params);
        return getJdbcTemplate().query(sql, params.toArray(), rowMapper);
    }

    @Override
    public int size() throws Exception {
        String sql = "SELECT COUNT(1) FROM \"" + table + "\"";
        return getJdbcTemplate().queryForObject(sql, Integer.class);
    }

    @Override
    public int size(List<Filter> filters) throws Exception {
        Query q = new Query();
        q.setFilters(filters);
        List params = new ArrayList();
        String sql = "SELECT COUNT(1) FROM \"" + table + "\"" + buildQuery(q, params);
        return getJdbcTemplate().queryForObject(sql, params.toArray(), Integer.class);
    }

    protected String buildQuery(Query query, List params) {
        List<Filter> filters = query.getFilters();
        StringBuffer sb = new StringBuffer(" ");
        if (!filters.isEmpty()) {
            List<String> where = new ArrayList<>();
            for (Filter filter : filters) {
                where.add(filter.getFilter());
                params.addAll(filter.getBinds());
            }
            sb.append("\nWHERE (").append(Joiner.on(") AND (").join(where));
        }

        if (query.getSortField() != null) {
            sb.append("\nORDER BY ").append(query.getSortField().getKey()).append(" ")
                    .append(query.getSortType().getKey());
        }

        if (query.getLimit() != 0) {
            sb.append("\nLIMIT ").append(query.getLimit()).append(" OFFSET ").append(query.getOffset());
        }
        return sb.toString();
    }

    protected MapSqlParameterSource getParamSource(E entity) {
        BeanMap params = BeanMap.create(entity);
        if (hasUpdatedField) {
            params.put("created", System.currentTimeMillis());
            params.put("updated", System.currentTimeMillis());
        }
        return new MapSqlParameterSource(params);
    }
}

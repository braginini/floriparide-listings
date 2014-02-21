package com.floriparide.listings.dao;

import com.floriparide.listings.model.Project;
import com.floriparide.listings.model.sort.SortField;
import com.floriparide.listings.model.sort.SortType;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * @author Andrey Parfenov
 */
public interface IProjectDao extends IBaseEntityDao<Project> {

    /**
     * Gets a part of a list of the projects {@link com.floriparide.listings.model.Project}
     *
     * @param offset    row offset to return from
     * @param limit     number of rows to return
     * @return a list of projects {@link com.floriparide.listings.model.Project}
     * @throws IOException if I/O error occurred
     */
    @NotNull
    public List<Project> getProjects(int offset, int limit) throws Exception;

    /**
     * Gets a list of all projects {@link com.floriparide.listings.model.Project}
     *
     * @return a list of projects {@link com.floriparide.listings.model.Project}
     * @throws IOException if I/O error occurred
     */
    @NotNull
    //todo add order
    public List<Project> getProjects() throws Exception;

    /**
     * Gets project table size (count)
     *
     * @return the size of the projects list
     * @throws Exception
     */
    public int size() throws Exception;

    /**
     * Gets a part of a list of the projects {@link com.floriparide.listings.model.Project}
     * ordered by specified {@link com.floriparide.listings.model.sort.SortField} and
     * {@link com.floriparide.listings.model.sort.SortType}
     *
     * @param offset    row offset to return from
     * @param limit     number of rows to return
     * @param sortField sort field to sort by {@link com.floriparide.listings.model.sort.SortField}
     * @param sortType  sort type to sort  {@link com.floriparide.listings.model.sort.SortType}
     * @return a list of projects {@link com.floriparide.listings.model.Project} matching specified criteria
     * @throws IOException if I/O error occurred
     */
    @NotNull
    public List<Project> getProjects(int offset, int limit, @NotNull SortField sortField, @NotNull SortType sortType) throws Exception;
}

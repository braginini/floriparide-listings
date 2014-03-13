package com.floriparide.listings.dao;

import com.floriparide.listings.model.RawData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Mikhail Bragin
 */
public interface IRawDataDao {

	@NotNull
	void create(@NotNull List<RawData> rawData) throws Exception;

	@NotNull
	Long create(@NotNull RawData rawData) throws Exception;

}

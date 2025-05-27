package com.shell.core.models.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shell.core.models.TableColumn;

/**
 * Implementation of the TableColumn interface.
 * <p>
 * This class represents a table column value and provides methods
 * to access and check the presence of the value.
 * </p>
 * 
 * @author Mohammad Shoaib
 * @version 1.1
 * @since 26-05-2025
 */
public class TableColumnImpl implements TableColumn {

    private static final Logger logger = LoggerFactory.getLogger(TableColumnImpl.class);

    private final String columnValue;

    /**
     * Constructs a TableColumnImpl with the specified column value.
     *
     * @param columnValue the value of the column, can be null or blank
     */
    public TableColumnImpl(String columnValue) {
        logger.debug("Initialized TableColumnImpl with columnValue: {}", columnValue);
        this.columnValue = columnValue;
    }

    /**
     * Returns the value of the column or an empty string if null.
     *
     * @return the column value or an empty string
     */
    @Override
    public String getColumn() {
        String result = Optional.ofNullable(columnValue).orElse("");
        logger.debug("getColumn() called, returning: {}", result);
        return result;
    }

    /**
     * Checks whether the column has a non-blank value.
     *
     * @return true if the column value is not blank; false otherwise
     */
    @Override
    public boolean hasValue() {
        boolean hasValue = StringUtils.isNotBlank(columnValue);
        logger.debug("hasValue() called, result: {}", hasValue);
        return hasValue;
    }
}

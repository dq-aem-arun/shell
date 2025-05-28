package com.shell.core.models;

import java.util.List;

/**
 * Interface representing a single table row
 */
public interface TableRow {
    /**
     * Get all columns in this row
     *
     * @return List of TableColumn objects
     */
    List<TableColumn> getColumns();

    /**
     * Check if row has any columns
     * 
     * @return true if row has columns, false otherwise
     */
    boolean hasColumns();
}

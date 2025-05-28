package com.shell.core.models;

import java.util.List;

/**
 * Sling Model interface for Dynamic Table component
 */
public interface DynamicTable {

    /**
     * Get all table rows
     * 
     * @return List of TableRow objects
     */
    List<TableRow> getRows();

    /**
     * Check if table has any rows
     * 
     * @return true if table has rows, false otherwise
     */
    boolean hasRows();

}
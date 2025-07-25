package com.shell.core.models;

/**
 * Interface representing a single table column
 */
public interface TableColumn {

    /**
     * Get the column value
     * 
     * @return column value as String
     */
    String getColumn();

    /**
     * Check if column has value
     * 
     * @return true if column has non-empty value, false otherwise
     */
    boolean hasValue();
}

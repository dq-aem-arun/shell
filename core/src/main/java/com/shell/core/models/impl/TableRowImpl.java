package com.shell.core.models.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shell.core.models.TableColumn;
import com.shell.core.models.TableRow;

/**
 * Implementation of the TableRow interface.
 * <p>
 * This class reads a Sling Resource and extracts child resources
 * representing table columns. It creates {@link TableColumn} instances
 * based on the column data found.
 * </p>
 * 
 * @author Mohammad Shoaib
 * @version 1.1
 * @since 26-05-2025
 */
public class TableRowImpl implements TableRow {
    private static final Logger LOG = LoggerFactory.getLogger(TableRowImpl.class);

    private final Resource rowResource;
    private List<TableColumn> columns;

    /**
     * Constructs a TableRowImpl with the provided row resource.
     * 
     * @param rowResource the Sling resource representing the row
     */
    public TableRowImpl(Resource rowResource) {
        this.rowResource = rowResource;
        initColumns();
    }

    /**
     * Initializes the list of TableColumn instances from the "columns" child
     * resource.
     */
    private void initColumns() {
        this.columns = new ArrayList<>();

        LOG.info("Initializing columns for row: {}", rowResource.getName());

        // Debug: Log all child resources of this row
        LOG.info("Row child resources:");
        rowResource.getChildren().forEach(child -> {
            LOG.info("  - Row child: {} (type: {})", child.getName(), child.getResourceType());
            ValueMap childProps = child.getValueMap();
            LOG.info("    Properties: {}", childProps);
        });

        Resource columnsResource = rowResource.getChild("columns");
        if (columnsResource != null) {
            LOG.info("Found columns resource: {}", columnsResource.getPath());

            columnsResource.getChildren().forEach(columnResource -> {
                LOG.info("Processing column: {}", columnResource.getName());
                ValueMap columnProps = columnResource.getValueMap();
                LOG.info("Column properties: {}", columnProps);

                String columnValue = columnProps.get("column", String.class);
                LOG.info("Column value: '{}'", columnValue);

                if (StringUtils.isNotBlank(columnValue)) {
                    this.columns.add(new TableColumnImpl(columnValue));
                    LOG.info("Added column with value: '{}'", columnValue);
                } else {
                    LOG.info("Column value is blank, skipping");
                }
            });
        } else {
            LOG.warn("No 'columns' child resource found under: {}", rowResource.getPath());
        }

        LOG.info("Total columns for this row: {}", columns.size());
    }

    /**
     * Returns the list of columns in the row.
     * 
     * @return a list of TableColumn instances; never null
     */
    @Override
    public List<TableColumn> getColumns() {
        return columns != null ? columns : Collections.emptyList();
    }

    /**
     * Checks if the row contains any columns.
     * 
     * @return true if at least one column is present; false otherwise
     */
    @Override
    public boolean hasColumns() {
        boolean hasColumns = columns != null && !columns.isEmpty();
        LOG.info("Row hasColumns() returning: {}", hasColumns);
        return hasColumns;
    }
}

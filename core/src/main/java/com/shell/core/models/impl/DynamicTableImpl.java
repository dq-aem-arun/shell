package com.shell.core.models.impl;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.shell.core.models.DynamicTable;
import com.shell.core.models.TableRow;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementation of the DynamicTable Sling Model.
 * <p>
 * This model adapts a Sling resource representing a dynamic table,
 * initializing its row and column structure using child resources.
 * </p>
 * 
 * <p>
 * Extensive logging is included for debugging and traceability.
 * </p>
 * 
 * <p>
 * Expected structure:
 * </p>
 * <ul>
 * <li>{@code currentResource}
 * <ul>
 * <li>{@code rows}
 * <ul>
 * <li>{@code columns}</li>
 * </ul>
 * </li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @version 1.1
 * @since 26-05-2025
 * @author Mohammad Shoaib
 */
@Model(adaptables = Resource.class, adapters = DynamicTable.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class DynamicTableImpl implements DynamicTable {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicTableImpl.class);

    @SlingObject
    private Resource currentResource;

    private List<TableRow> tableRows;

    /**
     * Initializes the DynamicTable model after injection.
     */
    @PostConstruct
    protected void init() {
        LOG.info("Initializing DynamicTable for resource: {}", currentResource.getPath());

        this.tableRows = new ArrayList<>();

        // Log all properties of current resource
        ValueMap properties = currentResource.getValueMap();
        LOG.info("Current resource properties: {}", properties);

        // Log all child resources
        LOG.info("Child resources of current resource:");
        currentResource.getChildren().forEach(child -> {
            LOG.info("  - Child: {} (type: {})", child.getName(), child.getResourceType());
        });

        // Look for rows data
        Resource rowsResource = currentResource.getChild("rows");
        if (rowsResource != null) {
            LOG.info("Found rows resource: {}", rowsResource.getPath());

            rowsResource.getChildren().forEach(rowResource -> {
                LOG.info("Processing row: {}", rowResource.getName());
                ValueMap rowProps = rowResource.getValueMap();
                LOG.info("Row properties: {}", rowProps);

                TableRowImpl tableRow = new TableRowImpl(rowResource);
                if (tableRow.hasColumns()) {
                    this.tableRows.add(tableRow);
                    LOG.info("Added row with {} columns", tableRow.getColumns().size());
                } else {
                    LOG.info("Row has no columns, skipping");
                }
            });
        } else {
            LOG.warn("No 'rows' child resource found under: {}", currentResource.getPath());
        }

        LOG.info("Total rows processed: {}", tableRows.size());
    }

    /**
     * Returns the list of table rows.
     *
     * @return a list of {@link TableRow} instances; never null
     */
    @Override
    public List<TableRow> getRows() {
        return tableRows != null ? tableRows : Collections.emptyList();
    }

    /**
     * Indicates whether the table has any rows.
     *
     * @return true if at least one row exists; false otherwise
     */
    @Override
    public boolean hasRows() {
        boolean hasRows = tableRows != null && !tableRows.isEmpty();
        LOG.info("hasRows() returning: {}", hasRows);
        return hasRows;
    }
}
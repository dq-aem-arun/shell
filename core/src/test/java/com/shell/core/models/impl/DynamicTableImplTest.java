package com.shell.core.models.impl;

import com.shell.core.models.DynamicTable;
import com.shell.core.models.TableColumn;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

/**
 * Unit test for {@link DynamicTableImpl} Sling model.
 * Uses AEM Mocks to simulate the content structure and validate behavior.
 *
 * @author Mohammad Shoaib
 * @version 1.0
 * @since 27-05-2025
 */
@ExtendWith(AemContextExtension.class)
class DynamicTableImplTest {
    private final AemContext context = new AemContext();

    /**
     * Sets up the test context by registering models and loading the sample JSON structure.
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(DynamicTableImpl.class);
        context.load().json("/shellTable/shell-table.json", "/component");
    }

    /**
     * Verifies that the correct number of rows are loaded from the content structure.
     */
    @Test
    void getRowsTest() {
        context.currentResource("/component");
        DynamicTable table = context.currentResource().adaptTo(DynamicTable.class);
        Assertions.assertNotNull(table);

        int expected = 4;
        int actual = table.getRows().size();
        Assertions.assertEquals(expected, actual, "children of rows are not coming properly");
    }

    /**
     * Verifies that the table reports having rows.
     */
    @Test
    void hasRowsTest() {
        context.currentResource("/component");
        DynamicTable table = context.currentResource().adaptTo(DynamicTable.class);
        Assertions.assertNotNull(table);

        boolean expected = true;
        boolean actual = table.hasRows();
        Assertions.assertEquals(expected, actual);
        Assertions.assertTrue(() -> actual,"rows are empty! ");
    }

     /**
     * Verifies that the correct number of columns are present in the first row.
     */
    @Test
    void getColumnsTest() {
        context.currentResource("/component");
        DynamicTable tableRow = context.currentResource().adaptTo(DynamicTable.class);
        List<TableColumn> columns = tableRow.getRows().get(0).getColumns();

        Assertions.assertNotNull(tableRow);
        int expected = 5;
        int actual = columns.size();
        Assertions.assertEquals(expected, actual, "children of columns are not coming properly");
    }

    /**
     * Verifies that the first row has columns.
     */
    @Test
    void hasColumnsTest() {
        context.currentResource("/component");
        DynamicTable table = context.currentResource().adaptTo(DynamicTable.class);
        Assertions.assertNotNull(table);

        Assertions.assertTrue(() -> table.getRows().get(0).hasColumns(),"columns are empty!!");
    }

    /**
     * Verifies specific column values and checks for index out of bounds on rows.
     */
    @Test
    void getColumnValueTest(){
        context.currentResource("/component");
        DynamicTable table = context.currentResource().adaptTo(DynamicTable.class);
        Assertions.assertNotNull(table,() -> "Dynamic Table is null");

        Assertions.assertEquals("Vineet", table.getRows().get(1).getColumns().get(3).getColumn());
        Assertions.assertNotSame("vineet",table.getRows().get(1).getColumns().get(3).getColumn(),"Both are not String");
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> table.getRows().get(4));
    }

    /**
     * Verifies if a column has a non-blank value and checks column index bounds.
     */
    @Test
    void hasColumnValueTest(){
        context.currentResource("/component");
        DynamicTable tableRow = context.currentResource().adaptTo(DynamicTable.class);
        List<TableColumn> columns = tableRow.getRows().get(0).getColumns();

        boolean expected = true;
        boolean actual = columns.get(3).hasValue();
        Assertions.assertEquals(expected,actual,() -> "Column value not has in columns!");
        Assertions.assertThrows(IndexOutOfBoundsException.class, () -> columns.get(7),"index 7 not present in Table Columns");
    }
}
package com.shell.core.models;

import com.adobe.cq.export.json.ComponentExporter;

public interface Search extends ComponentExporter {
    String getPlaceholder();
    String getRootPath();
    int getMaxResults();
}

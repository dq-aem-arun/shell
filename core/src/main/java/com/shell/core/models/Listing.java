package com.shell.core.models;

import java.util.List;

public interface Listing {

    List<ArticleItem> getArticles();

    // Inner class representing a single article item
    public static class ArticleItem {
        private final String title;
        private final String description;
        private final String path;
        private final String imagePath;

        public ArticleItem(String title, String description, String path, String imagePath) {
            this.title = title;
            this.description = description;
            this.path = path;
            this.imagePath = imagePath;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getPath() {
            return path;
        }

        public String getImagePath() {
            return imagePath;
        }
    }
}

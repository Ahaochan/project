package com.ahao.entity;

import java.util.List;

/**
 * Created by Ahaochan on 2017/7/15.
 */
public class DropDownListDTO {
    private Item defaultItem;
    private List<Item> items;

    public DropDownListDTO() {
    }

    public DropDownListDTO(Item defaultItem, List<Item> items) {
        this.defaultItem = defaultItem;
        this.items = items;
    }

    public Item getDefaultItem() {
        return defaultItem;
    }

    public void setDefaultItem(Item defaultItem) {
        this.defaultItem = defaultItem;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public static class Item{
        private final String text;
        private final String url;

        public Item(String text, String url) {
            this.text = text;
            this.url = url;
        }

        public String getText() {
            return text;
        }

        public String getUrl() {
            return url;
        }
    }
}

package com.master.db.model;

public class PrefundedDocument {
    private String type;
    private Integer id;
    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    private String name;
    private String link;

    // No-argument constructor
    public PrefundedDocument() {
    }

    // Constructor with all fields
    public PrefundedDocument(String type, String name, String link) {
        this.type = type;
        this.name = name;
        this.link = link;
    }

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "PrefundedDocument{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", previewLink='" + link + '\'' +
                '}';
    }
}
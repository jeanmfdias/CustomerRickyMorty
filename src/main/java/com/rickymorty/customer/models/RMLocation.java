package com.rickymorty.customer.models;

import java.util.List;

public class RMLocation {
    private String name;
    private String type;
    private String dimension;
    private List<String> residents;

    public RMLocation(RickyMortyLocation location) {
        this.name = location.name();
        this.type = location.type();
        this.dimension = location.dimension();
        this.residents = location.residents();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public List<String> getResidents() {
        return residents;
    }

    public void setResidents(List<String> residents) {
        this.residents = residents;
    }
}

package com.rickymorty.customer.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "locations")
public class RickyMortyLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column
    private String type;

    @Column
    private String dimension;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<RickyMortyCharacter> residents;

    public RickyMortyLocation() {

    }

    public RickyMortyLocation(RickyMortyLocationRecord location) {
        this.name = location.name();
        this.type = location.type();
        this.dimension = location.dimension();
//        this.residents = location.residents();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<RickyMortyCharacter> getResidents() {
        return residents;
    }

    public void setResidents(List<RickyMortyCharacter> residents) {
        residents.forEach(r -> r.setLocation(this));
        this.residents = residents;
    }

    @Override
    public String toString() {
        return """
                #%d Name: %s | Type: %s | Dimension: %s
                Characters: (%s)
                """.formatted(
                this.getId(),
                this.getName(),
                this.getType(),
                this.getDimension(),
                this.getResidents());
    }
}

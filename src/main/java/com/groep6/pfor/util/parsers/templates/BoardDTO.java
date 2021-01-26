package com.groep6.pfor.util.parsers.templates;

import com.groep6.pfor.factories.FactionFactory;
import com.groep6.pfor.models.*;
import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.models.factions.FactionType;

import java.util.HashMap;
import java.util.Map;

/**
 * The Data Transfer Object that represents the board object in Firebase
 *
 * @author OwenElderbroek
 */

public class BoardDTO {
    public Map<String, CityDTO> cities;

    public BoardDTO() {}

    private BoardDTO(Map<String, CityDTO> cities) {
        this.cities = cities;
    }

    public Board toModel() {
        Map<String, Tile> cities = new HashMap<>();
        for (CityDTO city : this.cities.values()) cities.put(city.name, city.toModel());
        
        return new Board(cities);
    }

    public static BoardDTO fromModel(Board board) {
        Map<String, CityDTO> cities = new HashMap<>();
        for (Tile tile : board.getTiles()) {
            City city = (City) tile;
            cities.put(city.getCityName(), CityDTO.fromModel(city));
        }
        return new BoardDTO(cities);
    }
}

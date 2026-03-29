package cityrescue.classes;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a station on the grid that holds units.
 */
public class Station {
    private int id;
    private String name;
    private int xCoord;
    private int yCoord;
    private int capacity = 10; 
    private List<Unit> units;

    public Station(int id, String name, int xCoord, int yCoord) {
        this.id = id;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.name = name;
        this.units = new ArrayList<>();    
    } 

    public void setCapacity(int maxUnits){
        capacity = maxUnits;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getID(){
        return id;
    }

    public int getX() {
        return xCoord;
    }

    public int getY() {
        return yCoord;
    }

    public List<Unit> getUnits() {
        return units;    
    }

    public void addUnit(Unit unit) {
        units.add(unit);
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
    }

}
package cityrescue.classes;

import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;

/**
 * Represents a unit that is held in a station. 
 * Each unit has a type that represents the type of incidents it can handle.
 */
public abstract class Unit {
    protected int id;
    protected int stationID;
    protected int x;
    protected int y;
    protected UnitStatus status;
    protected Incident assignedIncident = null;
    protected int assignedIncidentId = 0;
    protected int ticksLeft = 0;

    public Unit(int id, int stationID, int x, int y){
        this.id = id;
        this.stationID = stationID;
        this.x = x;
        this.y = y;
        this.status = UnitStatus.IDLE;
    }

    public int getID() {
        return id;
    }

    public UnitStatus getStatus(){
        return status;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Incident getAssignedIncident() { 
        return assignedIncident; 
    }

    public void setX(int x) { 
        this.x = x; 
    }

    public void setY(int y) { 
        this.y = y; 
    
    }
    public void setAssignedIncident(Incident incident) { 
        this.assignedIncident = incident; 
    }

    public void setStatus(UnitStatus status) {
        this.status = status;
    }

    public int getStationId() {
        return stationID;
    }

    public boolean isAssigned() {
        if (status == UnitStatus.EN_ROUTE || status == UnitStatus.AT_SCENE) {
            return true;
        }
        return false;
    }

    public void setStationId(int stationID) {
        this.stationID = stationID;
    }

    public void setOutOfService(boolean out) {
        this.status = out ? UnitStatus.OUT_OF_SERVICE : UnitStatus.IDLE;
    }


    public String toString(){
        String incidentStr;
        if (assignedIncidentId == 0) {
            incidentStr = "-";
        } else {
            incidentStr = String.valueOf(assignedIncidentId);
        }
        return String.format("U#%d TYPE=%s HOME=%d LOC=(%d,%d) STATUS=%s INCIDENT=%s WORK=%d",
        id, getType(), stationID, x, y, status, incidentStr, ticksLeft);
    }
    
    public abstract boolean canHandle(Incident incident);
    public abstract String getType();
    public abstract int getTicksToResolve();

}



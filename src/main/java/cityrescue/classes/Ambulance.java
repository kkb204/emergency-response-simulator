package cityrescue.classes;

import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.IncidentType;

/**
 * A subclass of unit. 
 * Ambulances can only handle medical incidents.
 */
public class Ambulance extends Unit {
    public Ambulance(int id, int stationID, int x, int y) {
        super(id, stationID, x, y);
    }

    @Override
    public boolean canHandle(Incident incident) {
        if (incident.getType() == IncidentType.MEDICAL) {
            return true;
        }
        return false;
    }

    public int getTicksToResolve() { 
        return 2; 
    }

    @Override
    public String getType() {
        return "AMBULANCE";
    }

}
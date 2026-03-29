package cityrescue.classes;

import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.IncidentType;

/**
 * A subclass of unit. 
 * PoliceCar can only handle crimes.
 */
public class PoliceCar extends Unit {
    public PoliceCar(int id, int stationID, int x, int y) {
        super(id, stationID, x, y);
    }

    @Override
    public boolean canHandle(Incident incident) {
        if (incident.getType() == IncidentType.CRIME) {
            return true;
        }
        return false;
    }

    public int getTicksToResolve() { 
        return 3; 
    }

    @Override
    public String getType() {
        return "POLICE_CAR";
    }
}

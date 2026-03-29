package cityrescue.classes;

import cityrescue.enums.UnitType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.IncidentType;

/**
 * A subclass of unit. 
 * Fire engines can only handle fire incidents.
 */
public class FireEngine extends Unit {
    public FireEngine(int id, int stationID, int x, int y) {
        super(id, stationID, x, y);
    }

    @Override
    public boolean canHandle(Incident incident) {
        if (incident.getType() == IncidentType.FIRE) {
            return true;
        }
        return false;
    }

    public int getTicksToResolve() { 
        return 4;
    }

    @Override
    public String getType() {
        return "FIRE_ENGINE";
    }
}
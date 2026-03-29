import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import cityrescue.*;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

public class AddedTickTest {
    private CityRescue cr;

    @BeforeEach
    void setUp() throws Exception {
        cr = new CityRescueImpl();
        cr.initialise(5, 5);
    }
    @Test
    void tickIncrementsCounter() throws Exception {
        cr.tick();
        cr.tick();
        cr.tick();
        assert cr.getStatus().contains("TICK=3") == true;
    }

    @Test
    void ambulanceResolvesAfter2Ticks() throws Exception {
        int stationId = cr.addStation("A", 0, 0);
        int unitId = cr.addUnit(stationId, UnitType.AMBULANCE);
        int incidentId = cr.reportIncident(IncidentType.MEDICAL, 1, 0, 1);
        cr.dispatch();
        cr.tick();
        cr.tick();
        cr.tick();
        assert cr.viewIncident(incidentId).contains("STATUS=RESOLVED") == true;
        assert cr.viewUnit(unitId).contains("STATUS=IDLE") == true;
    }

    @Test
    void fireEngineResolvesAfter4Ticks() throws Exception {
        int stationId = cr.addStation("A", 0, 0);
        int unitId = cr.addUnit(stationId, UnitType.FIRE_ENGINE);
        int incidentId = cr.reportIncident(IncidentType.FIRE, 1, 0, 1);
        cr.dispatch();
        cr.tick();
        cr.tick();
        cr.tick();
        cr.tick();
        cr.tick();
        assert cr.viewIncident(incidentId).contains("STATUS=RESOLVED") == true;
    }

    @Test
    void policeCarResolvesAfter3Ticks() throws Exception {
        int stationId = cr.addStation("A", 0, 0);
        int unitId = cr.addUnit(stationId, UnitType.POLICE_CAR);
        int incidentId = cr.reportIncident(IncidentType.CRIME, 1, 0, 1);
        cr.dispatch();
        cr.tick();
        cr.tick();
        cr.tick();
        cr.tick();
        assert cr.viewIncident(incidentId).contains("STATUS=RESOLVED") == true;
    }
}
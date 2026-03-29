import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import cityrescue.*;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

public class AddedIncidentTest {
    private CityRescue cr;

    @BeforeEach
    void setUp() throws Exception {
        cr = new CityRescueImpl();
        cr.initialise(5, 5);
    }
    
    @Test
    // Check that cancelling an incident sets the unit status back to idle
    void cancelIncident_setsUnitToIdle() throws Exception {
        int stationId = cr.addStation("Central", 0, 0);
        int unitId = cr.addUnit(stationId, UnitType.AMBULANCE);  
        int incidentId = cr.reportIncident(IncidentType.MEDICAL, 1, 2, 2);
        cr.dispatch();
        cr.cancelIncident(incidentId);
        String unitDetails = cr.viewUnit(unitId);
        assertTrue(unitDetails.contains("STATUS=IDLE"));
    }

    @Test
    // Check that an exception is thrown if the new severity is below the old one
    void escalateIncident_throwsIfSeverityInvalid() throws Exception{
        int stationId = cr.addStation("Central", 0, 0);
        int incidentID = cr.reportIncident(IncidentType.MEDICAL, 3, 1, 1);
        assertThrows(IllegalStateException.class, () -> cr.escalateIncident(incidentID, 2));
    }

    @Test
    // Checks that a resolved incident can't be escalated 
    void escalateIncident_throwsIfResolved() throws Exception {
        int stationId = cr.addStation("A", 0, 0);
        int unitId = cr.addUnit(stationId, UnitType.AMBULANCE);
        int incidentId = cr.reportIncident(IncidentType.MEDICAL, 1, 0, 1);
        cr.dispatch();
        cr.tick();
        cr.tick();
        cr.tick();
        assertThrows(IllegalStateException.class, () -> cr.escalateIncident(incidentId, 3));
    }

    @Test
    // Checks that id's are returned in ascending order 
    void getIncidentIds_returnsInAscendingOrder() throws Exception {
        cr.reportIncident(IncidentType.MEDICAL, 1, 0, 0);
        cr.reportIncident(IncidentType.FIRE, 2, 1, 1);
        cr.reportIncident(IncidentType.CRIME, 3, 2, 2);
        assertArrayEquals(new int[]{1, 2, 3}, cr.getIncidentIds());
    }
}
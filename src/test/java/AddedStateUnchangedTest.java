import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import cityrescue.*;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

public class AddedStateUnchangedTest {
    private CityRescue cr;

    @BeforeEach
    void setUp() throws Exception {
        cr = new CityRescueImpl();
        cr.initialise(5, 5);
    }
    
    @Test
    void addStation_throwsStateUnchanged() throws Exception {
        int before = cr.getStationIds().length;
        assertThrows(InvalidLocationException.class, () -> cr.addStation("A", 99, 99));
        assert cr.getStationIds().length == before;
    }

    @Test
    void reportIncident_throwsStateUnchanged() throws Exception {
        int before = cr.getIncidentIds().length;
        assertThrows(InvalidSeverityException.class, () -> cr.reportIncident(IncidentType.MEDICAL, 99, 0, 0));
        assert cr.getIncidentIds().length == before;
    }

    @Test
    void transferUnit_throwsStateUnchanged() throws Exception {
        int stationId = cr.addStation("A", 0, 0);
        int stationId2 = cr.addStation("B", 3, 3);
        int unitId = cr.addUnit(stationId, UnitType.AMBULANCE);
        int incidentId = cr.reportIncident(IncidentType.MEDICAL, 1, 2, 2);
        cr.dispatch();
        assertThrows(IllegalStateException.class, () -> cr.transferUnit(unitId, stationId2));
        assert cr.viewUnit(unitId).contains("HOME=" + stationId) == true;
    }
}

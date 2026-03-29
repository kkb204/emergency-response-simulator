import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import cityrescue.*;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

public class AddedDispatchTest {
    private CityRescue cr;

    @BeforeEach
    void setUp() throws Exception {
        cr = new CityRescueImpl();
        cr.initialise(5, 5);
    }
    @Test
    void dispatchOnlyAssignsCompatibleUnits() throws Exception {
        int stationId = cr.addStation("Central", 1, 1);
        int unitId = cr.addUnit(stationId, UnitType.AMBULANCE);
        int incidentId = cr.reportIncident(IncidentType.FIRE, 3, 1, 1);
        cr.dispatch();
        String incidentDetails = cr.viewIncident(incidentId);
        assert incidentDetails.contains("UNIT=-") == true;
    }

    @Test
    void dispatchSetsCorrectStatuses() throws Exception {
        int stationId = cr.addStation("A", 0, 0);
        int unitId = cr.addUnit(stationId, UnitType.AMBULANCE);
        int incidentId = cr.reportIncident(IncidentType.MEDICAL, 1, 2, 2);
        cr.dispatch();
        assert cr.viewIncident(incidentId).contains("STATUS=DISPATCHED") == true;
        assert cr.viewUnit(unitId).contains("STATUS=EN_ROUTE") == true;
    }

    @Test
    void dispatchSkipsOutOfServiceUnits() throws Exception {
        int stationId = cr.addStation("A", 0, 0);
        int unitId = cr.addUnit(stationId, UnitType.AMBULANCE);
        cr.setUnitOutOfService(unitId, true);
        int incidentId = cr.reportIncident(IncidentType.MEDICAL, 1, 2, 2);
        cr.dispatch();
        assert cr.viewIncident(incidentId).contains("UNIT=-") == true;
    }

}

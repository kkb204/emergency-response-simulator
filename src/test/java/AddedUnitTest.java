import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import cityrescue.*;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

public class AddedUnitTest {
    private CityRescue cr;

    @BeforeEach
    void setUp() throws Exception {
        cr = new CityRescueImpl();
        cr.initialise(5, 5);
    }
    
    @Test
    // Checks that a unit can't be added if the type field is empty
    void addUnit_throwsIfTypeNull() throws Exception {
        int stationId = cr.addStation("Central", 0, 0);
        assertThrows(InvalidUnitException.class, () -> cr.addUnit(stationId, null));
    }

    @Test
    // Checks that you can't add a unit if the station is full 
    void addUnit_throwsIfStationFull() throws Exception {
        int stationId = cr.addStation("Central", 0, 0);
        cr.setStationCapacity(stationId, 1);
        cr.addUnit(stationId, UnitType.AMBULANCE);
        assertThrows(IllegalStateException.class, () -> cr.addUnit(stationId, UnitType.AMBULANCE));
    }

    @Test
    // Checks that you can't cancel a unit if it is EN_ROUTE
    void decommissionUnit_throwsIfActive() throws Exception {
        int stationId = cr.addStation("Central", 0, 0);
        int unitId = cr.addUnit(stationId, UnitType.AMBULANCE);
        int incidentId = cr.reportIncident(IncidentType.MEDICAL, 1, 2, 2);
        cr.dispatch();
        assertThrows(IllegalStateException.class, () -> cr.decommissionUnit(unitId));
    }

    @Test
    // Check that you can't transfer a unit to another station if it is active
    void transferUnit_throwsIfActive() throws Exception {
        int stationId = cr.addStation("Central", 0, 0);
        int stationId2 = cr.addStation("North", 4, 4);
        int unitId = cr.addUnit(stationId, UnitType.AMBULANCE);
        int incidentId = cr.reportIncident(IncidentType.MEDICAL, 1, 2, 2);
        cr.dispatch();
        assertThrows(IllegalStateException.class, () -> cr.transferUnit(unitId, stationId2));
    }

    @Test
    // Check that transferring a unit updates its location
    void transferUnit_updatesUnitLocation() throws Exception {
        int stationId = cr.addStation("Central", 0, 0);
        int stationId2 = cr.addStation("North", 3, 3);
        int unitId = cr.addUnit(stationId, UnitType.AMBULANCE);
        cr.transferUnit(unitId, stationId2);
        assertTrue(cr.viewUnit(unitId).contains("LOC=(3,3)"));
    }

    @Test
    // Checks that the unit id's are returned in ascending order
    void getUnitIds_returnsInAscendingOrder() throws Exception {
        int stationId = cr.addStation("Central", 0, 0);
        cr.addUnit(stationId, UnitType.AMBULANCE);
        cr.addUnit(stationId, UnitType.FIRE_ENGINE);
        cr.addUnit(stationId, UnitType.POLICE_CAR);
        assertArrayEquals(new int[]{1, 2, 3}, cr.getUnitIds());
    }

}
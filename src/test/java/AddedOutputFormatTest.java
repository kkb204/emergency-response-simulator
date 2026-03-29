import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import cityrescue.*;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

public class AddedOutputFormatTest {
    private CityRescue cr;

    @BeforeEach
    void setUp() throws Exception {
        cr = new CityRescueImpl();
        cr.initialise(5, 5);
    }
    
    @Test
    void viewUnit_showsDashWhenNoIncident() throws Exception {
        int stationId = cr.addStation("Central", 0, 0);
        int unitId = cr.addUnit(stationId, UnitType.AMBULANCE);
        assert cr.viewUnit(unitId).contains("INCIDENT=-") == true;
    }

    @Test
    void getStatus_showsCorrectTickValue() throws Exception {
        cr.tick();
        cr.tick();
        assert cr.getStatus().contains("TICK=2") == true;
    }

    @Test
    void getStatus_showsCorrectCounts() throws Exception {
        int stationId = cr.addStation("A", 0, 0);
        cr.addUnit(stationId, UnitType.AMBULANCE);
        cr.reportIncident(IncidentType.MEDICAL, 1, 2, 2);
        cr.addObstacle(3, 3);
        assert cr.getStatus().contains("STATIONS=1 UNITS=1 INCIDENTS=1 OBSTACLES=1") == true;
    }
}
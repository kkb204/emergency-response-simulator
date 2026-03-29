import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

import cityrescue.*;
import cityrescue.enums.*;
import cityrescue.exceptions.*;

public class AddedBasicsTest {
    private CityRescue cr;

    @BeforeEach
    void setUp() throws Exception {
        cr = new CityRescueImpl();
        cr.initialise(5, 5);
    }

    // Check that the initialise function resets stations, units and incidents
    @Test
    void initialise_resetsArrays() throws Exception {
        // Initialise the grid 
        cr = new CityRescueImpl();
        cr.initialise(5, 5);
        // Add a station
        cr.addStation("Central", 1, 1);
        // Initialise the grid again
        cr.initialise(4, 4);
        // Check that the initialise function removed any created stations from the array
        assert cr.getStationIds().length == 0;
    }
    
    // Check that stations aren't removed if there are still units in it
    @Test
    void removeStation_throwsIllegalState() throws Exception{
        // Create a station with an ambulance
        int stationId = cr.addStation("Central", 1, 1);
        cr.addUnit(stationId, UnitType.AMBULANCE);
        // Check that an exception is thrown if removing is attempted
        assertThrows(IllegalStateException.class, () -> cr.removeStation(stationId));
    }

    @Test
    // Check that an exception is thrown if the capacity is set below the number of current units
    void setStationCapacity_throwsIfBelowCurrentUnitCount() throws Exception {
        int stationId = cr.addStation("A", 0, 0);
        cr.addUnit(stationId, UnitType.AMBULANCE);
        cr.addUnit(stationId, UnitType.AMBULANCE);
        assertThrows(InvalidCapacityException.class, () -> cr.setStationCapacity(stationId, 1));
    }

    @Test
    // Check that stations can't be added if the location chosen is blocked
    void addStation_throwsIfLocationBlocked() throws Exception {
        cr.addObstacle(2, 2);
        assertThrows(InvalidLocationException.class, () -> cr.addStation("A", 2, 2));
    }

    @Test
    // Check that station id's are given in ascending order
    void getStationIds_returnsInAscendingOrder() throws Exception {
        cr.addStation("A", 0, 0);
        cr.addStation("B", 1, 1);
        cr.addStation("C", 2, 2);
        assertArrayEquals(new int[]{1, 2, 3}, cr.getStationIds());
    }

    @Test
    // Check that a station can't be created if the name field is empty
    void addStation_throwsIfNameBlankOrNull() throws Exception {
        assertThrows(InvalidNameException.class, () -> cr.addStation(null, 0, 0));
        assertThrows(InvalidNameException.class, () -> cr.addStation("", 0, 0));
    }
}


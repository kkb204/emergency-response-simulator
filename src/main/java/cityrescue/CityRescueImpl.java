package cityrescue;

import cityrescue.enums.*;
import cityrescue.exceptions.*;
import java.util.List;
import java.util.ArrayList;
import cityrescue.classes.*;

/**
 * CityRescueImpl (Starter)
 *
 * Your task is to implement the full specification.
 * You may add additional classes in any package(s) you like.
 */
public class CityRescueImpl implements CityRescue {
    // Fields added for map width and height, tick, stations, units and incidents
    private int width;
    private int height;

    private int tick;
    private int stationID = 1;

    private List<Station> stations;
    private List<Unit> units;
    private List<Incident> incidents;

    private static final int MAX_STATIONS = 20;
    private static final int MAX_UNITS = 50;
    private static final int MAX_INCIDENTS = 200;

    // Counters for auto-incrementing IDs
    private int unitID = 1;
    private int incidentID = 1;

    private CityMap cityMap;

    // Constructor to create three empty arrays (will be called each time the object is created)
    public CityRescueImpl() {
        stations = new ArrayList<>();
        units = new ArrayList<>();
        incidents = new ArrayList<>();
        tick = 0;
        cityMap = new CityMap(width, height);
    }

    /**
     * Creates a new grid and resets values for stations, units and incidents
     *
     * @param width the width of the grid to be created
     * @param height the height of the grid to be created
     * @throws InvalidGridException if the width and height are 0 or negative
     */
    @Override
    public void initialise(int width, int height) throws InvalidGridException {
        // Ensure the width and height are greater than 0, otherwise throw an exception
        if (width <= 0 || height <= 0) {
            throw new InvalidGridException("Width and height must be greater than 0.");
        }
        this.height = height;
        this.width = width;
        this.tick = 0;

        // Create a new grid
        cityMap = new CityMap(width, height);
        // Reset stations, units and incidents to be empty
        stations.clear();
        units.clear();
        incidents.clear();
        
        // Reset ID counters for determinism
        stationID = 1;
        unitID = 1;
        incidentID = 1;

    }

   
    @Override
    public int[] getGridSize() {
        return new int[] {width, height};
    }

     /**
     * Adds an obstacle to the grid
     *
     * @param x the x coordinate where the obstacle will be added
     * @param y the y coordinate where the obstacle will be added
     * @throws InvalidLocationException if x and y are 0, negative or out of bounds of the grid
     */
    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new InvalidLocationException("The cell is out of bounds.");
        }
        cityMap.setBlocked(x, y);
    }

    /**
     * Removes an obstacle from the grid
     *
     * @param x the x coordinate where the obstacle is to be removed from
     * @param y the y coordinate where the obstacle is to be removed from
     * @throws InvalidLocationException if x and y are 0, negative or out of bounds of the grid
     */
    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new InvalidLocationException("The cell is out of bounds.");
        }
        cityMap.setUnblocked(x, y);
    }

    /**
     * Adds a new station to the grid.
     *
     * @param name the name of the station
     * @param x the x coordinate of the station
     * @param y the y coordinate of the station
     * @return the ID of the new station
     * @throws InvalidNameException if the name is empty or null
     * @throws InvalidLocationException if the location is out of bounds or blocked
     */
    @Override
    public int addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException, CapacityExceededException {
        if (stations.size() >= MAX_STATIONS) {
            throw new CapacityExceededException("Maximum stations reached.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidNameException("Station name cannot be blank.");
        }
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new InvalidLocationException("Station location is not in bounds.");
        }
        if (cityMap.isBlocked(x, y)) {
            throw new InvalidLocationException("Station location is blocked.");
        }
        
        int newStationID = stationID++;
        Station station = new Station(newStationID, name, x, y);
        stations.add(station);
        return newStationID;

    }

    /**
     * Removes a station from the map.
     *
     * @param stationId the id of the station to be removed 
     * @throws IDNotRecognisedException if the id given does not correspond to a station
     * @throws IllegalStateException if the station still has units in it
     */
    @Override
    public void removeStation(int stationId) throws IDNotRecognisedException, IllegalStateException {
        Station station = null;
        for (Station s : stations) {
        if (s.getID() == stationId) {
            station = s;
            break;
            }
        }
        if (station == null) {
            throw new IDNotRecognisedException("Station ID " + stationId + " not recognised");
        }
        if (station.getUnits().size() > 0) {
            throw new IllegalStateException("Station still owns units, cannot close");
        }
        stations.remove(station);

        int x = station.getX();
        int y = station.getY();
        cityMap.setUnblocked(x, y);
    }

    /**
     * Sets the capacity of the station.
     *
     * @param stationId the id of the station
     * @param maxUnits the maximum number of units allowed in the station
     * @throws IDNotRecognisedException if the id given does not correspond to a station
     * @throws InvalidCapacityException if the maximum units is less than 0 or less than the current amount of units
     */
    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {
        if (maxUnits <= 0) {
            throw new InvalidCapacityException("Capacity must be greater than 0.");
        }

        Station station = null;
        for (Station s : stations) {
            if (s.getID() == stationId) {
                station = s;
                break;
            }
        }

        if (station == null) {
            throw new IDNotRecognisedException("Station ID cannot be recognised.");
        }
        if (maxUnits < station.getUnits().size()) {
            throw new InvalidCapacityException("Capacity cannot be less than current unit count.");
        }

        station.setCapacity(maxUnits);
    }


    @Override
    public int[] getStationIds() {
        stations.sort((a, b) -> Integer.compare(a.getID(), b.getID()));

        int[] ids = new int[stations.size()];
        for (int i = 0; i < stations.size(); i++) {
            ids[i] = stations.get(i).getID();
        }
        return ids;
    }

    /**
     * Adds a unit to a station.
     *
     * @param stationId the id of the selected station
     * @param type the type of unit to be added to the station
     * @throws IDNotRecognisedException if the id given does not correspond to a station
     * @throws InvalidUnitException if the id given does not correspond to a station
     * @throws IllegalStateException if the station is at full capacity
     * @throws CapacityExceededException if maximum units has been reached
     */
    @Override
    public int addUnit(int stationId, UnitType type) 
        throws IDNotRecognisedException, InvalidUnitException, IllegalStateException, CapacityExceededException {
        if (units.size() >= MAX_UNITS) {
            throw new CapacityExceededException("Maximum units reached.");
        }
        // Find station
        Station station = null;
        for (Station s : stations) {
            if (s.getID() == stationId) {
                station = s;
                break;
            }
        }

        if (station == null) {
            throw new IDNotRecognisedException("Station ID not recognised.");
        }

        if (type == null) {
        throw new InvalidUnitException("Unit type cannot be null.");
        }


        // Check capacity
        if (station.getUnits().size() >= station.getCapacity()) {
            throw new IllegalStateException("Station is at full capacity.");
        }

        // Create new unit with auto ID
        int newUnitID = unitID++;
        Unit unit;
        if (type == UnitType.AMBULANCE) {
            unit = new Ambulance(newUnitID, stationId, station.getX(), station.getY());
        } else if (type == UnitType.FIRE_ENGINE) {
            unit = new FireEngine(newUnitID, stationId, station.getX(), station.getY());
        } else if (type == UnitType.POLICE_CAR) {
            unit = new PoliceCar(newUnitID, stationId, station.getX(), station.getY());
        } else {
            throw new InvalidUnitException("Unknown unit type.");
        }
        units.add(unit);
        station.addUnit(unit);

        return newUnitID;
        }

    /**
     * Removes a unit from the system
     *
     * @param unitId the id of the unit to be removed 
     * @throws IDNotRecognisedException if unit id doesn't correspond to a unit
     * @throws IllegalStateException if the unit is active
     */
    @Override
    public void decommissionUnit(int unitId)
        throws IDNotRecognisedException, IllegalStateException {

        Unit unit = null;
        for (Unit u : units) {
            if (u.getID() == unitId) {
                unit = u;
                break;
            }
        }

        if (unit == null) {
            throw new IDNotRecognisedException("Unit ID not recognised.");
        }

        if (unit.getStatus() == UnitStatus.EN_ROUTE
        || unit.getStatus() == UnitStatus.AT_SCENE) {
        throw new IllegalStateException("Unit cannot be decommissioned while active.");
        }

        // Remove from station
        for (Station s : stations) {
            if (s.getID() == unit.getStationId()) {
                s.removeUnit(unit);
                break;
            }
        }

        units.remove(unit);
        }

    /**
     * Transfers a unit to a different station
     *
     * @param unitId the unit id of the unit to be transferred
     * @param newStationId the id of the station to move the unit to 
     * @throws IDNotRecognisedException if unit id doesn't correspond to a unit
     * @throws IllegalStateException if the unit is active
     */
    @Override
    public void transferUnit(int unitId, int newStationId)
        throws IDNotRecognisedException, IllegalStateException {
        Unit unit = null;
        for (Unit u : units) {
            if (u.getID() == unitId) {
                unit = u;
                break;
            }
        }

        if (unit == null) {
            throw new IDNotRecognisedException("Unit ID not recognised.");
        }

        if (unit.getStatus() != UnitStatus.IDLE) {
        throw new IllegalStateException("Unit must be IDLE to transfer.");
        }

        Station newStation = null;
        for (Station s : stations) {
            if (s.getID() == newStationId) {
                newStation = s;
                break;
            }
        }

        if (newStation == null) {
            throw new IDNotRecognisedException("New station not recognised.");
        }

        if (newStation.getUnits().size() >= newStation.getCapacity()) {
            throw new IllegalStateException("New station at capacity.");
        }

        // Remove from old station
        for (Station s : stations) {
            if (s.getID() == unit.getStationId()) {
                s.removeUnit(unit);
                break;
            }
        }

        unit.setX(newStation.getX());
        unit.setY(newStation.getY());
        newStation.addUnit(unit);
        unit.setStationId(newStationId);
    }

    /**
     * Sets the status of the unit to be out of service.
     *
     * @param unitId the id of the unit to be set out of service 
     * @param outOfService true to set the unit out of service and false to set it to IDLE
     * @throws IDNotRecognisedException if the unit ID does not exist
     * @throws IllegalStateException if the unit is currently assigned to an incident
     */
    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService)
        throws IDNotRecognisedException, IllegalStateException {

        Unit unit = null;
        for (Unit u : units) {
            if (u.getID() == unitId) {
                unit = u;
                break;
            }
        }

        if (unit == null) {
            throw new IDNotRecognisedException("Unit not recognised.");
        }

        if (unit.isAssigned()) {
            throw new IllegalStateException("Unit is currently assigned.");
        }

        unit.setOutOfService(outOfService);
    }

    @Override
    public int[] getUnitIds() {
        units.sort((a, b) -> Integer.compare(a.getID(), b.getID()));

        int[] ids = new int[units.size()];
        for (int i = 0; i < units.size(); i++) {
            ids[i] = units.get(i).getID();
        }
        return ids;
    }

    /**
     * Views the status of a unit.
     *
     * @param unitId the id of the unit
     * @return a string that contains all information about the unit 
     * @throws IDNotRecognisedException if the ID given cannot be found
     */
    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {

        for (Unit u : units) {
            if (u.getID() == unitId) {
                return u.toString(); // assumes toString formatted correctly
            }
        }

        throw new IDNotRecognisedException("Unit not recognised.");
    }

    /**
     * Reports an incident so that a unit can be assigned to it.
     *
     * @param type the type of incident 
     * @param severity an integer for the severity of the incident
     * @param x the x coordinate of the station
     * @param y the y coordinate of the station
     * @return the ID of the new incident 
     * @throws InvalidSeverityException if the severity is not in the range 1-5
     * @throws InvalidLocationException if the location is out of bounds or blocked
     * @throws CapacityExceededException if no more incidents can be added to the list
     */
    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y)
        throws InvalidSeverityException, InvalidLocationException, CapacityExceededException {
        if (incidents.size() >= MAX_INCIDENTS) {
            throw new CapacityExceededException("Maximum incidents reached.");
        }

        if (type == null) {
        throw new InvalidLocationException("Incident type cannot be null.");
        }

        if (severity < 1 || severity > 5) {
            throw new InvalidSeverityException("Severity must be between 1 and 5.");
        }

        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new InvalidLocationException("Invalid incident location.");
        }

        if (cityMap.isBlocked(x, y)) {
            throw new InvalidLocationException("Location is blocked.");
        }

        int newIncidentID = incidentID++;
        Incident incident = new Incident(newIncidentID, x, y, severity, type);

        incidents.add(incident);

        return newIncidentID;
    }

    /**
     * Cancels a reported incident 
     *
     * @param incidentId the id of the incident to be cancelled
     * @throws IDNotRecognisedException if the incident id cannot be found
     * @throws IllegalStateException if the incident status is not reported or dispatched
     */
    @Override
    public void cancelIncident(int incidentId)
        throws IDNotRecognisedException, IllegalStateException {

        Incident incident = null;
        for (Incident i : incidents) {
            if (i.getID() == incidentId) {
                incident = i;
                break;
            }
        }

        if (incident == null) {
            throw new IDNotRecognisedException("Incident not recognised.");
        }

        if (incident.getStatus() != IncidentStatus.REPORTED &&
            incident.getStatus() != IncidentStatus.DISPATCHED) {
            throw new IllegalStateException("Incident cannot be cancelled.");
        }

        if (incident.getStatus() == IncidentStatus.DISPATCHED) {
            Unit unit = incident.getAssignedUnit();
            if (unit != null) {
                unit.setStatus(UnitStatus.IDLE);
                incident.setAssignedUnit(null);
            }
        }

        incident.setStatus(IncidentStatus.CANCELLED);
    }

    /**
     * Increases the severity of an incident
     *
     * @param incidentId the id of the incident to be escalated 
     * @param newSeverity the severity to escalate the incident to
     * @throws IDNotRecognisedException if the incident id cannot be found
     * @throws InvalidSeverityException if the severity is not in the range 1-5 or less than the current severity  
     * @throws IllegalStateExceptionif if the incident has been resolved
     */
    @Override
    public void escalateIncident(int incidentId, int newSeverity)
        throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {

        if (newSeverity < 1 || newSeverity > 5) {
            throw new InvalidSeverityException("Severity must be between 1 and 5.");
        }

        for (Incident i : incidents) {
            if (i.getID() == incidentId) {
                if (i.getStatus() == IncidentStatus.RESOLVED) {
                    throw new IllegalStateException("Incident resolved.");
                }

                if (newSeverity <= i.getSeverity()) {
                    throw new IllegalStateException("New severity must be higher.");
                }

                i.setSeverity(newSeverity);
                return;
            }
        }

        throw new IDNotRecognisedException("Incident not recognised.");
    }

    @Override
    public int[] getIncidentIds() {
        incidents.sort((a, b) -> Integer.compare(a.getID(), b.getID()));

        int[] ids = new int[incidents.size()];
        for (int i = 0; i < incidents.size(); i++) {
            ids[i] = incidents.get(i).getID();
        }
        return ids;
    }

    /**
     * Returns the details of an incident.
     *
     * @param incidentId the id of the incident 
     * @return a string containing all the information about the incident 
     * @throws IDNotRecognisedException if the incident id cannot be found 
     */
    @Override
    public String viewIncident(int incidentId)
        throws IDNotRecognisedException {

        for (Incident i : incidents) {
            if (i.getID() == incidentId) {
                return i.toString();
            }
        }

        throw new IDNotRecognisedException("Incident not recognised.");
    }

    /**
     * Assigns the a unit to an incident depending on which is the closest.
     * If the distances are equal, assign the unit with the lowest ID.
     * Units are elligible if they are IDLE, not out of service and fit the type of incident.
     * When a unit is assigned, its status changes the EN_ROUTE and incident status changes to DISPATCHED.
     * The incident then stores the object reference of the assigned unit.
     */
    @Override
    public void dispatch() {

        incidents.sort((a, b) -> Integer.compare(a.getID(), b.getID()));

        for (Incident incident : incidents) {

            if (incident.getStatus() != IncidentStatus.REPORTED)
                continue;

            Unit bestUnit = null;
            int bestDistance = Integer.MAX_VALUE;

            for (Unit unit : units) {
                if (unit.getStatus() != UnitStatus.IDLE)
                    continue;
                if (unit.getStatus() == UnitStatus.OUT_OF_SERVICE)
                    continue;
                if (unit.canHandle(incident) == false) 
                    continue;

                // Manhattan distance
                int distance = Math.abs(unit.getX() - incident.getX())
                            + Math.abs(unit.getY() - incident.getY());

                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestUnit = unit;
                }

                // Tie-breaker: lower unitId
                else if (distance == bestDistance &&
                        bestUnit != null &&
                        unit.getID() < bestUnit.getID()) {
                    bestUnit = unit;
                }
            }

            if (bestUnit != null) {
                bestUnit.setStatus(UnitStatus.EN_ROUTE);
                bestUnit.setAssignedIncident(incident);
                incident.setAssignedUnit(bestUnit);
                incident.setStatus(IncidentStatus.DISPATCHED);
            }
        }
    }

    /**
    * Advances the simulation by one tick.
    * During each tick, EN_ROUTE units move one step closer to their assigned incident,
    * units attempt to minimise Manhattan distance to their incident.
    *
    * For incidents that are IN_PROGRESS, work ticks are decremented and once they reach 0 the incident becomes resolved.
    * Units are return to IDLE status.
    *
    * When units arrive at scene, their status changes from EN_ROUTE to AT_SCENE
    */
    @Override
    public void tick() {
        tick++;

        // Sort units in ascending order
        units.sort((a, b) -> Integer.compare(a.getID(), b.getID()));


        for (Unit unit : units) {
            if (unit.getStatus() == UnitStatus.EN_ROUTE) {
                Incident incident = unit.getAssignedIncident();
                if (incident == null) continue;

                int unitX = unit.getX();
                int unitY = unit.getY();
                int incidentX = incident.getX();
                int incidentY = incident.getY();
                int currentDist = Math.abs(unitX - incidentX) + Math.abs(unitY - incidentY);

                int[] xChange = {0, 1, 0, -1};
                int[] yChange = {1, 0, -1, 0};

                for (int d = 0; d < 4; d++) {
                    int newX = unitX + xChange[d];
                    int newY = unitY + yChange[d];

                    if (newX < 0 || newX >= width || newY < 0 || newY >= height)
                        continue;
                    if (cityMap.isBlocked(newX, newY))
                        continue;

                    if ((Math.abs(newX - incidentX) + Math.abs(newY - incidentY)) < currentDist) {
                        unit.setX(newX);
                        unit.setY(newY);
                        break;
                    }
                }
            }
        }

        incidents.sort((a, b) -> Integer.compare(a.getID(), b.getID()));
        for (Incident incident : incidents) {
            if (incident.getStatus() == IncidentStatus.IN_PROGRESS) {
                incident.decrementTicks();

                if (incident.isResolved()) {
                    Unit unit = incident.getAssignedUnit();
                    if (unit != null) {
                        unit.setStatus(UnitStatus.IDLE);
                        unit.setAssignedIncident(null);
                        incident.clearAssignedUnit();                    
                    }
                }
            }
        }

        for (Unit unit : units) {
            if (unit.getStatus() == UnitStatus.EN_ROUTE) {
                Incident incident = unit.getAssignedIncident();
                if (incident == null) continue;

                if (unit.getX() == incident.getX() &&
                    unit.getY() == incident.getY()) {

                    unit.setStatus(UnitStatus.AT_SCENE);
                    incident.inProgress(unit.getTicksToResolve());
                }
            }
        }
    }

    /**
     * Formats the number of stations, units, incidents, obstacles and ticks into a string.
     * Adds each incident and unit in ascending order to the status string.
     * @return a string containing the overall status of the simulation
     */
    @Override
    public String getStatus() {
        String status = "";

        // Add tick and counts
        status = "TICK=" + tick + "\n" + "STATIONS=" + stations.size() + " UNITS=" + units.size() +
                " INCIDENTS=" + incidents.size() + " OBSTACLES=" + cityMap.countObstacles() + "\n" + "INCIDENTS\n";

        // Add incidents in ascending order
        incidents.sort((a, b) -> Integer.compare(a.getID(), b.getID()));
        for (Incident i : incidents) {
            status += i.toString() + "\n";
        }

        // Add units in ascending order
        status += "UNITS\n";
        units.sort((a, b) -> Integer.compare(a.getID(), b.getID()));
        for (Unit u : units) {
            status += u.toString() + "\n";
        }

        return status;
    }
}

package cityrescue.classes;

import cityrescue.enums.IncidentType;
import cityrescue.enums.IncidentStatus;
import cityrescue.enums.UnitStatus;

/**
 * Represents an incident that has occured on the map. 
 * Each incident has a severity and a type.
 */
public class Incident {
    private int id;
    private int x;
    private int y;
    private int severity;
    private IncidentType type;

    private IncidentStatus status;
    private Unit assignedUnit;
    private int ticksLeft;

    public Incident(int id, int x, int y, int severity, IncidentType type) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.severity = severity;
        this.type = type;
        this.status = IncidentStatus.REPORTED;
        this.assignedUnit = null;
        this.ticksLeft = 0;
    }

    public int getId() {
        return id;
    }
    public IncidentType getType() {
        return type;
    }
    public int getSeverity() {
        return severity;
    }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public IncidentStatus getStatus() {
        return status;
    }
    public Unit getAssignedUnit() {
        return assignedUnit;
    }

    public int getTicksLeft() {
        return ticksLeft;
    }

    public int getID() {
        return id; 
    }

    public void setStatus(IncidentStatus status) { 
        this.status = status; 
    }

    public void setSeverity(int severity) { 
        this.severity = severity; 
    }

    public void setAssignedUnit(Unit unit) {
        this.assignedUnit = unit;
        this.status = IncidentStatus.DISPATCHED;
    }

    public void clearAssignedUnit() {
        this.assignedUnit = null;
    }

    public void inProgress(int ticksRequired) {
        this.status = IncidentStatus.IN_PROGRESS;
        this.ticksLeft = ticksRequired;
    }
    public boolean isResolved() {
        return this.status == IncidentStatus.RESOLVED;
    }

    public boolean isCancelled() {
        return this.status == IncidentStatus.CANCELLED;
    }

    public void decrementTicks() {
        if (ticksLeft > 0) {
            ticksLeft -= 1;
        }
        if (ticksLeft == 0 && this.status == IncidentStatus.IN_PROGRESS) {
            this.status = IncidentStatus.RESOLVED;
            if (assignedUnit != null) {
                assignedUnit.setStatus(UnitStatus.IDLE);
            }
        }
    }

    public void escalate(int newSeverity) {
        this.severity = newSeverity;
    }

    public String toString() {
        String unitStr;
        if (assignedUnit == null) {
            unitStr = "-";
        } else {
            unitStr = String.valueOf(assignedUnit.getID());
        }
        return String.format("I#%d TYPE=%s SEV=%d LOC=(%d,%d) STATUS=%s UNIT=%s",
        id, type, severity, x, y, status, unitStr);
    }
} 
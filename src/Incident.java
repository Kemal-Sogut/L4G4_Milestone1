/**
 * Incident represents an incident in this project
 * @author Sam Touma 101033509
 */

 import java.sql.Time;

public class Incident {
    public enum Severity {
        LOW,
        MODERATE,
        HIGH
    }

    public enum Type {
        DRONE_REQUEST,
        FIRE_DETECTED
        }

    private Time time;
    private int id;
    private Severity severity;
    private Type type;
    
    public Incident(int hour, int minute, int second, int id, Severity severity, Type type){
        time = new Time(((hour* 60 + minute)* 60 + second) * 1000);
        this.id = id;
        this.severity = severity;
        this.type = type;
    }

    /**
     * Gets ID of the zone with the incident
     * @return the ID
     */
    public int getID(){
        return id;
    }

    /**
     * Gets the severity of the incident
     * @return the severity
     */
    public Severity getSeverity(){
        return severity;
    }

    /**
     * Gets the time of the incident
     * @return the time
     */
    public Time getTime(){
        return time;
    }

    /**
     * Gets the severity of incident.
     * @return the type
     */
    public Type getType(){
        return type;
    }
}

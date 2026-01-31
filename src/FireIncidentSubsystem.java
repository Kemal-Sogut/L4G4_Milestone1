/**
 * The FireIncidentSubsystem class reads the input files that has all the information about the fires
 * and relays that information to the scheduler
 * @author Sam Touma 101033509
 */

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;

public class FireIncidentSubsystem implements Runnable{

    //attributes for the FireIncidentSubsystem
    private Scheduler scheduler;
    private String zoneInput, eventInput;
    private Box sendBox, recieveBox;
    private int numCompleted;


    //Constructor
    public FireIncidentSubsystem(Scheduler scheduler, String zoneInput, String eventInput, Box sendBox, Box recieveBox){
        this.scheduler = scheduler;
        this.zoneInput = zoneInput;
        this.eventInput = eventInput;
        this.sendBox = sendBox;
        this.recieveBox = recieveBox;
        numCompleted = 0;
    }

    /**
     * Read input CSV files and relay information to scheduler
     */
    public void run(){
        HashMap<Integer, Zone> zones = null;
        try {
            zones = readZones();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Incident> incidents = null;
        try {
            incidents = readIncidents();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.printf("number of incidents loaded %d.\n", incidents.size());
        for (Incident incident: incidents){
            sendBox.put(new IncidentMessage(incident.getSeverity(), zones.get(incident.getID()).getStart(), zones.get(incident.getID()).getEnd(), incident.getTime(), incident.getType()));
            recieveBox.get();
            System.out.println("Received job completion token for fire incident");
            numCompleted++;
        }
        sendBox.put(null);
    }

    /**
     * Reads the zone input file.
     * @return an HashMap of all zones
     */
    private HashMap<Integer, Zone> readZones() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(zoneInput));
        HashMap<Integer, Zone> zones = new HashMap<>();
        sc.nextLine();
        while (sc.hasNextLine()){
            String[] line = sc.nextLine().trim().split(",");
            String[] start = line[1].split("();");
            String[] end = line[2].split("();");
            zones.put(
                    Integer.parseInt(line[0]),
                    new Zone(Integer.parseInt(start[0].substring(1)),
                            Integer.parseInt(start[1].substring(0,1)),
                            Integer.parseInt(end[0].substring(1)),
                            Integer.parseInt(end[1].substring(0, (end[1].length())-1))));
        }
        sc.close();
        return zones;
    }

    /**
     * Reads the event input file.
     * @return an ArrayList of all events
     */
    private ArrayList<Incident> readIncidents() throws FileNotFoundException {
        Scanner sc = new Scanner(new File (eventInput));
        ArrayList<Incident> incidents = new ArrayList<>();
        sc.nextLine();
        while (sc.hasNextLine()){
            String[] line = sc.nextLine().trim().split(",");

            String[] time = line[0].split(":");

            Incident.Type type;
            if (line[2].equals("FIRE_DETECTED")) type = Incident.Type.FIRE_DETECTED;
            else type = Incident.Type.DRONE_REQUEST;

            Incident.Severity severity;
            if (line[3].equals("High")) severity = Incident.Severity.HIGH;
            else if (line[3].equals("Moderate")) severity = Incident.Severity.MODERATE;
            else severity = Incident.Severity.LOW;

            incidents.add(new Incident(Integer.parseInt(time[0]), Integer.parseInt(time[1]), Integer.parseInt(time[2]), Integer.parseInt(line[1]), severity, type));
        }
        sc.close();
        return incidents;
    }

    /**
     * Gets the numbers of incidents that have been completed
     * @return the number of incidents
     */
    public int getNumCompleted(){
        return numCompleted;
    }

}

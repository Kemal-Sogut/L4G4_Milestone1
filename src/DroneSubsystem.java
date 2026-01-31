import java.sql.Time;
import java.util.ArrayList;
import java.util.Set;

import static java.lang.Math.ceil;
import static java.lang.Math.sqrt;

/***
 * @author Sam Touma 101033509
 */
public class DroneSubsystem implements Runnable {
    private Boolean ON_JOB = false;
    Scheduler scheduler;
    IncidentMessage currentJobdetails;


    private final double SIZE_OF_TANK = 12;
    double requiredLiquid;

    double timeTaken = 0;
    double distance;
    double numReturnTrips;

    long timeToEmptyTank = (long) 2.4;
    long openCloseNozzle = 1;
    double speed = 25;


    Box sendBox, recieveBox;

    public DroneSubsystem(Scheduler Scheduler, Box sendBox, Box recieveBox) {
        this.scheduler = Scheduler;
        this.sendBox = sendBox;
        this.recieveBox = recieveBox;
    }

    /***
     * Checks for AVAILABLE work
     */
    @Override
    public void run() {
        while(true){
            //Look for Job
            jobDetails();
            //Do the job
            if (currentJobdetails != null) {
                ON_JOB = true;
            } else break;
            extinguishFire();
        }
    }

    private void extinguishFire() {
        //How much water is needed
        if (currentJobdetails.getSeverity() == Incident.Severity.LOW) {
            requiredLiquid = 10; // Water needed for low
            numReturnTrips = ceil(requiredLiquid/SIZE_OF_TANK);
        } else if (currentJobdetails.getSeverity() == Incident.Severity.MODERATE) {
            requiredLiquid = 20; // Water needed for moderate
            numReturnTrips = ceil(requiredLiquid/SIZE_OF_TANK);
        } else if (currentJobdetails.getSeverity() == Incident.Severity.HIGH) {
            requiredLiquid = 30; // Water needed for high
            numReturnTrips = ceil(requiredLiquid/SIZE_OF_TANK);
        }
        droneCalculations();
        notifyJobCompletion();
    }

    public void jobDetails() {
        System.out.println("Drone Requests Job");
        currentJobdetails = (IncidentMessage) recieveBox.get();
    }

    public void notifyJobCompletion() {
        //Packet sent back to scheduler
        sendBox.put(true);
    }

    public void droneCalculations() {
        //Distance formula
        distance = sqrt((currentJobdetails.getEndX())^2 + (currentJobdetails.getEndY())^2);
        //Time needed for whole trip (back and forth) with open/close nozzle and tank emptying.
        timeTaken = 2*(distance/speed)*numReturnTrips + openCloseNozzle + timeToEmptyTank*(requiredLiquid/SIZE_OF_TANK);
        droneReport();
    }

    public void droneReport() {
        //Notify the scheduler that the job is done
        System.out.println("Job severity: " + currentJobdetails.getSeverity());
        System.out.println("Job Completion Time: " + timeTaken);
        ON_JOB = false;
    }
}

import java.awt.*;
import java.sql.Time;

public class Main {
    public static void main(String[] args) {
        /*
        Iteration 1: Fire system notifies Scheduler, Scheduler notifies Drones
        How it works. Scheduler stores available work for the Drones.
        The Drone is actively looking for work when there is work available.
         */

        Box fireToSchedulerBox = new Box();
        Box schedulerToFireBox = new Box();
        Box schedulerToDroneBox = new Box();
        Box droneToSchedulerBox = new Box();

        Scheduler scheduler = new Scheduler(schedulerToFireBox, schedulerToDroneBox, fireToSchedulerBox, droneToSchedulerBox);
        FireIncidentSubsystem fireSystem = new FireIncidentSubsystem(scheduler, "Firefighting-Drone-Swarm-Group1/input/sample_zone_file.csv","Firefighting-Drone-Swarm-Group1/input/Sample_event_file.csv", fireToSchedulerBox, schedulerToFireBox);
        DroneSubsystem drone_specifications = new DroneSubsystem(scheduler, droneToSchedulerBox, schedulerToDroneBox);
        Thread drone1 = new Thread(drone_specifications);
        Thread fire_system1 = new Thread(fireSystem);
        Thread schedulerThread = new Thread(scheduler);
        schedulerThread.start();
        drone1.start();
        fire_system1.start();
    }

}

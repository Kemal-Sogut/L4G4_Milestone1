
/***
 * The Scheduler class is responsible for data exchange between
 * FireIncidentSubSystem and DroneSubsystem
 * @author Sam Touma
 */
public class Scheduler implements Runnable {

    private Box fireIncidentSendBox;
    private Box fireIncidentRecieveBox;
    private Box droneSendBox;
    private Box droneRecieveBox;



    /***
     * Constructor
     */
    public Scheduler(Box fireIncidentSendBox, Box droneSendBox, Box fireIncidentRecieveBox, Box droneRecieveBox) {
        this.fireIncidentSendBox = fireIncidentSendBox;
        this.droneSendBox = droneSendBox;
        this.fireIncidentRecieveBox = fireIncidentRecieveBox;
        this.droneRecieveBox = droneRecieveBox;
    }

    @Override
    public void run() {
        while (true) {
            Object iMessage = fireIncidentRecieveBox.get();
            if (iMessage instanceof IncidentMessage){
                droneSendBox.put((IncidentMessage)iMessage);
                droneRecieveBox.get();
                fireIncidentSendBox.put(true);
            }
            else{
                droneSendBox.put(null);
                break;
            }
        }
    }
}


/**
 * Box that holds object
 * @author Sam Touma 101033509
 * @version 2025-01-23
 */
public class Box {
    // Object held
    private Object o;
    //Empty or not
    private boolean empty = true;

    /**
     * Returns the object if the box is not empty then
     * sets the box to empty
     * @return Held Object
     */
    public synchronized Object get() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        empty = true;
        notify();
        return o;
    }

    /**
     * Sets the box to the object given.
     * @param o The object for the box to take
     */
    public synchronized void put(Object o) {
        while (!empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        this.o = o;
        empty = false;
        notifyAll();
    }

    /**
     * Returns the object if the box is not empty,
     * does not remove the object from the box unlike get()
     * @return Held Object
     */
    public synchronized Object peek() {
        while (empty) {
            try {
                wait();
            } catch (InterruptedException e) {}
        }
        return o;
    }
}
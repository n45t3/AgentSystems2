package prison.prog;

public class ProgEntry {

    public static void main(String[] args) {
        PrisonManager pm = new PrisonManager();
        pm.start();
        try {
            pm.join();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

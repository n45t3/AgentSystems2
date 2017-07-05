package prison.prog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.json.JSONException;
import org.json.JSONObject;



public class Server {

    private ServerSocket  ss       = null;
    private Socket        client   = null;
    private Runnable      worker   = null;
    private static Thread workerT  = null;
    private static Server instance = new Server();
    private PrisonManager pm       = null;

    private Server() {
    }

    public static boolean register(PrisonManager pm) {
        if (instance.pm != null) return false;
        instance.pm = pm;
        return true;
    }

    public static boolean init(int port) {
        try {
            instance.ss = new ServerSocket(port);
            // instance.ss.setSoTimeout(3000);
            instance.worker = new Runnable() {

                @Override
                public void run() {
                    try {
                        instance.client = instance.ss.accept();
                        synchronized (instance.pm.lock) {
                            instance.pm.STOPPED = false;
                        }
                        JSONObject json = new JSONObject();
                        Thread.sleep(5000);
                        json.put("type", "no_elo");
                        json.put("content", "bartek pizda XD");
                        if (!send(json)) return;
                        while (true) {
                            synchronized (instance.pm.lock) {
                                if (!instance.pm.RUNNING) break;
                            }
                            Thread.sleep(5000);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            workerT = new Thread(instance.worker);
            workerT.start();

            // instance.worker = new Runnable() {
            //
            // @Override
            // public void run() {
            // try (StringWriter sw = new StringWriter();
            // BufferedReader instr = new BufferedReader(
            // new InputStreamReader(instance.client.getInputStream())
            // )) {
            // while (true) {
            // synchronized (instance) {
            // if (instance.ss.isClosed()) break;
            // if (instance.client.isClosed()) break;
            // }
            //
            // }
            // } catch (Exception e) {
            // e.printStackTrace();
            // return;
            // }
            // }
            //
            // };
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean send(JSONObject json) {
        if (instance.ss == null || instance.client == null) return false;
        try (OutputStreamWriter out = new OutputStreamWriter(
                instance.client.getOutputStream(), StandardCharsets.UTF_8
        )) {
            out.write(json.toString());
            out.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void join() throws InterruptedException {
        workerT.join();
    }
}

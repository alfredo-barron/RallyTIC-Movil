package alfredobarron.com.rallytic.resources;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by alfredobarron on 29/07/15.
 */
public class Tiempo {

    private Timer timer = new Timer();
    private int segundos;

    class Contador extends TimerTask {
        public void run() {
            segundos++;
            System.out.println("segundo" + segundos);
        }
    }

    public void Contar() {
        this.segundos = 0;
        timer = new Timer();
        timer.schedule(new Contador(), 0, 1000);
    }

    public void Detener() {
        timer.cancel();
    }

    public int getSegundos() {
        return this.segundos;
    }

}

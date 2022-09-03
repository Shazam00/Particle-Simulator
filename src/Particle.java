import java.awt.Color;
import java.util.Random;

public class Particle {
    Color colour;
    int SCREEN_X = 2500;
    int SCREEN_Y = 1400;
    double pos_x, pos_y;
    double vel_x, vel_y;
    Random rand = new Random();

    /**
     * Create a particle with a known Color and position
     * @param c - Color obj
     * @param x - x-position
     * @param y - y-position
     */
    public Particle(Color c, int x, int y){
        colour = c;
        pos_x = x; 
        pos_y = y; 
        vel_x = 0;
        vel_y = 0;
    }
    
    /**
     * Create a particle with a random position
     * @param c - Color obj
     */
    public Particle(Color c){
        colour = c;
        pos_x = rand.nextInt((SCREEN_X) + 1); 
        pos_y = rand.nextInt((SCREEN_Y) + 1); 
        vel_x = 0;
        vel_y = 0;
    }

    /**
     * increment particle position
     */
    public void Update(){
        pos_x +=vel_x;
        pos_y +=vel_y;

        if (pos_x <=0) {
            pos_x = 2;
            vel_x*=-1;
        } else if (pos_x >= SCREEN_X ){
            pos_x = SCREEN_X-2;
            vel_x*=-1;
        }
        if (pos_y <=0) {
            pos_y = 1;
            vel_y*=-1;
        } else if (pos_y >= SCREEN_Y-50 ){
            pos_y = SCREEN_Y-51;
            vel_y*=-1;
        }
    }
}
import javax.swing.JPanel;
import java.awt.Graphics;
import java.util.LinkedHashMap;
import java.util.Set;
import java.awt.Color;

public class Simulation extends JPanel {
    int NUM_OF_PARTICLES;
    int NUM_PARTICLE_TYPES;
    int GRAVITY_RANGE;
    int MAX_VELOCITY;

    String[] colourOrder;
    double[][] Attractions;

    int SCREEN_X;
    int SCREEN_Y;

    Particle[][] Particles;
    LinkedHashMap<String,Color> particlesMap;

    /**
     * @param x Screen Width
     * @param y Screen Height
     * @param p Particle Map of colors, Color name , Color obj
     * @param n number of particle per color
     * @param g range of interaction/gravity
     * @param mv max velocity
     */
    public Simulation(int x, int y, LinkedHashMap<String,Color> p, int n, int g, int mv){
        // initialize all settings
        SCREEN_X = x;
        SCREEN_Y = y;
        NUM_PARTICLE_TYPES = p.size();
        NUM_OF_PARTICLES = n;
        GRAVITY_RANGE = g;
        MAX_VELOCITY = mv;
        particlesMap = p;
        colourOrder = new String[NUM_PARTICLE_TYPES];
        Particles = new Particle[NUM_PARTICLE_TYPES][NUM_OF_PARTICLES];
        Attractions = new double[NUM_PARTICLE_TYPES][NUM_PARTICLE_TYPES];

        // create all particle types
        Set<String> keys = p.keySet();
        int k = 0;
        for (String key:keys){
            colourOrder[k] = key;
            k++;
        }

        // create all particles
        for (int i=0;i<NUM_PARTICLE_TYPES;i++){
            for (int j=0;j<NUM_OF_PARTICLES;j++){
                Particles[i][j] = new Particle(p.get(colourOrder[i]));
            }
        }

        // starts a thread for each type of particle
        Thread[] ParticleThreads = new Thread[NUM_PARTICLE_TYPES];
        for (int i=0;i<NUM_PARTICLE_TYPES;i++ ){
            final int index = i;
            ParticleThreads[i] = new Thread(new Runnable() {
                public void run() {
                    while (true){
                        for (int j=0; j<NUM_PARTICLE_TYPES;j++){
                            calculateAttraction(Particles[index],Particles[j],Attractions[index][j]);
                        }
                        try {
                            Thread.sleep(16);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            ParticleThreads[i].start();
        }
    }

    /**
     * Randomize position of all particles
     */
    public void randomizeParticles(){
        for (int i=0;i<NUM_PARTICLE_TYPES;i++){
            for (int j=0;j<NUM_OF_PARTICLES;j++){
                Particles[i][j] = new Particle(particlesMap.get(colourOrder[i]));
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, SCREEN_X, SCREEN_Y);

        // Updates next position of particles and draws them on screen
        for (int i=0;i<NUM_PARTICLE_TYPES;i++){
            for (int j=0;j<NUM_OF_PARTICLES;j++){
                Particles[i][j].Update();
                g.setColor(Particles[i][j].colour);
                g.fillRect((int)Particles[i][j].pos_x, (int)Particles[i][j].pos_y, 1, 1);
            }
        }
        // draws labels on scren for each interaction
        for (int i=0;i<NUM_PARTICLE_TYPES;i++){
            for (int j=0;j<NUM_PARTICLE_TYPES;j++){
                g.setColor(particlesMap.get(colourOrder[i]));
                g.drawString(colourOrder[i]+" X "+colourOrder[j]+"    : "+String.format("%.3f",Attractions[i][j]),150,115+30*j+i*200);
            }
        }
    }

    /**
     * Calculates and set the new velocity of Particle[] A when interacting with Particle[] B 
     * @param a - an array of Particles A
     * @param b - an array of Particles B
     * @param gravity - strength of attraction/gravity
     */
    public void calculateAttraction(Particle a[], Particle b[], double gravity){
        double fx, fy;
        double dx, dy;
        double d, F;
        double g=gravity;
        for (int i=0;i<a.length;i++){
            fx = 0;
            fy = 0;
            for (int j=0;j<b.length;j++){
                dx = a[i].pos_x - b[j].pos_x; 
                dy = a[i].pos_y - b[j].pos_y; 
                d = Math.sqrt(dx*dx+dy*dy);
                if ((d>0) && (d < GRAVITY_RANGE)){
                    F = g * (1/d);   
                    fx += (F*dx);
                    fy += (F*dy);
                }
            }
            // cap velocity
            if (a[i].vel_x <= MAX_VELOCITY*-1){
                 a[i].vel_x = MAX_VELOCITY*-1;
            } else if (a[i].vel_x > MAX_VELOCITY){
                a[i].vel_x = MAX_VELOCITY;
            }
            if (a[i].vel_y <= MAX_VELOCITY*-1){
                a[i].vel_y = MAX_VELOCITY*-1;
            } else if (a[i].vel_y > MAX_VELOCITY){
                a[i].vel_y = MAX_VELOCITY;
            }

            // update velocity
            a[i].vel_x = (a[i].vel_x + fx)*0.25;
            a[i].vel_y = (a[i].vel_y + fy)*0.25;
        }
    }
}
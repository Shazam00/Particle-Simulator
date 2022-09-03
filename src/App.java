import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import java.util.LinkedHashMap;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.Timer;

import java.awt.Color;
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Program Start.");
        JFrame f = new JFrame();
        JButton LocationButton = new JButton("Randomize Locations");
        JButton AttractionButton = new JButton("Randomize Attraction");
        JButton ZeroButton = new JButton("Reset to Zero");
        Random rand = new Random();
        LinkedHashMap<String,Color> particles =new LinkedHashMap<String,Color>();
        
        // Simulation settings
        int SCREEN_X = 2500;
        int SCREEN_Y = 1400;
        int numParticles = 1500;
        int GRAVITY_RANGE = 100;
        int MAX_VELOCITY = 10;
        particles.put("Red", Color.RED);
        particles.put("Green", Color.GREEN);
        particles.put("Blue", Color.BLUE);
        particles.put("Yellow", Color.YELLOW);
        particles.put("White", Color.WHITE);
        particles.put("Pink", Color.PINK);

        // create simulation object with specified settings
        Simulation s = new Simulation(SCREEN_X,SCREEN_Y, particles, numParticles, GRAVITY_RANGE, MAX_VELOCITY);

        // create all scroll bars with listeners for each particle types
        JScrollBar[][] ScrollBars = new JScrollBar[s.NUM_PARTICLE_TYPES][s.NUM_PARTICLE_TYPES];
        AdjustmentListener[] adjustmentListeners = new AdjustmentListener[s.NUM_PARTICLE_TYPES];
        for (int i=0; i<s.NUM_PARTICLE_TYPES;i++){
            final int index = i;
            adjustmentListeners[index] = new AdjustmentListener(){
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    for (int j=0; j<s.NUM_PARTICLE_TYPES;j++){
                        if (ScrollBars[index][j] == e.getSource() ){
                            s.Attractions[j][index] = (e.getValue()*0.01)-5;
                        }
                    }
                }
            };
        }
        for (int i=0; i<s.NUM_PARTICLE_TYPES;i++){
            for (int j=0; j<s.NUM_PARTICLE_TYPES;j++){
                ScrollBars[i][j] = new JScrollBar(JScrollBar.HORIZONTAL,500,1,1, 1000);
                ScrollBars[i][j].setBounds(20,100+30*i+200*j,100,20);
                ScrollBars[i][j].addAdjustmentListener(adjustmentListeners[i]);
                f.add(ScrollBars[i][j]);
            }
        }
        
        // initialize random attractions for each particle interaction
        for (int i=0;i<s.NUM_PARTICLE_TYPES;i++){
            for (int j=0;j<s.NUM_PARTICLE_TYPES;j++){
                ScrollBars[i][j].setValue(rand.nextInt(400) +300); 
            }
        }

        LocationButton.setBounds(20,50,160,30);
        AttractionButton.setBounds(180,50,160,30);
        ZeroButton.setBounds(340, 50, 160, 30);

        // randomize positions of all particles
        LocationButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                s.randomizeParticles();
            }
        }); 

        // randomize attraction rules for all particle types
        AttractionButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //
                for (int i=0;i<s.NUM_PARTICLE_TYPES;i++){
                    for (int j=0;j<s.NUM_PARTICLE_TYPES;j++){
                        ScrollBars[i][j].setValue(rand.nextInt(400) +300); 
                    }
                }

            }
        }); 

        // set all particle interactions to zero
        ZeroButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                for (int i=0;i<s.NUM_PARTICLE_TYPES;i++){
                    for (int j=0;j<s.NUM_PARTICLE_TYPES;j++){
                        ScrollBars[i][j].setValue(500); 
                    }
                }
            }
        }); 

        f.add(LocationButton);
        f.add(AttractionButton);
        f.add(ZeroButton);
        f.setSize(SCREEN_X,SCREEN_Y);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(s, BorderLayout.CENTER);
        f.setVisible(true);
        f.setTitle("Particle Simulation");
        final Timer timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                f.repaint();
            }
        });
        timer.start();
    }
}
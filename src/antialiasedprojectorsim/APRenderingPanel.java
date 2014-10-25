/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package antialiasedprojectorsim;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author MEwer
 */
public class APRenderingPanel extends javax.swing.JPanel {
    private Random r;
    private InputImage input;
    private ProjectorOffsets actualPOffsets;
    private ProjectorOffsets simPOffsets;
    
    
    /**
     * Creates new form APRenderingPanel
     */
    public APRenderingPanel() {
        r = new Random();
        try {
            input = new InputImage.RasterInputImage(ImageIO.read(new File("ss.png")));
        } catch (IOException ex) {
            Logger.getLogger(APRenderingPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        //actualPOffsets = new ProjectorOffsets(new double[]{r.nextDouble(), r.nextDouble()});
        double amplitudeFactor = 6;
        double freqFac = 9;
        actualPOffsets = new ProjectorOffsets(new double[]{amplitudeFactor, amplitudeFactor}, new double[]{freqFac, 1.1 * freqFac}, new double[]{0, 0.2}, new int[]{ProjectorOffsets.WAVE_SINE, ProjectorOffsets.WAVE_SINE}, System.currentTimeMillis());
        simPOffsets = new ProjectorOffsets(new double[]{amplitudeFactor, amplitudeFactor}, new double[]{freqFac, 1.1 * freqFac}, new double[]{0, 0.2}, new int[]{ProjectorOffsets.WAVE_SINE, ProjectorOffsets.WAVE_SINE}, System.currentTimeMillis());
        initComponents();
        
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                APRenderingPanel.this.repaint();
            }
        }, 0, 1);
    }

    public boolean paused = false;
    
    @Override
    protected void paintComponent(Graphics g2) {
        super.paintComponent(g2);
        Graphics2D g = (Graphics2D)g2;
        
//        double[] defTL = new double[] {r.nextInt(10) - 5, r.nextInt(10) - 5};
//        double[] defTL = new double[] {0, 0};
//        defTL[0] *= -1;
//        defTL[1] *= -1;
//        double[] defTL = actualPOffsets.getOffsets(System.currentTimeMillis());
//        double[] displayOffset = new double[] {50, 50};
        //double[] displayOffset = new double[] {50 - defTL[0], 50 - defTL[1]};
        double[] defTL = simPOffsets.getOffsets(System.currentTimeMillis());
        double[] defArea = new double[] {100, 100};
        double[] displayOffset = actualPOffsets.getOffsets(System.currentTimeMillis());
        if (paused) {
            defTL = new double[] {0, 0};
            displayOffset = new double[] {0, 0};
        }
        defTL[0] += 100;
        defTL[1] += 200;
        displayOffset[0] += 50;
        displayOffset[1] += 50;
        
        long start = System.currentTimeMillis();
        double[] offset = new double[] {0, 0};
        double[] size = new double[] {3, 3};
        for (double y = 0; y < defArea[1]; y += size[1]) {
            for (double x = 0; x < defArea[0]; x += size[0]) {
                offset[0] = defTL[0] + x;
                offset[1] = defTL[1] + y;
                g.setColor(input.getRegionColor(offset, size));
                g.fillRect((int)(x + displayOffset[0]), (int)(y + displayOffset[1]),
                        (int)(x + displayOffset[0] + size[0]) - (int)(x + displayOffset[0]),
                        (int)(y + displayOffset[1] + size[1]) - (int)(y + displayOffset[1]));
            }
        }
        long end = System.currentTimeMillis();
        
        System.out.println("time: " + (end - start));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package antialiasedprojectorsim;

/**
 * Models the motion of the projector/projection.
 * Ideally this would have some concept of orientation and/or rotation, but
 * that's overkill for now.  Note that I've tacitly made this n-dimensional, too.
 * @author MEwer
 */
public class ProjectorOffsets {
    public static final int WAVE_SINE = 1;
    
    public double[] amplitude;
    public double[] frequency; // In cycles per second.
    public double[] phase; // In cycles.
    public int[] waveform;
    
    public long startTime;
    
    public double[] getOffsets(long time) {
        double[] result = new double[amplitude.length];
        for (int i = 0; i < result.length; i++) {
            switch (waveform[i]) {
                case WAVE_SINE:
                    result[i] = amplitude[i] * Math.sin(frequency[i] * (((time - startTime) / 1000.0) + phase[i]) * 2 * Math.PI);
                    break;
            }
        }
        return result;
    }
    
    public ProjectorOffsets(double[] frequency) {
        this(defaultAmplitude(frequency.length),
             frequency,
             defaultPhase(frequency.length),
             defaultWaveform(frequency.length),
             0);
    }
    
    public static double[] defaultAmplitude(int dims) {
        double[] amplitude = new double[dims];
        for (int i = 0; i < dims; i++) {
            amplitude[i] = 1;
        }
        return amplitude;
    }

    public static double[] defaultPhase(int dims) {
        double[] phase = new double[dims];
        for (int i = 0; i < dims; i++) {
            phase[i] = 0;
        }
        return phase;
    }

    public static int[] defaultWaveform(int dims) {
        int[] waveform = new int[dims];
        for (int i = 0; i < dims; i++) {
            waveform[i] = WAVE_SINE;
        }
        return waveform;
    }
    
    public ProjectorOffsets(double[] amplitude, double[] frequency, double[] phase, int[] waveform, long startTime) {
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.phase = phase;
        this.waveform = waveform;
        this.startTime = startTime;
    }
}

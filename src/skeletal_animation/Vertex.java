package skeletal_animation;

import math.Vec4;

/**
 *
 * @author leonardo
 */
public class Vertex {

    public final Vec4 position = new Vec4();
    public final Vec4 positionTransf = new Vec4();
    public static final Vec4[] positionsTmp = { new Vec4(), new Vec4() };
    public Bone[] bones = new Bone[2];
    public double[] weights = new double[2];

    public Vertex(double px, double py, Bone b0, Bone b1, double w0, double w1) {
        position.set(px, py, 0, 1);
        bones[0] = b0;
        bones[1] = b1;
        weights[0] = w0;
        weights[1] = w1;
    }
    
    public void calculate() {
        positionsTmp[0].set(position);
        bones[0].getInverseTransform().multiply(positionsTmp[0]);
        
        positionsTmp[1].set(position);
        bones[1].getInverseTransform().multiply(positionsTmp[1]);
        
        bones[0].getTransform().multiply(positionsTmp[0]);
        bones[1].getTransform().multiply(positionsTmp[1]);
        positionsTmp[0].multiply(weights[0]);
        positionsTmp[1].multiply(weights[1]);
        positionTransf.set(positionsTmp[0]);
        positionTransf.add(positionsTmp[1]);
    }
    
}

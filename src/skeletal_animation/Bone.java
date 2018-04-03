package skeletal_animation;

import java.util.ArrayList;
import java.util.List;
import math.Mat4;
import math.Vec4;

/**
 *
 * @author leonardo
 */
public class Bone {

    public static enum Type { ROOT, JOINT, END };
    private Bone parent;
    private Type type;
    private String name = "";
    private final Vec4 position = new Vec4();
    private final Vec4 offset = new Vec4();
    private final Mat4 transform = new Mat4();
    private final Mat4 transformTmp = new Mat4();
    private final Mat4 inverseTransform = new Mat4();
    private final List<Bone> children = new ArrayList<Bone>();

    public Bone(Type type) {
        this.type = type;
        transform.setIdentity();
    }

    public Bone getParent() {
        return parent;
    }

    public void setParent(Bone parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vec4 getPosition() {
        return position;
    }

    public Vec4 getOffset() {
        return offset;
    }

    public Mat4 getTransform() {
        return transform;
    }

    public Mat4 getInverseTransform() {
        return inverseTransform;
    }

    public List<Bone> getChildren() {
        return children;
    }

    private static final int[] POSE_INDEX = { 0 };
    public void setPose(double[] data) {
        POSE_INDEX[0] = 0;
        setPose(data, POSE_INDEX);
    }
    
    private void setPose(double[] data, int[] index) {
        if (type == Type.ROOT) {
            transform.setTranslation(offset);
        }
        else {
            transform.set(parent.getTransform());
            transformTmp.setTranslation(offset);
            transform.multiply(transformTmp);
        }
        
        if (data != null) {
            transformTmp.setRotationZ(data[index[0]++]);
            transform.multiply(transformTmp);
        }
        
        position.set(0, 0, 0, 1);
        transform.multiply(position);
        
        for (Bone child : children) {
            child.setPose(data, index);
        }
    }
    
    public void fillBonesList(List<Bone> bones) {
        if (type == Type.ROOT) {
            bones.clear();
        }
        bones.add(this);
        for (Bone child : children) {
            child.fillBonesList(bones);
        }
    }
    
    public void calculateInverseTransformation() {
        if (type == Type.ROOT) {
            setPose(null);
        }
        inverseTransform.set(transform);
        inverseTransform.invert();
        for (Bone child : children) {
            child.calculateInverseTransformation();
        }
    }
    
}

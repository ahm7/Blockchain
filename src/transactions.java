import java.util.ArrayList;

public class transactions {
    private  String hash;
    private  boolean flag;
    private int inputCounter;
    private ArrayList<input> inputs;
    private int outputCounter;
    private ArrayList<input> outputs;
    private String signature;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getInputCounter() {
        return inputCounter;
    }

    public void setInputCounter(int inputCounter) {
        this.inputCounter = inputCounter;
    }

    public ArrayList<input> getInputs() {
        return inputs;
    }

    public void setInputs(ArrayList<input> inputs) {
        this.inputs = inputs;
    }

    public int getOutputCounter() {
        return outputCounter;
    }

    public void setOutputCounter(int outputCounter) {
        this.outputCounter = outputCounter;
    }

    public ArrayList<input> getOutputs() {
        return outputs;
    }

    public void setOutputs(ArrayList<input> outputs) {
        this.outputs = outputs;
    }
}

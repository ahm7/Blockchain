package Parsing;

import Parsing.InputsFromText;
import Parsing.OutputsFromText;

import java.util.ArrayList;

public class TransactionFromText {
    private InputsFromText inputs;
    private ArrayList<OutputsFromText> outputs;

    public TransactionFromText() {

    }

    public InputsFromText getInputs() {
        return inputs;
    }

    public void setInputs(InputsFromText inputs) {
        this.inputs = inputs;
    }

    public ArrayList<OutputsFromText> getOutputs() {
        return outputs;
    }

    public void setOutputs(ArrayList<OutputsFromText> outputs) {
        this.outputs = outputs;
    }
}

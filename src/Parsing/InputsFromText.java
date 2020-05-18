package Parsing;

public class InputsFromText {
    private int transactionNumber,input,previousTX = -1,outputIndex = -1;

    public InputsFromText() {

    }

    public int getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(int transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public int getInput() {
        return input;
    }

    public void setInput(int input) {
        this.input = input;
    }

    public int getPreviousTX() {
        return previousTX;
    }

    public void setPreviousTX(int previousTX) {
        this.previousTX = previousTX;
    }

    public int getOutputIndex() {
        return outputIndex;
    }

    public void setOutputIndex(int outputIndex) {
        this.outputIndex = outputIndex;
    }
}

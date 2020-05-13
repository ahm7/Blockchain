import java.security.PrivateKey;
import java.security.PublicKey;

public class output {
    private double value;
    private int index;
    private PublicKey publicKey;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
    // value
    //outputindex
    // public key of payee
}

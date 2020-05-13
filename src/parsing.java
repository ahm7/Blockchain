import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList;

public class parsing {

    public ArrayList<TransactionFromText> readDataset(){
        try {
            ArrayList<TransactionFromText> transactions = new ArrayList<>();
            File myObj = new File("txdataset_v2.txt");
            Scanner myReader = new Scanner(myObj);
            int printIndex = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parsedTransaction = parseString(data,"\\s+");
                if(printIndex == 0){
                    printIndex++;
                    continue;
                }
                InputsFromText inputs = new InputsFromText();
                ArrayList<OutputsFromText> outputs = new ArrayList<>();
                for(int i = 0;i < parsedTransaction.length;i++){
                    if(i == 0){
                        inputs.setTransactionNumber(Integer.parseInt(parsedTransaction[i]));
                    }
                    else{
                        String[] parsedAttribute = parseString(parsedTransaction[i], ":");
                        if(parsedAttribute[0].equals("intput")){
                            inputs.setInput(Integer.parseInt(parsedAttribute[1]));
                        }
                        else if(parsedAttribute[0].equals("previoustx")){
                            inputs.setPreviousTX(Integer.parseInt(parsedAttribute[1]));
                        }
                        else if(parsedAttribute[0].equals("outputindex")){
                            inputs.setOutputIndex(Integer.parseInt(parsedAttribute[1]));
                        }
                        else if(parsedAttribute[0].contains("value")){
                            OutputsFromText output = new OutputsFromText();
                            output.setValue(Double.parseDouble(parsedAttribute[1]));
                            i++;
                            parsedAttribute = parseString(parsedTransaction[i], ":");
                            output.setOutput(Integer.parseInt(parsedAttribute[1]));
                            outputs.add(output);
                        }
                    }
                }
                if (printIndex != 0){
                    TransactionFromText transaction = new TransactionFromText();
                    transaction.setInputs(inputs);
                    transaction.setOutputs(outputs);
                    transactions.add(transaction);
                }
                printIndex++;
            }
            myReader.close();
            return transactions;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return null;
    }

    public String[] parseString(String stringToParse, String regex){
        String[] parsedTransaction = stringToParse.split(regex);
        return parsedTransaction;
    }
}

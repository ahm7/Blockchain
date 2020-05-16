import java.io.*;
import java.util.Scanner; // Import the Scanner class to read text files
import java.util.ArrayList;

public class parsing {


    public ArrayList<TransactionFromText> readDataset(){
        try {
            ArrayList<TransactionFromText> transactions = new ArrayList<>();
            File myObj = new File("testFiles/testAll.txt");
            Scanner myReader = new Scanner(myObj);
            int printIndex = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] parsedTransaction = parseString(data,"\\s+");
                if(printIndex == 0 || data.equals("41226")){
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

    public ArrayList<NodePeers> readNodePeers(int nodeNumber) throws FileNotFoundException {
        String path;
        path = "NodesPeers/Node" + nodeNumber + "Peers.txt";
        File myObj = new File(path);
        Scanner myReader = new Scanner(myObj);
        ArrayList<NodePeers> nodePeers = new ArrayList<>();
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] parsedPeer = parseString(data, "\\s+");
            NodePeers nodePeer = new NodePeers();
            nodePeer.setNodeTo(Integer.parseInt(parsedPeer[0]));
            nodePeer.setIP(parsedPeer[1]);
            nodePeer.setPort(Integer.parseInt(parsedPeer[2]));
            nodePeers.add(nodePeer);
        }
        myReader.close();
        return nodePeers;
    }

    public NodePeers readPort(int nodeNumber) throws FileNotFoundException {
        File myObj = new File("NodesPeers/NodesIPsAndPorts.txt");
        Scanner myReader = new Scanner(myObj);
        NodePeers nodePort = new NodePeers();
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] parsedNode = parseString(data, "\\s+");
            if(Integer.parseInt(parsedNode[0]) == nodeNumber){
                nodePort.setNodeTo(Integer.parseInt(parsedNode[0]));
                nodePort.setIP(parsedNode[1]);
                nodePort.setPort(Integer.parseInt(parsedNode[2]));
                break;
            }
            else{
                continue;
            }
        }
        myReader.close();
        return nodePort;
    }

    public void writeNodeTransactions() throws IOException {
        File myObj = new File("testFiles/txdataset_v2.txt");
        Scanner myReader = new Scanner(myObj);
        int printIndex = 0;
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            if(printIndex != 0 && !data.equals("41226")){
                System.out.println(data);
                String[] parsedTransaction = parseString(data,"\\s+");
                String[] parsedInput = parseString(parsedTransaction[1], ":");
                int nodeNumber = Integer.parseInt(parsedInput[1]);
                String path = "";
                path = "testFiles/Node" + nodeNumber + "Transactions.txt";
                Writer output;
                output = new BufferedWriter(new FileWriter(path, true));
                String line = data + "\n";
                output.append(line);
                output.close();
            }
            printIndex++;
        }
        myReader.close();
    }

    public String[] parseString(String stringToParse, String regex){
        String[] parsedTransaction = stringToParse.split(regex);
        return parsedTransaction;
    }

    public void createFile(int nodeNumber) throws IOException {
        String path;
        path = "TestFiles\\Node" + nodeNumber + "Transactions.txt";
        File myObj = new File(path);
        myObj.createNewFile();
    }

}

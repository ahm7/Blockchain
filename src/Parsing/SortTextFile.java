package Parsing;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class SortTextFile {

    public void sortTransactionsFile(int nodeNumber) throws IOException {
        BufferedReader reader;
        String path = "TestFiles\\POWVariant.Node" + nodeNumber + "Transactions.txt";
        reader = new BufferedReader(new FileReader(path));
        ArrayList<NodeTransaction> nodeTransactions = new ArrayList<NodeTransaction>();
        String currentLine = reader.readLine();
        while (currentLine != null) {
            String[] parsedNodeTransaction = currentLine.split("\\s+");
            String[] parsedPreviousTx = parsedNodeTransaction[2].split(":");
            nodeTransactions.add(new NodeTransaction(currentLine, Integer.parseInt(parsedPreviousTx[1])));
            currentLine = reader.readLine();
        }
        Collections.sort(nodeTransactions, new previousTxCompare());
        path = "TestFiles\\POWVariant.Node" + nodeNumber + "SortedTransactions.txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        for (NodeTransaction node : nodeTransactions) {
            writer.write(node.nodeTransaction);
            writer.newLine();
        }
        reader.close();
        writer.close();
    }
}

class NodeTransaction {
    String nodeTransaction;
    int previousTx;

    public NodeTransaction(String nodeTransaction, int previousTx) {
        this.nodeTransaction = nodeTransaction;
        this.previousTx = previousTx;
    }
}

//previousTx Class to compare the names

class previousTxCompare implements Comparator<NodeTransaction>
{
    @Override
    public int compare(NodeTransaction s1, NodeTransaction s2)
    {
        return s1.previousTx - s2.previousTx;
    }
}



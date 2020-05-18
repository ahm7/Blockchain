package Entities;

import java.io.Serializable;

public class Vote   implements Serializable {

    private boolean nodeVote;

    public Vote(boolean nodeVote)    {
        this.nodeVote = nodeVote;
    }

    public boolean getNodeVote() {
        return nodeVote;
    }

    public void setNodeVote(boolean nodeVote) {
        this.nodeVote = nodeVote;
    }


}

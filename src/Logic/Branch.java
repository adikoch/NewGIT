package Logic;

import java.util.HashSet;

public class Branch {
    Boolean isTracking;
    Boolean isRemote;

   private String branchName;
    private Boolean isMaster;
    private Commit pointedCommit;

    public Branch(String name)
    {
        branchName = name;
    }

    public String getBranchName()
    {
        return branchName;
    }

    public Commit getPointedCommit()
    {
        return pointedCommit;
    }

    public void setPointedCommit(Commit newC)
    {
        pointedCommit=newC;
    }

    //  tracking true
//    String branchName;
//    Integer pointedCommit;
//    String trackingAfter;

    //is remote true
//    String branchName;
//    Integer pointedCommit;

}

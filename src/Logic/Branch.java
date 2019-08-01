package Logic;

import java.util.HashSet;

public class Branch {
    Boolean isTracking;
    Boolean isRemote;

   private String branchName;
    private Boolean isMaster;
    Commit pointedCommit;

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

    //  tracking true
//    String branchName;
//    Integer pointedCommit;
//    String trackingAfter;

    //is remote true
//    String branchName;
//    Integer pointedCommit;

}

package Logic;

import java.util.HashSet;

public class Branch {
    Boolean isTracking;
    Boolean isRemote;

   private String branchName;
    private Commit pointedCommit;
    private  String pointedCommitSHA1;

    void setPointedCommitSHA1(String pointedCommit) {
        pointedCommitSHA1 = pointedCommit;
    }

    Branch(String name)
    {
        branchName = name;
    }
    Branch(String name, String commitSHA1)
    {
        branchName = name;
        pointedCommitSHA1 = commitSHA1;
    }

    public String getBranchName()
    {
        return branchName;
    }

    public Commit getPointedCommit()
    {
        return pointedCommit;
    }

    void setPointedCommit(Commit newC)
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

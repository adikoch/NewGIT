package Logic;

import jaxb.schema.generated.MagitBranches;

import java.util.Map;

public class Branch {

    //members
    private String branchName;
    private Commit pointedCommit;
    private  String pointedCommitSHA1="";

    Boolean isTracking;
    Boolean isRemote;

    //con
    Branch(String name)
    {
        branchName = name;
    }
    Branch(String name, String commitSHA1)
    {
        branchName = name;
        pointedCommitSHA1 = commitSHA1;
    }


    //set\get

    public String getPointedCommitSHA1() { return pointedCommitSHA1; }
    void setPointedCommitSHA1(String pointedCommit) { pointedCommitSHA1 = pointedCommit; }

    public String getBranchName() { return branchName; }

    public Commit getPointedCommit() { return pointedCommit; }
    void setPointedCommit(Commit newC) { pointedCommit=newC; }


    //methods
    public static Map<String, Folder.Component> getAllBranchesToMap(MagitBranches branches)
    {


    }
}

package Logic;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Repository {

    Path path;
    HashSet<Branch> branches;
    Branch head;
    //Commit headCommit;

    //private String repositoryName;
    //String repositoryLocation;
    //String remoteReferenceName;
    //String referenceNameLocation;


    private String repositoryName;
    String repositoryLocation;
    String remoteReferenceName;
    String referenceNameLocation;


    public Repository(Path workingPath)
    {
        path = workingPath;
        branches = new HashSet<>();
    }

    public void Switch(Path path)
    {

    }
    public Branch setBranchByName(String name)
    {
        Branch newBranch = null;
        for(Branch b:branches) {
            if (b.branchName.equals(name))
                newBranch = b;
        }
            return newBranch;

    }
}

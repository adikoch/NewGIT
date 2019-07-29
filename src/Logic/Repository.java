package Logic;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Repository {

    Path path;
    HashSet<Branch> branches;
    Branch head;
    Map objects;
    //Commit headCommit;

    //private String repositoryName;
    //String repositoryLocation;
    //String remoteReferenceName;
    //String referenceNameLocation;


    private String repositoryName;
    String repositoryLocation;
    String remoteReferenceName;
    String referenceNameLocation;


    public Repository(Path workingPath, Branch headBranch)
    {
        path = workingPath;
        branches = new HashSet<>();
        branches.add(headBranch);
        head = headBranch;
    }

    public void Switch(Path path)
    {

    }
    public Branch setBranchByName(String name)
    {
        Branch newBranch = null;
        for(Branch b:branches) {
            if (b.getBranchName().equals(name))
                newBranch = b;
        }
            return newBranch;

    }
}

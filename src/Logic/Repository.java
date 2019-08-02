package Logic;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Repository {

   private Path path;
   private HashSet<Branch> branches;
   private Branch head;
    private Map<String,Path> SHA1Map = new HashMap<String,Path>();

    private String repositoryName;
    //String repositoryLocation;
    //String remoteReferenceName;
    //String referenceNameLocation;


    public Repository(Path workingPath, Branch headBranch)
    {
        path = workingPath;
        branches = new HashSet<>();
        branches.add(headBranch);
        head = headBranch;
        repositoryName = "EmptyRepository";

    }
    public  String getRepositoryName(){ return repositoryName; }
    public  Path getRepositoryPath(){ return path; }
    public  HashSet<Branch> getBranches(){ return branches ; }
    public Branch getHeadBranch (){return head;}

    public Map<String,Path> getSHA1Map(){ return SHA1Map;}

    public void Switch(Path newPath)
    {
        path = newPath;
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

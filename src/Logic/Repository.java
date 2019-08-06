package Logic;

import java.nio.file.Path;
import java.util.*;

public class Repository {

   private Path path;
   private HashSet<Branch> branches;
   private Branch head;
    private Map<String,Path> SHA1Map = new HashMap<>();
    private LinkedList<Commit> commitList = new LinkedList<>();


    public LinkedList<Commit> getCommitList() {
        return commitList;
    }

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
    //public void setHeadBranch(Branch b){this.head=b; }

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

    /*public void setBranchByName(String name)
    {
        for(Branch b:branches) {
            if (b.getBranchName().equals(name))
                head = b;}
    }*/
}

package Logic;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Repository {

   private Path path;
   private HashSet<Branch> branches;
   private Branch head;
    private Map<String,Path> SHA1Map = new HashMap<>();
    private Map<String,Commit> commitMap =new HashMap<>();

    Map<String,Commit> getCommitList() {
        //return commitList;
        return commitMap;
    }

    private String repositoryName;
    //String repositoryLocation;
    //String remoteReferenceName;
    //String referenceNameLocation;


    Repository(Path workingPath, Branch headBranch)
    {
        path = workingPath;
        branches = new HashSet<>();
        branches.add(headBranch);
        head = headBranch;
        repositoryName = "EmptyRepository";
        commitMap = new HashMap<>();
    }
    public  String getRepositoryName(){ return repositoryName; }
    public  Path getRepositoryPath(){ return path; }
    public  HashSet<Branch> getBranches(){ return branches ; }
    public Branch getHeadBranch (){return head;}
    public void setHeadBranch(Branch b){this.head=b; }

    public Map<String,Path> getSHA1Map(){ return SHA1Map;}

    void Switch(Path newPath)
    {
        path = newPath;
    }

    Branch setBranchByName(String name)// אמורים לקרוא לו גט ולא סט
    {
        Branch newBranch = null;
        for(Branch b:branches) {
            if (b.getBranchName().equals(name))
                newBranch = b;
        }
        return newBranch;

    }



    void getRepositorysBranchesObjecets()
    {
        String objectsPath = path + "\\.magit\\Objects";
        Path BranchesPath = Paths.get(path.toString() + "\\.magit\\Branches");
        File[] allBranches = BranchesPath.toFile().listFiles();
        String fileContent = "";

        for (File f : allBranches) {
            if (!f.getName().equals("Head")) {
                if(!f.getName().equals(this.head.getBranchName())) {
                    fileContent = GitManager.readTextFile(f.toString());
                    this.branches.add(new Branch(f.getName(), fileContent));
                }
            }

        }
    }
    /*public void setBranchByName(String name)
    {
        for(Branch b:branches) {
            if (b.getBranchName().equals(name))
                head = b;}
    }*/
}

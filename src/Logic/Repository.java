package Logic;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Repository {

    private Path path;
    private HashSet<Branch> branches;
    private Branch head;
    private Map<String, Commit> commitMap = new HashMap<>();
    private String repositoryName;
    //String repositoryLocation;
    //String remoteReferenceName;
    //String referenceNameLocation;
    Repository(Path workingPath) {
        path = workingPath;
        branches = new HashSet<>();
        repositoryName = "EmptyRepository";
        commitMap = new HashMap<>();
    }


    Repository(Path workingPath, Branch headBranch) {
        path = workingPath;
        branches = new HashSet<>();
        branches.add(headBranch);
        head = headBranch;
        repositoryName = "EmptyRepository";
        commitMap = new HashMap<>();
    }

    public String getRepositoryName() {
        return repositoryName;
    }

    public Path getRepositoryPath() {
        return path;
    }

    public HashSet<Branch> getBranches() {
        return branches;
    }

    public Branch getHeadBranch() {
        return head;
    }

    public void setHeadBranch(Branch b) {
        this.head = b;
    }


    Map<String, Commit> getCommitList() {
        //return commitList;
        return commitMap;
    }

    void Switch(Path newPath) {
        path = newPath;
    }

    Branch getBranchByName(String name)// אמורים לקרוא לו גט ולא סט
    {
        Branch newBranch = null;
        for (Branch b : branches) {
            if (b.getBranchName().equals(name))
                newBranch = b;
        }
        return newBranch;

    }


   void getRepositorysBranchesObjecets() {
        Path BranchesPath = Paths.get(path.toString() + "\\.magit\\Branches");
        File[] allBranches = BranchesPath.toFile().listFiles();
        String fileContent;

        for (File f : allBranches) {
            {
                if(f.getName() != "Head") {
                    fileContent = GitManager.readTextFile(f.toString());
                    this.branches.add(new Branch(f.getName(), fileContent));
                }
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


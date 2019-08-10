package Logic;

import Resources.jaxb.MagitRepository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Repository {
//members
    private Path path;
    private HashSet<Branch> branches;
    private Branch head;
    private Map<String, Commit> commitMap;
    private String repositoryName;


    //String remoteReferenceName;
    //String referenceNameLocation;


//con
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

    //get\set
    public String getRepositoryName() { return repositoryName; }

    public Path getRepositoryPath() { return path; }

    public HashSet<Branch> getBranches() { return branches; }

    public Branch getHeadBranch() { return head; }

    public void setHeadBranch(Branch b) { this.head = b; }


    Map<String, Commit> getCommitList() { return commitMap; }

    public MagitRepository loadFromXml(String i_XmlPath) throws FileNotFoundException, JAXBException {
            InputStream inputStream = new FileInputStream(i_XmlPath);
            JAXBContext jc = JAXBContext.newInstance("Resources.jaxb");
            Unmarshaller u = jc.createUnmarshaller();
            return (MagitRepository) u.unmarshal(inputStream);
    }

    //methods
    void Switch(Path newPath) { path = newPath; }

    Branch getBranchByName(String name)// אמורים לקרוא לו גט ולא סט
    {
        Branch newBranch = null;
        for (Branch b : branches) {
            if (b.getBranchName().equals(name))
                newBranch = b;
        }
        return newBranch;

    }


   void getRepositorysBranchesObjects() {
        Path BranchesPath = Paths.get(path.toString() + "\\.magit\\Branches");
        File[] allBranches = BranchesPath.toFile().listFiles();
        String fileContent;

        for (File f : allBranches) {
            {
                if(!f.getName().equals("Head")) {
                    fileContent = GitManager.readTextFile(f.toString());
                    this.branches.add(new Branch(f.getName(), fileContent));
                }
            }
        }
    }
}



package Logic;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class Repository {

    Path path;
    List<Commit> commits;
    LinkedList<Branch> branches;
    Commit headCommit;

    private String repositoryName;
    String repositoryLocation;
    String remoteReferenceName;
    String referenceNameLocation;


    public Repository(Path workingPath)
    {
        path = workingPath;
        branches = new LinkedList<Branch>();
    }
}

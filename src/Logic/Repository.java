package Logic;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class Repository {

     Path path;
    LinkedList<Branch> branches;
    Branch headCommit;

    private String repositoryName;
    String repositoryLocation;
    String remoteReferenceName;
    String referenceNameLocation;


    public Repository(Path workingPath)
    {
        path = workingPath;
        branches = new LinkedList<>();
    }
}

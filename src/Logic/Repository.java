package Logic;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Repository {

    private Path path;
    HashSet<Branch> branches;
    private Branch head;

    //Commit headCommit;
    //private String repositoryName;
    //String repositoryLocation;
    //String remoteReferenceName;
    //String referenceNameLocation;




    public Repository(Path workingPath)
    {
        path = workingPath;
        branches = new HashSet<Branch>();
    }


    public void Switch(Path newPath)
    {
        path = newPath;
    }
}

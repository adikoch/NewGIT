package Logic;

import java.io.File;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;

public class Repository {

   private Path path;
   private HashSet<Branch> branches;
   private Branch head;
   private Map objects;
    //Commit headCommit;

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



    public void Switch(Path newPath)
    {
        path= newPath;
    }

    /*
        {//רוצה לפתוח את הקובץ head שנמצא בתוך התיקייה currReposetory,.magit,.Branches לקחת משם את שם ה
        //לקחת משם את השם שמייצג את הבראנצ האקטיבי
        //התוכן של מה שבקובץ F זה השם של הבראנץ האקטיבי
        //בשם הזה יש את הsha1
        //לוקחת את הSHA1 ושמה אותו ב
                path=newPath;
        branches= new HashSet<>();
        File branchesFolder=
        //String headBranch=
        //branches.add()

     */

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

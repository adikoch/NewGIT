package Logic;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GitManager {
    private Repository GITRepository;
    private String userName;

    public void updateNewUserNameInLogic(String NewUserName)
    {
        userName = NewUserName;
    }

    public  void ImportRepFromXML()
    {

    }

    public void ShowFilesOfCurrCommit()
    {

    }

    public void ShowStatus()
    {

    }

    public void Commit()
    {

    }

    public void ShowAllBranches()
    {

    }

    public  void CreatBranch()
    {

    }

    public  void DeleteBranch()
    {

    }

    public static void CheckOut()
    {

    }

    public static void ShowHistoryOfActiveBranch()
    {

    }

    public void createEmptyRepositoryFolders(String repPath,String repName) throws FileAlreadyExistsException {
        Path workingPath;
        if (repPath.substring(repPath.length() - 1) != "/")
        {
            repPath += "\\";
        }
        if(!Files.exists(Paths.get(repPath+repName)))
        {
            new File(repPath + repName + "\\.magit\\objects").mkdirs();
            new File(repPath + repName + "\\.magit\\branches").mkdirs();
            workingPath = Paths.get(repPath + repName);
        }
        else throw new FileAlreadyExistsException("Error! This file already exist\n" + repPath + repName);
        this.GITRepository = (new Repository(workingPath));
        GITRepository.path = Paths.get(repPath + repName);
        GITRepository.branches.add(new Branch("Master"));


    }


    public void CreateNewBranch  (String newBranchName) throws FileAlreadyExistsException {
        for (Branch X : GITRepository.branches) {
            if (X.toString() == newBranchName) {
                throw new FileAlreadyExistsException("This Branch is already exist!");
            }
            else
            {
                Branch newB = new Branch(newBranchName);
                GITRepository.branches.add(newB);
                newB.pointedCommit = GITRepository.headCommit;

            }

        }

    }
}

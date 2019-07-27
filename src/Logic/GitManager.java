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

    public static void ImportRepFromXML()
    {

    }

    public static void ShowFilesOfCurrCommit()
    {

    }

    public static void ShowStatus()
    {

    }

    public static void Commit()
    {

    }

    public static void ShowAllBranches()
    {

    }

    public static void CreatBranch()
    {

    }

    public static void DeleteBranch()
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

    public void switchRepository(Path newRepPath) throws Exception
    {
        Path checkIfMagit= Paths.get(newRepPath+"\\.magit");
        if(Files.exists(newRepPath))
        {
            if(Files.exists(checkIfMagit))
            GITRepository.Switch(newRepPath);
            else throw new Exception();//exeption for not being magit

        }
        else throw new Exception();//exception for not existing
    }



}

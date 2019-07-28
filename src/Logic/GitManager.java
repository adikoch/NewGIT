package Logic;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class GitManager {
    private Repository GITRepository;
    private String userName;

    private class diffLog
    {
        private List<Path> updatedFiles;
        private List<Path> createdFiles;
        private List<Path> deletedFiles;

    }


    public void updateNewUserNameInLogic(String NewUserName) {
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

    public void createEmptyRepositoryFolders(String repPath,String repName) throws IOException {
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

//create branch file
        File head = new File( repPath + repName + "\\.magit\\branches\\head.txt");
        Writer out = null;
        try {
            out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(head)));
            out.write("there need to be a shha1 here of the head\r\n");
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ignored) {
                }
            }
        }

        File headCommit = new File( repPath + repName + "\\.magit\\objects\\headcommit.txt");
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            out = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(headCommit)));
            out.write("Shwa1 of repository" +
                    "StartOfMachine" +
            dateFormat.format(date) +
                            userName +
            "\r\n");
        } finally {
            if (out.equals(null)) {
                try {
                    out.close();
                } catch (IOException ignored) {
                }
            }
        }


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


    public void CreateNewBranch  (String newBranchName) throws FileAlreadyExistsException {
        for (Branch X : GITRepository.branches) {
            if (X.toString() == newBranchName) {
                throw new FileAlreadyExistsException("This Branch is already exist!");
            }
            else
            {
                Branch newB = new Branch(newBranchName);
                GITRepository.branches.add(newB);
                newB.pointedCommit = GITRepository.head.pointedCommit;

            }

        }

    }
}

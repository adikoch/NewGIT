package UI;
import Logic.Branch;
import Logic.Commit;
import Logic.GitManager;
import org.graalvm.compiler.core.CompilationWrapper;


import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Scanner;

import static java.lang.System.lineSeparator;
import static java.lang.System.out;



public class Runner {
    private Scanner scanInput = new Scanner(System.in);
    private GitManager manager = new GitManager();
    //private LinkedList<Commit> CommitsList= new LinkedList<>();


    public void run() {
        boolean isValid=true;
        MainMenu menu = new MainMenu();
        int userInput = 0;
        while (userInput != 13 || !isValid) {
            if(isValid) menu.show();
            userInput = scanInput.nextInt();
            try {
                if(isOutOfRange(0,13,userInput)){
                sendToOption(userInput);
                isValid=true;
                }

            } catch (IndexOutOfBoundsException ex) {
                System.out.println("number out of bound! Please enter a valid input");
                isValid=false;
            }
        }
    }


    private void sendToOption(int userInput) {
        switch (userInput) {
            case (1):
                UpdateUsername();//validation V
                break;

            case (2):
                //ImportRepFromXML();
                break;

            case (3):
                SwitchRepository();
                break;

            case (4):
                //ShowFilesOfCurrCommit();
                break;

            case (5):
                ShowStatus();
                break;

            case (6):
                Commit();
                break;

            case (7):
                ShowAllBranches();
                break;

            case (8):
                CreatBranch();
                break;

            case (9):
                DeleteBranch();
                break;

            case (10):
                //CheckOut();
                break;

            case (11):
                //ShowHistoryOfActiveBranch();
                break;

            case (12):
                createEmptyRepository();
                break;

            case (13):
                break;


        }


    }

    private void UpdateUsername() {
        if (manager.getGITRepository() != null) {

            System.out.println("Please enter the new username:");
            String NewUserName = scanInput.nextLine();
            manager.updateNewUserNameInLogic(NewUserName);
        } else {
            out.println("There is no repository defined! no changes occurred");
        }
    }

    private void SwitchRepository() {
        Scanner sb = new Scanner(System.in);

        System.out.println("Enter the new repository's path:");
        String NewRepositoryPathString = sb.nextLine();
        Path NewRepositoryPath = Paths.get(NewRepositoryPathString);
        try {
            manager.switchRepository(NewRepositoryPath);
        } catch (Exception e)//there are two kindes possible: exception for not existing, exeption for not being magit, need to handle 2 of them
        {
            System.out.println((""));
        }
    }



    private void Commit()
    {
        System.out.println("Please enter description for the commit");
        Scanner sc= new Scanner(System.in);
        String description= sc.nextLine();
        try {
            manager.ExecuteCommit(description, true);
        } catch (IOException e) {
            out.println("Commit Failed!");        }
        manager.getCreatedFiles().clear();
        manager.getDeletedFile().clear();
        manager.getUpdatedFiles().clear();
        //CommitsList.add((newCommit));

    }

    private void CreatBranch() {
        if (manager.getGITRepository() != null) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the full name for the new branch: ");
        String newBranchName = sc.nextLine();
        try {
            manager.CreateNewBranch(newBranchName);
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }
    } else {
        out.println("The is no repository defined!");
    }
    }

    private void createEmptyRepository() {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the full path for the new repository: ");
        String repPath = sc.nextLine();
        System.out.println("Choose name for the new repository: ");
        String repName = sc.nextLine();
        try {
            manager.createEmptyRepositoryFolders(repPath, repName);
        }
        catch(FileAlreadyExistsException nameEx) {// the wanted name is not available
            System.out.println("The name you chose is allready in the system, please choose a different name");
        }
        catch(ArithmeticException pathEx) { // path does not exist
            out.println("The path you chose does not exist, please choose a different one");
        }
    }




    public void ShowStatus() {
        if (manager.getGITRepository() != null) {
            out.println("Repository's Name:" + manager.getGITRepository().getRepositoryName());
            out.println("Repository's Path:" + manager.getGITRepository().getRepositoryPath().toString());
            out.println("Repository's User:" + manager.getUserName());
            try {
                manager.ExecuteCommit("",false);
                out.println("Deleted Files's Paths:" + manager.getDeletedFile());
                out.println("Added Files's Paths:" + manager.getCreatedFiles());
                out.println("Updated Files's Paths:" + manager.getUpdatedFiles());
            } catch (IOException e) {
                out.println("Show Status Failed!");}
        } else {
            out.println("The is no repository defined!");
        }


    }

    public void ShowAllBranches() {
        if (manager.getGITRepository() != null) {
            HashSet<Branch> branches = manager.getGITRepository().getBranches();
            for (Branch b : branches) {
                if (manager.getGITRepository().getHeadBranch().equals(b)) {
                    System.out.println("\u2764");
                }
                out.println("Branch's Name:" + b.getBranchName());
                out.println("Branch's pointed commit SHA1:" + b.getPointedCommit().getSHA());
                out.println("Branch's pointed commit Description:" + b.getPointedCommit().getDescription());
                out.println(lineSeparator());
            }
        } else {
            out.println("The is no repository defined!");
        }

    }
    public  void DeleteBranch()
    {
        Scanner sc = new Scanner(System.in);

        out.println("Please enter the name of the branch you would like to delete");
        String branchName = sc.nextLine();

        manager.deleteBranchfromRepository(branchName);

    }

    public boolean isOutOfRange(int min, int max, int val)
    {
        if (val>=min && val<=max)
            return true;
        else throw new IndexOutOfBoundsException();
    }
}



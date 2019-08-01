package UI;
import Logic.Branch;
import Logic.Commit;
import Logic.GitManager;


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
    private LinkedList<Commit> CommitsList= new LinkedList<>();


    public void run() {
        MainMenu menu = new MainMenu();
        int userInput = 0;
        while (userInput != 13) {
            menu.show();
            try {
                userInput = scanInput.nextInt();
                sendToOption(userInput);

            } catch (IndexOutOfBoundsException ex) {
                System.out.println("number out of bound!");
            }
        }
    }


    private void sendToOption(int userInput) {
        switch (userInput) {
            case (1):
                UpdateUsername();
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
                //DeleteBranch();
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

    private void Commit()
    {
        System.out.println("Please enter description for the commit");
        Scanner sc= new Scanner(System.in);
        String description= sc.nextLine();
        Commit newCommit= new Commit(description, manager);
        CommitsList.add((newCommit));
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
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void UpdateUsername() {
        if (manager.getGITRepository() != null) {

            System.out.println("Enter the new username:");
        String NewUserName = scanInput.nextLine();
        manager.updateNewUserNameInLogic(NewUserName);
        } else {
            out.println("The is no repository defined!");
        }
    }

    private void SwitchRepository() {
        System.out.println("Enter the new repository's path:");
        String NewRepositoryPathString = scanInput.nextLine();
        Path NewRepositoryPath = Paths.get(NewRepositoryPathString);
        try {
            manager.switchRepository(NewRepositoryPath);
        } catch (Exception e)//there are two kindes possible: exception for not existing, exeption for not being magit, need to handle 2 of them
        {
            System.out.println((""));
        }
    }

    public void ShowStatus() {
        if (manager.getGITRepository() != null) {
            out.println("Repository's Name:" + manager.getGITRepository().getRepositoryName());
            out.println("Repository's Path:" + manager.getGITRepository().getRepositoryPath().toString());
            out.println("Repository's User:" + manager.getUserName());
            //add diff
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
}
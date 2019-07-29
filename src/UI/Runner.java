package UI;
import Logic.GitManager;


import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Runner {
    private Scanner scanInput = new Scanner(System.in);
    private GitManager manager = new GitManager();

    public void run() {
        MainMenu menu = new MainMenu();
        int userInput = 0;
        while (userInput != 13) {
            menu.show();
            try {
                userInput = scanInput.nextInt();
                sendToOption(userInput);

            }
            catch(IndexOutOfBoundsException ex)
            {
                System.out.println("number out of bound!");
            }
        }
    }


    private void sendToOption(int userInput) {
        switch (userInput)
        {
            case(1):
                UpdateUsername();
                break;

            case(2):
                //ImportRepFromXML();
                break;

            case(3):
                SwitchRepository();
                break;

            case(4):
                //ShowFilesOfCurrCommit();
                break;

            case(5):
                //ShowStatus();
                break;

            case(6):
                //Commit();
                break;

            case(7):
                //ShowAllBranches();
                break;

            case(8):
                CreatBranch();
                break;

            case(9):
                //DeleteBranch();
                break;

            case(10):
                //CheckOut();
                break;

            case(11):
                //ShowHistoryOfActiveBranch();
                break;

            case(12):
                createEmptyRepository();
                break;

            case(13):
                break;




        }


    }
    private void CreatBranch ()
    {
        System.out.println("Enter the full name for the new branch: ");
        String newBranchName = scanInput.nextLine();
        try {
            manager.CreateNewBranch(newBranchName);
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    private void createEmptyRepository()
    {
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the full path for the new repository: ");
        String repPath = sc.nextLine();
        System.out.println("Choose name for the new repository: ");
        String repName = sc.nextLine();
        try {
            manager.createEmptyRepositoryFolders(repPath,repName);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
     private void UpdateUsername()
     {
         System.out.println("Enter the new username:");
         String NewUserName =  scanInput.nextLine();
         manager.updateNewUserNameInLogic(NewUserName);
     }

    private void SwitchRepository()
    {
        System.out.println("Enter the new repository's path:");
        String NewRepositoryPathString =  scanInput.nextLine();
        Path NewRepositoryPath = Paths.get(NewRepositoryPathString);

        try{manager.switchRepository(NewRepositoryPath);}
        catch (IllegalArgumentException e1)
        {
            System.out.println("");
        }
        //catch() {}
        //catch(Exception e) {}

//there are two kindes of exeptions possible: exception for not existing, exeption for not being magit, need to handle 2 of them

    }

}
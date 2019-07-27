package UI;
import Logic.GitManager;


import java.nio.file.FileAlreadyExistsException;
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
                GitManager.ImportRepFromXML();
                break;

            case(3):
                SwitchRepository();
                break;

            case(4):
                GitManager.ShowFilesOfCurrCommit();
                break;

            case(5):
                GitManager.ShowStatus();
                break;

            case(6):
                GitManager.Commit();
                break;

            case(7):
                GitManager.ShowAllBranches();
                break;

            case(8):
                GitManager.CreatBranch();
                break;

            case(9):
                GitManager.DeleteBranch();
                break;

            case(10):
                GitManager.CheckOut();
                break;

            case(11):
                GitManager.ShowHistoryOfActiveBranch();
                break;

            case(12):
                createEmptyRepository();
                break;

            case(13):
                break;




        }


    }
    private void CreatBranch () throws FileAlreadyExistsException
    {
        System.out.println("Enter the full name for the new branch: ");
        String newBranchName = scanInput.nextLine();
        try {
            manager.CreateNewBranch(newBranchName);
        }
        catch (FileAlreadyExistsException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    private void createEmptyRepository()
    {
        System.out.println("Enter the full path for the new repository: ");
        String repPath = scanInput.nextLine();
        System.out.println("Choose name for the new repository: ");
        String repName = scanInput.nextLine();
        try {
            manager.createEmptyRepositoryFolders(repPath,repName);
        } catch (FileAlreadyExistsException ex) {
            System.out.println(ex.getFile());
        }
    }
     private void UpdateUsername()
     {
         System.out.println("Enter the new username:");
         String NewUserName =  scanInput.nextLine();
         manager.updateNewUserNameInLogic(NewUserName);
     }

    public void SwitchRepository()
    {
        System.out.println("Enter the new repository's path:");
        String NewReposetory =  scanInput.nextLine();
    }

}
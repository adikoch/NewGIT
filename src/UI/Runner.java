package UI;
import Logic.GitManager;


import java.nio.file.FileAlreadyExistsException;
import java.util.Scanner;

public class Runner {
    private Scanner scanInput = new Scanner(System.in);
    private GitManager manager = new GitManager();

    public void run() {
        MainMenu menu = new MainMenu();
        while (true) {
            menu.show();
            int userInput;
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


    public void sendToOption(int userInput) {
        switch (userInput)
        {
            case(1):
                UpdateUsername();
                break;

            case(2):
                GitManager.ImportRepFromXML();//todo
                break;

            case(3):
                GitManager.SwitchRepository();
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
    public void createEmptyRepository()
    {

        Scanner sc = new Scanner(System.in);
        System.out.println("Enter the full path for the new repository: ");
        String repPath = sc.nextLine();
        System.out.println("Choose name for the new repository: ");
        String repName = sc.nextLine();
        try {
            manager.createEmptyRepositoryFolders(repPath,repName);
        } catch (FileAlreadyExistsException ex) {
            System.out.println(ex.getFile());
        }
    }
     public void UpdateUsername()
     {
         System.out.println("Enter the new username:");
         String NewUserName =  scanInput.nextLine();
         manager.updateNewUserNameInLogic(NewUserName);
     }

}
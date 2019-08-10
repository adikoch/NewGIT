package UI;
import Logic.Branch;
import Logic.GitManager;
//import org.graalvm.compiler.core.CompilationWrapper;


import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;

import static java.lang.System.lineSeparator;
import static java.lang.System.out;



public class Runner {
    private Scanner scanInput = new Scanner(System.in);
    private GitManager manager = new GitManager();
    //private LinkedList<Commit> CommitsList= new LinkedList<>();


    public void run() {
        boolean isNumber=true, isValid=true;
        int result=0;
        MainMenu menu = new MainMenu();
        String userInput="0";
        while(userInput!="13" || !isValid)
        {
            if (isValid) menu.show();
            userInput=scanInput.nextLine();
            try{
                result = Integer.parseInt(userInput);
                isNumber=true;}
            catch (NumberFormatException e) {isNumber= false;}


           isValid=isNumber && !isOutOfRange(1,13,result);

            if(isValid)
                sendToOption(result);
            else out.println("Please enter a valid input!");
        }
    }



    private void sendToOption(int userInput) {
        switch (userInput) {
            case (1):
                UpdateUsername();//validation V
                break;

            case (2):
                ImportRepFromXML();
                break;

            case (3):
                SwitchRepository();//validation V
                break;

            case (4):
                ShowFilesOfCurrCommit();
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
                CreatBranch();// יווצא קובץ חדש בתיקייה branches, שבתוכו יש את הsha1 שכרגע מוצבע ע"י הhead הנוכחי,וגם הקומיט הנוכחי יהיה כמוהו, ושהhead הנוכחי עכשיו יצביע על הbranch שעכשיו יצרתי, להוסיף את הbranch לוגית ברפוזטורי
                break;

            case (9):
                DeleteBranch();
                break;

            case (10):
                CheckOut();
                break;

            case (11):
                ShowHistoryOfActiveBranch();
                break;

            case (12):
                //createEmptyRepository();//validation V
                break;

            case (13):
                break;


        }


    }

    private void ShowHistoryOfActiveBranch() {//לא עובד במקרה שעושים סוויץ רפוזטורי
        manager.ShowHistoryActiveBranch();
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
        boolean isValid= false;
        Scanner sb = new Scanner(System.in);

        while(!isValid)
        {
        System.out.println("Enter the new repository's path:");
        String NewRepositoryPathString = sb.nextLine();
        Path NewRepositoryPath = Paths.get(NewRepositoryPathString);
        try {
            manager.switchRepository(NewRepositoryPath);
            isValid=true;
        }
        catch (ExceptionInInitializerError e) {
            out.println("This path is not a part of the magit system");
        }
        catch (IllegalArgumentException er) {
            out.println("The path does not exist, please try again");
        } catch (UnsupportedOperationException err) {
            out.println("One of the file does noe available, please try again");
        }
        catch (IOException errr) {out.println("Unable to zip the required file");}
        }
    }



    private void Commit()
    {
        System.out.println("Please enter description for the commit");
        Scanner sc= new Scanner(System.in);
        String description= sc.nextLine();
        try {
            manager.ExecuteCommit(description, true);
        } catch (Exception e) {
            out.println("Commit Failed! Unable to create files");}
        out.println("Deleted Files's Paths:" + manager.getDeletedFiles().toString());
        out.println("Added Files's Paths:" + manager.getCreatedFiles().toString());
        out.println("Updated Files's Paths:" + manager.getUpdatedFiles().toString());
        manager.getCreatedFiles().clear();
        manager.getDeletedFiles().clear();
        manager.getUpdatedFiles().clear();
        //CommitsList.add((newCommit));

    }



    private void CreatBranch() {
        String newBranchName;
        boolean isValid=false;
        Scanner sc = new Scanner(System.in);
        out.println("Please enter the name of the new branch");
        newBranchName=sc.nextLine();//getting the name

        while(!isValid){
            for(Branch b: manager.getGITRepository().getBranches())                //checking if exist already
            {
                if(b.getBranchName().equals(newBranchName))
                {
                    isValid=false;
                    out.println("Branch name already exist, please enter a different name");
                    newBranchName=sc.nextLine();//getting the name
                }
                else isValid=true;
            }
        }//valid:
        manager.CreatBranch(newBranchName);
    }

//    private void createEmptyRepository() {
//        boolean isValid=false;
//        Scanner sc = new Scanner(System.in);
//        String repPath= null;
//        String repName= null;
//
//        while(repPath==null || !manager.DoesPathExist(repPath))
//        {
//
//        }
//
//        while(true) {//does path exist
//            out.println("Enter the full path for the new repository: ");
//            String repPath = sc.nextLine();
//            out.println("Choose name for the new repository: ");
//            String repName = sc.nextLine();
//            try {
//                manager.createEmptyRepositoryFolders(repPath, repName);
//                isValid=true;
//            }
//            catch (IllegalArgumentException e) {
//                out.println("The wanted name already exist, please try again");
//                isValid=false;}
//            catch (ExceptionInInitializerError er) {
//                out.println("The wanted path does not exist, please try again");
//                isValid=false;}
//        }
//    }

    /*
        private void createEmptyRepository() {
        boolean isValid=false;
        Scanner sc = new Scanner(System.in);

        while(!isValid) {
            out.println("Enter the full path for the new repository: ");
            String repPath = sc.nextLine();
            out.println("Choose name for the new repository: ");
            String repName = sc.nextLine();
            try {
                manager.createEmptyRepositoryFolders(repPath, repName);
                isValid=true;
            }
            catch (IllegalArgumentException e) {
                out.println("The wanted name already exist, please try again");
                isValid=false;}
            catch (ExceptionInInitializerError er) {
                out.println("The wanted path does not exist, please try again");
                isValid=false;}
        }
    }
     */




    void ShowStatus() {
        if (manager.getGITRepository() != null) {
            out.println("Repository's Name:" + manager.getGITRepository().getRepositoryName());
            out.println("Repository's Path:" + manager.getGITRepository().getRepositoryPath().toString());
            out.println("Repository's User:" + manager.getUserName());
            try {
                manager.ExecuteCommit("",false);
                out.println("Deleted Files's Paths:" + manager.getDeletedFiles());
                out.println("Added Files's Paths:" + manager.getCreatedFiles());
                out.println("Updated Files's Paths:" + manager.getUpdatedFiles());
                manager.getCreatedFiles().clear();
                manager.getDeletedFiles().clear();
                manager.getUpdatedFiles().clear();
            } catch (Exception e) {
                out.println("Show Status Failed! Unable to create files");}
        } else {
            out.println("The is no repository defined!");
        }


    }


    void ShowAllBranches()
    {
        String allBranches=manager.getAllBranches();
        out.println(allBranches);
    }

    void DeleteBranch()
    {
        out.println("Please enter the name of the branch to delete");
        Scanner sc= new Scanner(System.in);
        String branchName= sc.nextLine();

        try{
        manager.DeleteBranch(branchName);}
        catch(Exception e)
        {
            out.println("Erasing the head branch is not a valid action, no changes occurred");
        }

    }

    private static boolean isOutOfRange(int min, int max, int val)
    {
        if (val>=min && val<=max )
            return false;
        return true;
    }

    private void CheckOut()
    {
        out.println("Please enter the name of the branch to move over to");
        Scanner sc= new Scanner(System.in);
        String branchName= sc.nextLine();
        try {
            manager.ExecuteCommit("", false);
            if (manager.getDeletedFiles().size() != 0 ||
                    manager.getUpdatedFiles().size() != 0 ||
                    manager.getCreatedFiles().size() != 0) {
                out.println("There are unsaved changes in the WC. would you like to save it before checkout?" );
                String toCommit = sc.nextLine();
               if(toCommit.equals("yes"))
                {
                    manager.ExecuteCommit("commit before checkout to " + sc.toString() + "Branch", true);
                }
            }
                manager.executeCheckout(branchName);
            manager.getCreatedFiles().clear();
            manager.getDeletedFiles().clear();
            manager.getUpdatedFiles().clear();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ShowFilesOfCurrCommit()
    {
        try{
        manager.showFilesOfCommit();}
        catch(Exception e) {
            out.println("Unable to generate folder from commit object");}
    }

    private void ImportRepFromXML()
    {
        out.println("Please enter the path of the xml file to import");
        Scanner sc= new Scanner(System.in);
        String xmlPath= sc.nextLine();
        manager.ImportRepositoryFromXML(xmlPath);
    }



}
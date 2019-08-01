package Logic;

import UI.Runner;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Commit {
    //private String SHA1;
    private Folder treeRoot;// main library
    private LinkedList<String> SHA1PreveiousCommit;
    private String description;
    private String creationDate;
    private String changer;


    //inbar:
    private String Sha1PrevCommit;


    public Commit()
    {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY - hh:mm:ss:sss");
        Date date = new Date();
        treeRoot = new Folder();
        SHA1PreveiousCommit = new LinkedList<>();
        description = "Start The Machine";
        creationDate = dateFormat.format(date);
        changer = "Administrator";
        //GitManager.generateSHA1FromString(this.getCommitFileContent());
    }

    public Commit(String usersDescription, GitManager manager)
    {
        //ask for: , , , prevCommit, head branch update
        description=usersDescription;

        //Date
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY - hh:mm:ss:sss");
        Date date = new Date();
        creationDate = dateFormat.format(date);
        changer= manager.getUserName();
        //SHA1PreveiousCommit=
        //רוצה ללכת לקובץ head שנמצא בתוך .magit.Branches ולשנות את התוכן בו לשם הבראנצ הזה



        //prev Commit

    }

    public String getDescription (){return description;}
    public String getCommitFileContent() {
        String delimiter = ", ";
        StringBuilder sb = new StringBuilder();

        sb.append(GitManager.generateSHA1FromString(treeRoot.getFolderContentString()));
        sb.append(System.lineSeparator());
        sb.append(getPreviousCommitsSHAs(","));
        sb.append(System.lineSeparator());
        sb.append(description);
        sb.append(System.lineSeparator());
        sb.append(creationDate);
        sb.append(System.lineSeparator());
        sb.append(changer);
        return sb.toString();
    }

    public String getSHA(){
        return  GitManager.generateSHA1FromString(getCommitFileContent());
    }

    public String getPreviousCommitsSHAs(String delimiter) {
        StringBuilder sb = new StringBuilder();
        for(String sha: this.SHA1PreveiousCommit) {
            sb.append(sha);
            sb.append(delimiter);
        }
        if(SHA1PreveiousCommit.size() != 0) {
            sb.delete(sb.length() - delimiter.length() - 1, sb.length() - 1);
        }

        return sb.toString();
    }


    public Commit(File file)
    {

    }

    public void exportToFile()
    {

    }


}

package Logic;

import UI.Runner;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Commit {
    //private String SHA1;
    private Folder origCommit;// main library
    private String SHA1PreveiousCommit;
    private String SHA1PrevPrevCommit;
    private String description;
    private String creationDate;
    private String changer;

    public String getSHA1PreveiousCommit() {
        return SHA1PreveiousCommit;
    }
    public Folder getOrigCommit() { return origCommit;}
    public void setOrigCommit(Folder folder) {origCommit =  folder;}


    public void setSHA1PreveiousCommit(String SHA1PreveiousCommit) {
        this.SHA1PreveiousCommit = SHA1PreveiousCommit;
    }

    public Commit()
    {
        description = "Start The Machine";
        creationDate = GitManager.getDate();
        changer = "Administrator";
    }

    public Commit(String usersDescription, String userName)
    {
        description=usersDescription;
        //Date
        creationDate = GitManager.getDate();
        changer= userName;
        //SHA1PreveiousCommit=
        //רוצה ללכת לקובץ head שנמצא בתוך .magit.Branches ולשנות את התוכן בו לשם הבראנצ הזה



        //prev Commit

    }

    public String getDescription (){return description;}
    public String getCommitFileContent() {
        String delimiter = ", ";
        StringBuilder sb = new StringBuilder();

        //כאן רוצה לעשות שהקובץ שעליו עושים sha1 יהיה קובץ טקסט ריק
        //sb.append(GitManager.generateSHA1FromString(treeRoot.getFolderContentString()));
        sb.append(System.lineSeparator());
        sb.append(getSHA1PreveiousCommit());
        //sb.append(getPreviousCommitsSHAs(","));
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

    /*public String getPreviousCommitsSHAs(String delimiter) {
        StringBuilder sb = new StringBuilder();
        for(String sha: this.SHA1PreveiousCommit) {
            sb.append(sha);
            sb.append(delimiter);
        }
        if(SHA1PreveiousCommit.size() != 0) {
            sb.delete(sb.length() - delimiter.length() - 1, sb.length() - 1);
        }

        return sb.toString();
    }*/


    public Commit(File file)
    {

    }

    public void exportToFile()
    {

    }



}

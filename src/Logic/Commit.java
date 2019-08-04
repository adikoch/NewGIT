package Logic;

import UI.Runner;
import sun.security.pkcs11.P11Util;

import java.io.File;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

public class Commit {
    private String SHA1;
    private Folder rootFolder;
    private String rootFolderPathName;// main library repository
    private String SHA1PreveiousCommit;
    private String SHA1PrevPrevCommit;
    private String description;
    private String creationDate;
    private String changer;
    private String SHAContent;

    public String getSHA1PreveiousCommit() {
        return SHA1PreveiousCommit;
    }
   public String getSHA1PrevePrevCommit(){return SHA1PrevPrevCommit;}
    public String getRootfolderName(){return rootFolderPathName;}
    public void setRootfolder(String path){ rootFolderPathName = path;}
    public Folder getRootfolder(){return rootFolder;}


       public Folder getOrigCommit() { return rootFolder;}
      public void setOrigCommit(Folder folder) {rootFolder =  folder;}
    public void setSHA1ToCommit(Path path)
    {

    }

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

    public void setCommitFileContentToSHA() {
        String delimiter = ", ";
        StringBuilder s = new StringBuilder();
if(rootFolder != null) {
    s.append(GitManager.generateSHA1FromString(rootFolder.getFolderContentString()));
}
else {
    s.append(GitManager.generateSHA1FromString(""));
}
        s.append(System.lineSeparator());
        s.append(getSHA1PreveiousCommit());
        s.append(System.lineSeparator());
        s.append(getSHA1PrevePrevCommit());
        s.append(System.lineSeparator());
        s.append(description);
        s.append(System.lineSeparator());
        s.append(creationDate);
        s.append(System.lineSeparator());
        s.append(changer);
        SHA1 = GitManager.generateSHA1FromString(s.toString());
        SHAContent = s.toString();
    }

//    public String getCommitFileContent() {
//        String delimiter = ", ";
//        StringBuilder sb = new StringBuilder();
//
//        //כאן רוצה לעשות שהקובץ שעליו עושים sha1 יהיה קובץ טקסט ריק
//        //sb.append(GitManager.generateSHA1FromString(treeRoot.getFolderContentString()));
//        sb.append(System.lineSeparator());
//        sb.append(getSHA1PreveiousCommit());
//        sb.append(System.lineSeparator());
//        sb.append(getSHA1PrevePrevCommit());
//        sb.append(System.lineSeparator());
//        sb.append(description);
//        sb.append(System.lineSeparator());
//        sb.append(creationDate);
//        sb.append(System.lineSeparator());
//        sb.append(changer);
//        return sb.toString();
//    }

    public String getSHA(){
        return SHA1;
        //return  GitManager.generateSHA1FromString(getCommitFileContent());
    }
    public String getSHAContent(){
        return SHAContent;
        //return  GitManager.generateSHA1FromString(getCommitFileContent());
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

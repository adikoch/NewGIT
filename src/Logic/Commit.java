package Logic;

import jaxb.schema.generated.MagitCommits;
import jaxb.schema.generated.MagitFolders;
import jaxb.schema.generated.MagitSingleCommit;
import jaxb.schema.generated.MagitSingleFolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Commit {
    private String SHA1;
    private Folder rootFolder;
    private String rootFolderSHA1;
    private String SHA1PreveiousCommit;
    private String SHA1PrevPrevCommit;
    private String description;



    private String creationDate;
    private String changer;
    private String SHAContent;

    //constractors

    public Commit()
    {
        description = "Start The Machine";
        creationDate = GitManager.getDate(null);
        changer = "Administrator";
    }

    public Commit(String usersDescription, String userName)
    {
        description=usersDescription;
        creationDate = GitManager.getDate(null);
        changer= userName;
    }

    public Commit(ArrayList<String> args)
    {
        rootFolderSHA1 = args.get(0);
          SHA1PreveiousCommit = args.get(1);
          SHA1PrevPrevCommit = args.get(2);
          description = args.get(3);
          creationDate = args.get(4);
          changer = args.get(5);
    }


    // getters, setters
    public String getSHA1PrevPrevCommit() { return SHA1PrevPrevCommit; }
    public void setSHA1PrevPrevCommit(String SHA1PrevPrevCommit) { this.SHA1PrevPrevCommit = SHA1PrevPrevCommit; }

    public String getRootFolderSHA1() { return rootFolderSHA1; }
    public void setRootFolderSHA1(String rootFolderSHA1) { this.rootFolderSHA1 = rootFolderSHA1; }

    public String getSHAContent() { return SHAContent; }

    public Folder getRootFolder() { return rootFolder; }
    public void setRootFolder(Folder folder) { rootFolder =  folder;}

    public String getSHA1PreveiousCommit() { return SHA1PreveiousCommit; }
    public void setSHA1PreveiousCommit(String SHA1PreveiousCommit) { this.SHA1PreveiousCommit = SHA1PreveiousCommit; }

    public void setChanger(String changer) { this.changer = changer; }

    public void setSHA1(String SHA1) { this.SHA1=SHA1; }
    public String getSHA(){ return SHA1; }

    public String getDescription (){ return description;}
    public void setDescription (String descriptionFromUser){ description =  descriptionFromUser; }

    public void setCreationDate(String creationDate) { this.creationDate = creationDate; }

        //methods
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
        s.append(getSHA1PrevPrevCommit());
        s.append(System.lineSeparator());
        s.append(description);
        s.append(System.lineSeparator());
        s.append(creationDate);
        s.append(System.lineSeparator());
        s.append(changer);
        SHA1 = GitManager.generateSHA1FromString(s.toString());
        SHAContent = s.toString();
    }
    public static Map<String, Commit> getAllCommitsToMap(MagitCommits commits, Map<String, Folder.Component> folderList)
    {
        Map<String, Commit> newMap = new HashMap<>();
        List<MagitSingleCommit> commitlist = commits.getMagitSingleCommit();
        for(MagitSingleCommit c: commitlist)
        {
            ArrayList<String> args = new ArrayList<>();
            String rootFolderID = c.getRootFolder().getId();
            String rootFolderSHA1 = folderList.get(rootFolderID).getFolderSHA1();
            args.add(0,rootFolderSHA1);
            args.add(1,null);
            args.add(2,null);
            args.add(3,c.getMessage());
            args.add(4,c.getDateOfCreation());
            args.add(5,c.getAuthor());
            Commit newcommit = new Commit(args);
            newMap.put(c.getId(),newcommit);
            newcommit.setRootFolder((Folder)folderList.get(rootFolderID).getDirectObject());
        }
        return newMap;
    }

//    public void exportToFile()
//    {
//
//    }
}

package Logic;

import java.io.File;
import java.util.Date;
import java.util.List;

public class Folder {
    //private  Integer folderID;
    //private String blobName;
    //private String sha1;//content
    //private String lastUpdater;
    //private Date lastUpdateDate;
    //Enum folderType;
    //List<component> items;//sha1
    //Boolean isRoot;

    private List<component> components;
    private String SHA1;



    public class component
    {
        private FolderType type;
        private String Sha1;
        private String name;
        private String lastUpdater;
        private Date lastUpdateDate;
    }

    public Folder(File file) //creating by text file
    {

    }

    public Folder(String XMLstr) // creating by XML
    {

    }

    public String toString() //
    {
        return "dsd";
    }

    public void exportToFile() // check if the sha1 exist
    {

    }

}

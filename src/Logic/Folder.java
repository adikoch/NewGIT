package Logic;

import java.util.Date;
import java.util.List;

public class Folder {
    private  Integer folderID;
    private String blobName;
    private String shwa1;//content
    private String lastUpdater;
    private Date lastUpdateDate;
    Enum folderType;
    List<component> items;//sha1
    Boolean isRoot;

    public class component
    {
        FolderType type;
        String Shw1;
        String name;
        String lastUpdater;
        Date lastUpdateDate;
    }

}

package Logic;

import java.io.File;
import java.util.Date;
import java.util.List;

public class Commit {
    public String rootSHA1;
    public Folder treeRoot;
    public String mainFolderSHA1;
    private List<String> SHA1_preveiousCommit;
    private String description;
    private Date creationDate;
    private String changer;

    public Commit()
    {


    }
public Commit(String comment)
{

}

    public Commit(File file)
    {

    }
    public void exportToFile()
    {

    }


}

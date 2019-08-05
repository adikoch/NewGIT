package Logic;

public class  Blob  implements FileObject{

    private String content;


    //private Integer blobID;
    //private String blobName;
    //private String sha1;
    //private String lastUpdater;
    //private String lastUpdateDate;


    public void exportToFile()
    {

    }

    public Blob(String Content)
    {
        this.content=Content;
    }

    public String getContent()
    {
        return content;
    }

//public void createObj()
//{
//
//}
}

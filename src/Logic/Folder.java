package Logic;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
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

    private LinkedList<Component> components;
    //private String SHA1;

    public Folder(Component c)
    {
        components = new LinkedList<>();
        components.add(c);
    }


    protected static class Component implements Comparable<Component>{
        private FolderType type;
        private String Sha1;
        private String name;
        private String lastUpdater;
        private String lastUpdateDate;

        public Component(String name, String sha1, String type, String lastUpdater, String lastUpdateDate){

            this.type=FolderType.valueOf(type);
            this.name=name;
            this.lastUpdateDate=lastUpdateDate;
            this.Sha1=sha1;
            this.lastUpdater=lastUpdater;

        }       // from text/xml file
        public void Compnenet() {}

        //comperator
        public int compareTo(Component folderComponent) {
            return this.name.compareTo(folderComponent.name);
        }
    public FolderType getFolderType()
    {return type;}
        public String  getFolderName()
        {return name;}
        public String  getFolderSHA1()
        {return Sha1;}


        public String getComponentsString() {
            StringBuilder content = new StringBuilder();
            content.append(name);
            content.append(",");
            content.append(Sha1);
            content.append(",");
            content.append(type);
            content.append(",");
            content.append(lastUpdater);
            content.append(",");
            content.append(lastUpdateDate);
            return content.toString();

        }
    }

    public String stringComponentsToString() {
        StringBuilder content = new StringBuilder();
        for (Component a : components) {
            content.append(a.getComponentsString());
            content.append(System.lineSeparator());
        }
        return content.toString();
    }

    public Folder() // creating by XML or new empty root
    {
        //SHA1 = org.apache.commons.codec.digest.DigestUtils.sha1Hex("");
        components = new LinkedList<>();

    }

    public Folder(File file) //creating by text file
    {

    }

//    public String getSHA1() {
//        return SHA1;
//    }
//    public String toString()
//    {
//        return "dsd";
//    }


    public void exportToFile() // check if the sha1 exist
    {

    }

    public List<Component> getComponents() {
        return this.components;
    }


    public String getFolderContentString() {
        StringBuilder sb = new StringBuilder();

        for(Component c: components) {
            sb.append(c.toString());
            sb.append(System.lineSeparator());
        }


        return sb.toString();
    }


}



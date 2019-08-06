package Logic;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Folder implements FileObject{
    //private  Integer folderID;
    //private String blobName;
    //private String sha1;//content
    //private String lastUpdater;
    //private Date lastUpdateDate;
    //Enum folderType;
    //List<component> items;//sha1
    //Boolean isRoot;

    private ArrayList<Component> components;
    //private String SHA1;

    Folder(Component c)
    {
        components = new ArrayList<>();
        components.add(c);
    }
    Folder(ArrayList<Component> list) {
        components = list;
    }

    protected static class Component implements Comparable<Component> , FileObject{
        private FolderType type;
        private String Sha1;
        private String name;
        private String lastUpdater;
        private String lastUpdateDate;

        private FileObject directObject;

        public FileObject getDirectObject() {
            return directObject;
        }

        void setDirectObject(FileObject directObject) {
            this.directObject = directObject;
        }


        Component(String name, String sha1, FolderType type, String lastUpdater, Long lastUpdateDate){

            this.type= type;
            this.name=name;
            this.lastUpdateDate=lastUpdateDate.toString();
            this.Sha1=sha1;
            this.lastUpdater=lastUpdater;

        }       // from text/xml file
        public void Compnenet() {}

        //comperator
        public int compareTo(Component folderComponent) {
            return this.name.compareTo(folderComponent.name);
        }
        FolderType getComponentType() {return type;}
        String getComponentSHA1() { return Sha1;}
        String  getComponentName() {return name;}
        String  getFolderSHA1() {return Sha1;}

//        public void createObj()
//        {
//
//        }

        String getComponentsString() {
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

    String stringComponentsToString() {
        StringBuilder content = new StringBuilder();
        for (Component a : components) {
            content.append(a.getComponentsString());
            content.append(System.lineSeparator());
        }
        return content.toString();
    }

    Folder() // creating by XML or new empty root
    {
        //SHA1 = org.apache.commons.codec.digest.DigestUtils.sha1Hex("");
        components = new ArrayList<>();

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

    ArrayList<Component> getComponents() {
        return this.components;
    }


    String getFolderContentString() {
        StringBuilder sb = new StringBuilder();

        for(Component c: components) {
            sb.append(c.toString());
            sb.append(System.lineSeparator());
        }


        return sb.toString();
    }


}



package Logic;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Folder implements FileObject{

    //members
    private ArrayList<Component> components;

    //con
//    Folder(Component c)
//    {
//        components = new ArrayList<>();
//        components.add(c);
//    }
    Folder(ArrayList<Component> list) {
        components = list;
    }


    protected static class Component implements Comparable<Component> , FileObject{
        //members
        private FolderType type;
        private String Sha1;
        private String name;
        private String lastUpdater;
        private String lastUpdateDate;
        private FileObject directObject;

        //con
        Component(String name, String sha1, FolderType type, String lastUpdater, String lastUpdateDate){

            this.type= type;
            this.name=name;
            this.lastUpdateDate=lastUpdateDate.toString();
            this.Sha1=sha1;
            this.lastUpdater=lastUpdater;

        }       // from text/xml file


        //get\set
        public FileObject getDirectObject() { return directObject; }
        void setDirectObject(FileObject directObject) { this.directObject = directObject; }

        FolderType getComponentType() {return type;}

        String getComponentSHA1() { return Sha1;}

        String  getComponentName() {return name;}

        String  getFolderSHA1() {return Sha1;}

        //methods
        //comperator
        public int compareTo(Component folderComponent) {
            return this.name.compareTo(folderComponent.name);
        }


        String getComponentsStringFromComponent() {
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

       static Component getComponentFromString  (String s)
        {
            String[] st =  s.split(",");
            Component c = new Component(st[0],st[1],getTypeFromString(st[2]),st[3], st[4]);
            return c;
        }

    }

    //con
    Folder() // creating by XML or new empty root
    {
        components = new ArrayList<>();

    }

    public Folder(File file) //creating by text file
    {

    }

    //set\get
    public void setComponents(ArrayList<Component> components) { this.components = components; }
    ArrayList<Component> getComponents() { return this.components; }

    //methods
   static FolderType getTypeFromString(String s)
    {
        if("Folder".equals(s))
        {
            return FolderType.Folder;
        }
        else
        {
            return FolderType.Blob;
        }
    }


    String stringComponentsToString() {
        StringBuilder content = new StringBuilder();
        for (Component a : components) {
            content.append(a.getComponentsStringFromComponent());
            content.append(System.lineSeparator());
        }
        return content.toString();
    }



    ArrayList<Component> setComponentsFromString(String compomemtString) throws IOException {
        BufferedReader br = new BufferedReader(new StringReader(compomemtString));
        ArrayList<Component> st = new ArrayList<>();
        String a;
        int i = 0;
        while ((a = br.readLine()) != null) {
            st.add(Component.getComponentFromString(a));
            i++;
        }
        br.close();
        return st;
    }

    String getFolderContentString() {
        StringBuilder sb = new StringBuilder();

        for(Component c: components) {
            sb.append(c.getComponentsStringFromComponent());
            sb.append(System.lineSeparator());
        }


        return sb.toString();
    }

//    public void exportToFile() // check if the sha1 exist
//    {
//
//    }
}



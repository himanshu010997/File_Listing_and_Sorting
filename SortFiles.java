
/* Before running this make a copy of everything on desktop and put it in another place also just to be safe before testing the code
 * Run the program and it will display the 10 biggest files in the given directory and also move files from desktop to folders in documents
 * according to their type with folder names as the extension of the file
 */

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class SortFiles {

  public static File Biggest10[] = new File[10];
  public static int sizeList=0, asd=0;

  // Method to get the index of smallest file in Biggest10
  public static int getMinIndex() {

    int min=0, i=0;
    long mNum=Biggest10[0].length();  // mNum represents minimum size of files

    // Linear search to find the smallest file in size
    for(i=0; i<sizeList; i++)

      if(mNum>Biggest10[i].length()) {
        mNum=Biggest10[i].length();
        min=i;
      }

    return min;
  }

  static void RecursiveSearch(File[] files, int index, int level) {

    int minI;

    // Termination condition
    if(index == files.length)
      return;

    // Check if the current pointer is pointing to a file and then do required operations
    if(files[index].isFile()) {

      /* Check if size of list of biggest files is less than 10
       * If yes than add the current file to list */
      if(sizeList<10) {
        Biggest10[sizeList]=files[index];
        sizeList++;
      }
      /* Else find the file with minimum size in the list and if it is smaller than the current file
       * than add the current file in it's place */
      else {

        minI = getMinIndex();

        if(files[index].length()>Biggest10[minI].length()) {
          Biggest10[minI]=files[index];
        }
      }

    }

    // Check if the current pointer is pointing to a sub-directories
    else if(files[index].isDirectory()) {

      // Recursively search sub-directories
      try {
        RecursiveSearch(files[index].listFiles(), 0, ++level);
      }
      catch (NullPointerException e) {

      }
    }

    // Recursively search main directory
    try {
      RecursiveSearch(files, ++index, level);
    }
    catch (NullPointerException e) {

    }
  }

  public static void main(String[] args) throws IOException {

    // Code for Listing 10 biggest files in the given directory \\

    // Path for directory(change manually if needed)
    boolean isWin=false;
    String os_name = System.getProperty("os.name").toLowerCase();
    String dirPath;

    if(os_name.indexOf("win")>=0)
      isWin = true;

    if(isWin ==true)
      dirPath = "C:\\Users\\";
    else
      dirPath = "/home/";

    // File object
    File dir = new File(dirPath);

    if(dir.exists() && dir.isDirectory()) {
      /* Arrays for files and sub-directories 
       * Of directory pointed by dir */
      File files[] = dir.listFiles();

      System.out.println();

      // Calling recursive method
      RecursiveSearch(files, 0, 0);

      int i;
      // Sort the list of files(Biggest10)
      mergeSort(Biggest10, 0, sizeList-1);

      // Display the 10 biggest files or lesser and tell if no files found
      if(sizeList==0) {
        System.out.println("No Files in the directory");
      }

      else {
        System.out.println(sizeList+" biggest files in the directory: ");
        for(i=sizeList-1; i>-1; i--) {

          if(Biggest10[i].length()/1048576 != 0)
            System.out.println("\t"+Biggest10[i].getName()+" :  "+Biggest10[i].length()/1048576+" MB");
          else if(Biggest10[i].length()/1024 != 0)
            System.out.println("\t"+Biggest10[i].getName()+" :  "+Biggest10[i].length()/1024+" KB");
          else
            System.out.println("\t"+Biggest10[i].getName()+" :  "+Biggest10[i].length()+" Bytes");
        }
      }
    }

    else
      System.out.println("No such Directory Exists");


    // Code for sorting and cleaning desktop \\

    System.out.print("Please enter user name: ");
    Scanner read = new Scanner(System.in);
    String user = read.nextLine();

    String desktopPath;
    String documentsPath;
    if(isWin ==true){
      desktopPath = "C:\\Users\\"+user+"\\Desktop";
      documentsPath = "C:\\Users\\"+user+"\\Documents";
    }
    else{ 
      desktopPath = "/home/"+user+"/Desktop/";
      documentsPath = "/home/"+user+"/Documents/";
      ;
    }

    read.close();
    // File object
    File deskPath = new File(desktopPath);

    if(deskPath.exists() && deskPath.isDirectory()) {

      // Arrays for files and sub-directories of directory pointed by deskPath
      File files[] = deskPath.listFiles();

      int i, j, sizefiles;
      String filNam;
      sizefiles=files.length;

      File docPath = new File(documentsPath);
      if(docPath.exists() && docPath.isDirectory()) {

        for(i=0; i<sizefiles; i++) {

          // Check if the current pointer is pointing to a file and then do required operations
          if(files[i].isFile()) {


            // Find the extension of the file
            filNam = files[i].getName();
            String extension = filNam;
            j = extension.lastIndexOf('.');
            if (j >= 0) {
              extension = extension.substring(j+1);
              extension = extension.toUpperCase();
            }
            else{
              extension = "NO_EXTN";
            }

            // If the file is a shortcut continue
            if(extension.equals("lnk") || extension.equals("url") || extension.equals("exe"))
              continue;
            else {
              File fileExtnDir;
              if(isWin ==true){
                fileExtnDir = new File(documentsPath+"\\"+extension);
                /* Look for the file type in the documents folder if a folder with the name of extension of the file
                 * does not exist in documents make a new folder with that name and add the file to it */
                if(!fileExtnDir.exists()) {
                  try{
                    fileExtnDir.mkdirs();

                    Files.move(Paths.get(desktopPath+"\\"+filNam),
                    Paths.get(documentsPath+"\\"+extension+"\\"+filNam));
                  }
                  catch(SecurityException se){
                    System.out.println("Failed to create extension folder");
                  }
                }
                // Else move file the to it
                else
                try {
                  Files.move(Paths.get(desktopPath+"\\"+filNam),
                  Paths.get(documentsPath+"\\"+extension+"\\"+filNam));
                }
                catch(Exception es){
                }
              }


              else{
                fileExtnDir = new File(documentsPath+"/"+extension);
                /* Look for the file type in the documents folder if a folder with the name of extension of the file
                 * does not exist in documents make a new folder with that name and add the file to it */
                if(!fileExtnDir.exists()) {
                  try{
                    fileExtnDir.mkdirs();
                    Files.move(Paths.get(desktopPath+"/"+filNam),
                    Paths.get(documentsPath+"/"+extension+"/"+filNam));
                  }
                  catch(SecurityException se){
                    System.out.println("Failed to create extension folder");
                  }
                }
                // Else move file the to it
                else
                try {
                  Files.move(Paths.get(desktopPath+"/"+filNam),
                  Paths.get(documentsPath+"/"+extension+"/"+filNam));
                }
                catch(Exception es){
                }
              }
            }

          }
        }

      }
    }

  }

  public static void merge(File files[], int initial, int mid, int end) {

    int i, j, k, n1 = mid - initial + 1, n2 =  end - mid;
    File L[]=new File[n1], R[]=new File[n2];

    for (i = 0; i < n1; i++)
      L[i] = files[initial + i];
    for (j = 0; j < n2; j++)
      R[j] = files[mid + 1+ j];

    i = 0;
    j = 0;
    k = initial;

    while (i < n1 && j < n2) {
      if (L[i].length() <= R[j].length()) {
        files[k] = L[i];
        i++;
      }
      else {
        files[k] = R[j];
        j++;
      }
      k++;
    }

    while (i < n1) {
      files[k] = L[i];
      i++;
      k++;
    }

    while (j < n2) {
      files[k] = R[j];
      j++;
      k++;
    }
  }

  public static void mergeSort(File files[], int left, int right) {

    if (left < right) {
      int mid = left+(right-left)/2;

      mergeSort(files, left, mid);
      mergeSort(files, mid+1, right);

      merge(files, left, mid, right);
    }
  }

}

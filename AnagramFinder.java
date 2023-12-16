import java.io.*;
import java.util.Iterator;

/**
 * Command line anagram finder program.
 * @author Rhys Talley
 * @version 1.0 December 16th, 2023
 */
public class AnagramFinder {

    /**
     * Populates a MyMap object reference with values from the inputted BufferedReader.
     * @param in a BufferedReader over the file inputted in the command line
     * @param map a MyMap object reference for either BSTMap, AVLTreeMap, or MyHashMap
     * @throws IOException
     */
    private static void processFile(BufferedReader in, MyMap<String, MyList<String>> map) throws IOException {
        // https://www.geeksforgeeks.org/java-program-to-read-a-file-to-string/
        String str, sorted;
        MyList<String> l;
        while((str = in.readLine()) != null){ // iterates through all lines in the text file and ensures that the current line is not null (i.e. we haven't reached the end of the file)
            sorted = insertionSort(str.toLowerCase());
            if((l = map.get(sorted))!=null){ // checks to see if the sorted, lowercase key is already present in the table
                l.add(str.strip());
            }
            else{
                l = new MyLinkedList<>(); // if the key is not in the table, we create a new entry
                l.add(str.strip());
                map.put(sorted,l);
            }
        }
    }

    /**
     * Modified insertion sort algorithm for sorting a String lexicographically.
     * @param s the string to be sorted
     * @return the sorted version of the string
     */
    private static String insertionSort(String s){
        // https://www.freecodecamp.org/news/string-to-array-in-java-how-to-convert-a-string-to-an-array-in-java/
        char[] c = s.toCharArray();
        for (int i = 1; i < c.length; ++i) {
            char current = c[i];
            int k;
            for (k = i - 1; k >= 0 && c[k] >= current; --k) { // we can directly compare chars here because they are compared based on their ASCII decimal equivalents.
                c[k + 1] = c[k];
            }
            c[k + 1] = current;
        }
        return new String(c);
    }

    /**
     * Modified insertion sort algorithm for sorting an
     * array of Strings lexicographically and in-place.
     * @param s an array of Strings
     */
    private static void insertionSort(String[] s){
        for (int i = 1; i < s.length; ++i) {
            String current = s[i];
            int k;
            for (k = i - 1; k >= 0 && s[k].compareTo(current)>=0; --k) { // must check that k is a valid index and, if it is, that s[k] is still a "larger" string than current
                s[k + 1] = s[k];
            }
            s[k + 1] = current; // as soon as those conditions aren't met, current is inserted in its proper position
        }
    }

    /**
     * Given map, a populated MyMap object reference, and a target word, prints all anagrams
     * of that word found in the map lexicographically, or prints "No anagrams found" if there
     * are no anagrams.
     * @param map a pre-populated MyMap object reference
     * @param word the target word to find anagrams for
     */
    private static void findAnagrams(MyMap<String, MyList<String>> map, String word){
        String key = insertionSort(word.toLowerCase());
        MyList<String> l = map.get(key);
        if(l==null || (l.size()==1 && l.get(0).equals(word))){ // checks to see if the list is null (in which case there would be no anagrams) or if there is only one value in that list and the value is the word itself
            System.out.println("No anagrams found.");
        }
        else{
            String[] words = new String[l.size()];
            int i=0;
            Iterator<String> iter = l.iterator();
            while(iter.hasNext()){
                String val = iter.next();
                if(!val.equals(word)){ // make sure that the current val is not the word itself
                    words[i++] = val;
                }
                else{ // however, if the word is contained in the list, we have to decrease the size of words[] sp we can properly sort/iterate through it
                    String[] words2 = new String[l.size()-1]; // create a new array with one smaller size
                    System.arraycopy(words,0,words2,0,i); // then use built in System.arraycopy() to copy over all values
                    words = words2; // reassign words to be this new array
                }
            }
            insertionSort(words);
            for(String s1:words){
                System.out.println(s1);
            }
        }
    }

    public static void main(String[] args){
        try{
            if(args.length!=3){
                throw new IllegalArgumentException();
            }
            // https://www.geeksforgeeks.org/java-program-to-read-a-file-to-string/
            BufferedReader in = new BufferedReader(new FileReader(args[1]));

            if(args[2].equals("bst")){
                MyMap<String, MyList<String>> map = new BSTMap<>();
                processFile(in,map);
                findAnagrams(map,args[0]);
            }
            else if(args[2].equals("avl")){
                MyMap<String, MyList<String>> map = new AVLTreeMap<>();
                processFile(in,map);
                findAnagrams(map,args[0]);
            }
            else if(args[2].equals("hash")){
                MyMap<String, MyList<String>> map = new MyHashMap<>();
                processFile(in,map);
                findAnagrams(map,args[0]);
            }
            else{
                throw new NoSuchFieldException();
            }
        }
        catch(IllegalArgumentException e){
            System.err.println("Usage: java AnagramFinder <word> <dictionary file> <bst|avl|hash>");
            System.exit(1);
        }
        catch(FileNotFoundException e){
            System.err.println("Error: Cannot open file '" + args[1] + "' for input.");
            System.exit(1);
        }
        catch(NoSuchFieldException e){
            System.err.println("Error: Invalid data structure '" + args[2] + "' received.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Error: An I/O error occurred reading '" + args[1] + "'.");
            System.exit(1);
        }
    }
}


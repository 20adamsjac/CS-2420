import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class PoetryMain {
    public static void main(String[] args) {
        WritePoetry poem = new WritePoetry();

        System.out.println(poem.WritePoem("green.txt", "sam", 20, true));
        System.out.println(poem.WritePoem("Lester.txt", "lester", 30, true));
        System.out.println(poem.WritePoem("HowMany.txt", "how", 30, false));
        System.out.println(poem.WritePoem("Zebra.txt", "are", 50, true));
    }

    static class WritePoetry {
        public HashTable<String, WordFreqInfo> store;
        public ArrayList<String> punct;     //defines what is considered punctuation

        /**
         * Constructor for WritePoetry class
         */
        public WritePoetry() {
            this.store = new HashTable<>();
            this.punct = new ArrayList<>();
            punct.add("?");
            punct.add(".");
            punct.add("!");
            punct.add(",");
        }

        /**
         * Clears the HashTable item of any data
         */
        private void clearStorage() {
            store.makeEmpty();
        }

        /**
         * Writes a poem based off of a given poem
         * @param file the name of the poem file
         * @param word the starting word for the poem
         * @param count the number of words in the poem
         * @param x whether to print the hashtable
         * @return the poem as a string
         */
        public String WritePoem(String file, String word, int count, boolean x) {
            clearStorage();             //clear the hashtable
            String poem = word + " ";   //add the first word to the poem
            readPoem(file);             //fill the hashtable with the word information

            if( !store.contains(word) ){
                System.out.println("ERROR OCCURRED: first word not in word data");
                return null;            //if the first word is not in the data return an error
            }

            WordFreqInfo next = store.retrieveData(word);   //retrieve the word data for the first word
            String nextWord = next.pickNextWord();          //generate a new word
            while (!store.contains(nextWord)){
                nextWord = next.pickNextWord();             //ensure a word is picked
            }

            for( int i = 0 ; i<count ; i++ ){
                poem = poem.concat( nextWord + " " );       //add the next word to the poem
                if( punct.contains(nextWord) ){
                    poem+="\n";                             //if the new word is punctuation, end the line
                }
                next = store.retrieveData(nextWord);        //retrieve the data of the current word
                nextWord = next.pickNextWord();             //generate a new word
                while (!store.contains(nextWord)){
                    nextWord = next.pickNextWord();         //ensure new word is picked
                }
            }
            poem = poem.concat(".\n\n");                    //once word limit is hit, add a period and end lines

            if (x){
                poem = poem+store.toString();               //if the hashtable should be printed, add it to the poem
            }

            return poem;            //return the poem
        }

        /**
         * Reads the input file and stores the word information into the hashtable
         * @param file the name of the input file
         */
        private void readPoem(String file) {
            ArrayList<String> words = generateArray(file);          //create the array with the words from the poem

            for (int i = 0; i + 1 < words.size(); i++) {
                String curr = words.get(i);
                if(curr.charAt(0) >= 'A' && curr.charAt(0) <= 'Z'){
                    curr = (char)(curr.charAt(0)+32)+curr.substring(1); //ensure that the words are not capitalized
                }
                if( i+1 == words.size( )-1 ){
                    if( store.contains( curr ) ){
                        WordFreqInfo next = store.retrieveData( curr );     //if it is the last word and the data already
                        next.updateFollows( "." );                          //exists update the follow information
                    }
                    else{
                        WordFreqInfo next = new WordFreqInfo( curr,1 );
                        if( !store.insert( curr, next ) ) {
                            System.out.println( "Error occurred while adding new hash entry." );
                        }
                        next.updateFollows( "." );                          //if it doesn't already exist, create it
                    }
                }
                else {
                    if (store.contains(curr)) {
                        WordFreqInfo next = store.retrieveData(curr);
                        next.updateFollows(words.get(i + 1));             //if the word exists, update the follow list
                    } else {
                        WordFreqInfo next = new WordFreqInfo(curr, 0);
                        if (!store.insert(curr, next)) {
                            System.out.println("Error occurred while adding new hash entry.");
                        }
                        next.updateFollows(words.get(i + 1));           //if it doesn't exist, create it
                    }
                }
            }
        }

        /**
         * Generates an in-order array of all the words in the input file in order
         * @param file the name of the input file
         * @return an arraylist containing all the words of the input poem
         */
        private ArrayList<String> generateArray( String file ) {
            try {
                File obj = new File( file );
                Scanner read = new Scanner( obj );      //open reading from the file
                String curr;
                ArrayList<String> words = new ArrayList<>( );

                while ( read.hasNextLine( ) ) {
                    int beginIndex = 0;
                    String x = read.nextLine( );        //put the next line of the poem into x
                    for (int i = 0; i < x.length( ); i++) {

                        switch ( x.charAt( i ) ) {
                            case '!', ',', '?', '.':
                                words.add( x.substring( i, i + 1 ) );   //if the char is punctuation, add it to the list
                                beginIndex+=2;                          // and change the indices properly
                                i+=2;
                                break;
                            case ' ', '\n':
                                curr = x.substring( beginIndex, i );    //if the char is a space or null character
                                if(( curr.charAt( 0 ) >= 'A' ) && ( curr.charAt( 0 ) <= 'Z' )){
                                    curr = (char)( curr.charAt( 0 ) + 32 ) + curr.substring( 1 );   //make word lowercase
                                }
                                words.add( curr );                      //add the word to the list
                                beginIndex = i + 1;                     //change the begin index
                        }

                        if ( i == x.length( ) - 1 ) {
                            words.add( x.substring( beginIndex, i + 1 ) ); //add the last word in the line
                        }
                    }
                }
                read.close( );              //stop reading from the file
                return words;               //return the array of strings
            }
            catch ( FileNotFoundException e ) {             //if the file is not found catch the error
                e.printStackTrace( );
                System.out.print( "Error, could not find file" );
                return null;
            }
        }

    }
}

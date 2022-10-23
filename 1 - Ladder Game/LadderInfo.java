/**
 * This class creates WordInfo objects which allows the program
 * to keep track of words, their ladder, and number of moves previous to the word.
 */
public class LadderInfo {
    public String word;
    public int moves;
    public String ladder;
    public LadderInfo next;

    public LadderInfo(String word, int moves, String ladder){
        this.word = word;
        this.moves = moves;
        this.ladder = ladder;
        this.next = null;
    }

    public LadderInfo(String word){
        this.word = word;
        this.moves = 0;
        this.ladder = word;
        this.next = null;
    }

    public String toString(){
        return "Word " + word    + " Moves " +moves  + " Ladder ["+ ladder +"]";
    }

}


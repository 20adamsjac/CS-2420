/**
 * This class creates WordInfo objects which allows the program
 * to keep track of words, their ladder, and number of moves previous to the word.
 */
public class LadderInfo implements Comparable<LadderInfo> {
    public String word;
    public int moves;
    public int score;
    public String ladder;
    public LadderInfo next;

    public LadderInfo(String word, int moves, String ladder){
        this.score = 1000;
        this.word = word;
        this.moves = moves;
        this.ladder = ladder;
        this.next = null;
    }

    public LadderInfo(String word, int moves, String ladder, int score){
        this.score = score;
        this.word = word;
        this.moves = moves;
        this.ladder = ladder;
        this.next = null;
    }

    public LadderInfo(String word){
        this.score = 1000;
        this.word = word;
        this.moves = 0;
        this.ladder = word;
        this.next = null;
    }

    public String toString(){
        return "Word " + word    + " Moves " +moves  + " Ladder ["+ ladder +"]";
    }

    @Override
    public int compareTo(LadderInfo o) {
        if (this.score > o.score){
            return 1;
        }
        if (this.score < o.score){
            return -1;
        }
        return 0;
    }
}


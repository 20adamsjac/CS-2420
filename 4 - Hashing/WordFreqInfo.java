import java.util.*;

public class WordFreqInfo {
    public String word;
    public int occurCt;
    ArrayList<Freq> followList;

    public WordFreqInfo(String word, int count) {
        this.word = word;
        this.occurCt = count;
        this.followList = new ArrayList<Freq>();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Word :" + word+":");
        sb.append(" (" + occurCt + ") : ");
        for (Freq f : followList)
            sb.append(f.toString());

        return sb.toString();
    }

    /**
     * Generates a random word from the follow list
     * @return the randomly generated word
     */
    public String pickNextWord(){
        Random rand = new Random( );                    //create a random type
        int random = rand.nextInt( this.occurCt )+1;    //generate a new number between 0 and occurCt

        for(int j = 0; j < followList.size() ; j++){
            random -= followList.get( j ).followCt;     //subtract the follow count of the given word from random
            if(random <= 0){
                return followList.get( j ).follow;      //if random is less than or equal to zero return that word
            }
        }
        return followList.get(followList.size()-1).follow;      //if random gets out of bounds return the last word
    }

    public void updateFollows(String follow) {
       //System.out.println("updateFollows " + word + " " + follow);
        occurCt++;
        for (Freq f : followList) {
            if (follow.compareTo(f.follow) == 0) {
                f.followCt++;
                return;
            }
        }
        followList.add(new Freq(follow, 1));
    }

    public static class Freq {
        String follow;
        int followCt;

        public Freq(String follow, int ct) {
            this.follow = follow;
            this.followCt = ct;
        }

        public String toString() {
            return follow + " [" + followCt + "] ";
        }

        public boolean equals(Freq f2) {
            return this.follow.equals(f2.follow);
        }
    }


}


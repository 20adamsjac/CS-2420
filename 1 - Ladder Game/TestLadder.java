/**
 * TestLadder is the main class where a LadderGame is played using different word inputs. The play function can be
 * called given a starting and ending words or an integer which will lead to random words being selected. It also
 * prints out the elapsed time for the game.
 */
public class TestLadder {
    public static void main(String[] args) {
        String[] source ={"toes","oops", "ride", "happily", "slow", "stone", "biff"};
        String[] dest   ={"toed", "tots", "ands", "angrily", "fast", "money", "axal"};

        LadderGame g = new LadderGame("dictionary.txt");
        for (int i=0; i < source.length; i++){
            g.play(source[i], dest[i]);
        }

      //  int RANDOMCT = 7;
        //for (int i = 3; i < RANDOMCT; i++)
          //  g.play(i);

    }
}
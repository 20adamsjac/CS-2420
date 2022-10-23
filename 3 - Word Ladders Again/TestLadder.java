/**
 * TestLadder is the main class where a LadderGame is played using different word inputs. The play function can be
 * called given a starting and ending words or an integer which will lead to random words being selected. It also
 * prints out the elapsed time for the game.
 */
public class TestLadder {
    public static void main(String[] args) {
        String[] source ={"oops", "slow", "kiss", "cock", "jura", "stet", "rums", "stylus", "herded", "gusts"};
        String[] dest   ={"tots", "fast", "woof", "numb", "such", "whey", "numb", "swives", "raffia", "fells"};

        Priority g = new Priority("dictionary.txt");
        for (int i=0; i < source.length; i++){
            g.play(source[i], dest[i]);
        }

//      int RANDOMCT = 9;
//        for (int i = 0; i < RANDOMCT; i++)
//           g.play(5);
//
    }
}
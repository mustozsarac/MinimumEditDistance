import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class medAlgorithm1 {

    private static String readFileAsString(String fileName) throws IOException {
		StringBuffer text = new StringBuffer();
		try {
			File fileDir = new File(fileName);
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), "ISO-8859-1"));
			String line;
			while ((line = in.readLine()) != null) {
				text.append(line + "\r\n");
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return text.toString();
    }

    private static int min(int x, int y, int z) {
        if (x <= y && x <= z)
            return x;
        if (y <= x && y <= z)
            return y;
        else
            return z;
    }

    private static int editDistDP(String str1, String str2, int m, int n) {
        int dp[][] = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++) {
            for (int j = 0; j <= n; j++) {
                if (i == 0)
                    dp[i][j] = j;
                else if (j == 0)
                    dp[i][j] = i;
                else if (str1.charAt(i - 1) == str2.charAt(j - 1))
                    dp[i][j] = dp[i - 1][j - 1];
                else
                    dp[i][j] = 1 + min(dp[i][j - 1], dp[i - 1][j], dp[i - 1][j - 1]);
            }
        }
        return dp[m][n];
    }


    private static Word[] differentWords(String[] _tokens, String _userWord) {
        Word[] differentWords = new Word[_tokens.length];
        for (int i = 0; i < _tokens.length; i++) {
            differentWords[i] = new Word(_tokens[i],
                    editDistDP(_tokens[i], _userWord, _tokens[i].length(), _userWord.length()));
        }
        return differentWords;
    }

    public static void main(String[] args) throws IOException {
        GUI gui = new GUI();
        String context = " ";
        context = readFileAsString(System.getProperty("user.dir") + "/sozluk.txt");
	    context = context.replace('ý', 'ı');//replace some letters with appropriate Turkish letters.
	    context = context.replace('þ', 'ş');
	    context = context.replace('ð', 'ğ');
        String[] tokens = context.split("\\r\\n");//split the context into the tokens.
        
        gui.myWord.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void removeUpdate(DocumentEvent e) {
                list(tokens, gui.myWord.getText().toLowerCase(), gui);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                list(tokens, gui.myWord.getText().toLowerCase(), gui);
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
                list(tokens, gui.myWord.getText().toLowerCase(), gui);
            }
        });
    }

    private static void list(String[] tokens, String userWord, GUI gui) {
        long startTime = System.currentTimeMillis();//Start the timer.//

        Word[] differentWords = differentWords(tokens, userWord);//get words and find distance.
        Arrays.sort(differentWords);//sort by distance.
        gui.text.setText("Top 5 closest word: \n");
        for (int i = 0; i < 5; i++) {
            gui.text.append((i+1) + "-) " + differentWords[i].name + " --> " + differentWords[i].distance + "\n");
        }
        gui.text.append("time passed: " + (System.currentTimeMillis() - startTime) + " milli seconds\n");//End the timer.
                                                                                                          
    }
}

class Word implements Comparable<Word> {
    public String name;
    public int distance;

    public Word(String _name, int _distance) {
        name = _name;
        distance = _distance;
    }

    @Override
    public int compareTo(Word w) {
        if (this.distance != w.distance) {
            return this.distance - w.distance;
        }
        return 0;
    }
};

class GUI {
    JFrame frame;
    JTextArea text;
    JTextField myWord;
    JLabel l1, l2;

    GUI() {
        frame = new JFrame();// creating instance of JFrame
        frame.setTitle("Mustafa Ozsarac - 2016510086 - Part 1");
        
        l1 = new JLabel();
	    l1.setBounds(10, 30, 270, 50);
	    l1.setForeground(Color.RED);
	    l1.setText("Please enter a Turkish word:" );
	    frame.add(l1);
	    
        l2 = new JLabel();
	    l2.setBounds(10, 110, 270, 50);
	    l2.setForeground(Color.RED);
	    l2.setText("Calculated words and distance:" );
	    frame.add(l2);

        text = new JTextArea();
        text.setBounds(200, 90, 270, 125);
        text.setEditable(false);
        frame.add(text);

        myWord = new JTextField("");
        myWord.setBounds(200, 30, 270, 50);
        frame.add(myWord);

        frame.setSize(500, 275);// 350 width and 250 height
        frame.setLayout(null);// using no layout managers
        frame.setVisible(true);// making the frame visible
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}

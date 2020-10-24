import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class medAlgorithm2 {
	static String word1 = "";
	static String word2 = "";
	static Stack<String> stack = new Stack<String>();//for backtracing.
	
    private static int min(int x, int y, int z) {
        if (x <= y && x <= z)
            return x;
        if (y <= x && y <= z)
            return y;
        else
            return z;
    }
    
    public static void backTrace(int[][] dp, String word1, String word2, int m, int n) {//doing backtracing.
    	int curPosX = m; int curPosY = n;
    	while(curPosX != 0 || curPosY != 0) {
    		int cc = dp[curPosX][curPosY];
    		if(curPosY - 1 < 0) {
    			stack.push("Delete '" + word1.charAt(curPosX - 1) + "'");
    			curPosX --;
    			continue;
    		}
    		else if(curPosX - 1 < 0) {
    			stack.push("Insert '" + word2.charAt(curPosY - 1) + "'");
    			curPosY --;
    			continue;
    		}
    		
            int cc_L = dp[curPosX][curPosY - 1];
            int cc_U = dp[curPosX - 1][ curPosY];
            int cc_D = dp[curPosX - 1][ curPosY - 1];
            
            if ((cc_D <= cc_L && cc_D <= cc_U) && (cc_D == cc - 1 || cc_D == cc))
            {
                if (cc_D == cc - 1)
                {
                    stack.push("Substitute '" + word1.charAt(curPosX - 1) + "' by '" + word2.charAt(curPosY - 1) + "'");
                    curPosX--;
                    curPosY--;
                }
                else
                {
                    stack.push("Keep '" + word1.charAt(curPosX - 1) + "'" + " (Zero cost.)");
                    curPosX--;
                    curPosY--;
                }
            }
            else if (cc_L <= cc_D && cc_L == cc - 1)
            {
                stack.push("Insert '" + word2.charAt(curPosY - 1) + "'");
                curPosY--;                   
            }
            else
            {
                stack.push("Delete '" + word1.charAt(curPosX - 1) + "'");
                curPosX--;                   
            }
    		
    	}

    	
    	
    }
	

	    private static int editDistDP(String str1, String str2, int m, int n, GUI gui) {
	        int dp[][] = new int[m + 1][n + 1];
	        for (int i = 0; i <= m; i++) {
	            for (int j = 0; j <= n; j++) {
	                if (i == 0) {//first word empty,
	                    dp[i][j] = j;//use insertion n times.
	                }
	                else if (j == 0) {//second word empty,
	                    dp[i][j] = i;//use deletion n times.
	                }
	                else if (str1.charAt(i - 1) == str2.charAt(j - 1)) {//if last chars are same, divide the problem.
	                    dp[i][j] = dp[i - 1][j - 1];
	                }
	                else {
	                	dp[i][j] = 1 + min(dp[i][j - 1], dp[i - 1][j], dp[i - 1][j - 1]);
	                }
	            }
	        }
	        
	        //print the table.
	        gui.text.setText("Table:(as in first word vertical, second word horizantal.) \n");        
			for (int i = 0; i < m + 1; i++)
			{
				for (int j = 0; j < n + 1; j++)
				{
					gui.text.append(dp[i][j] + " ");
				}
				gui.text.append("\n");
				
			}
			gui.text.append("\n");
			backTrace(dp,str1,str2,m,n);//do backtracing for path.
			
			//return the distance.
	        return dp[m][n];
	    }


	    public static void main(String[] args) throws IOException {
	        GUI gui = new GUI();

	        gui.myWord1.getDocument().addDocumentListener(new DocumentListener() {
	            @Override
	            public void removeUpdate(DocumentEvent e) {
	                word1 = gui.myWord1.getText().toLowerCase();list(word1,word2,gui);
	            }

	            @Override
	            public void insertUpdate(DocumentEvent e) {
	            	word1 = gui.myWord1.getText().toLowerCase();list(word1,word2,gui);
	            }

	            @Override
	            public void changedUpdate(DocumentEvent arg0) {
	            	word1 = gui.myWord1.getText().toLowerCase();list(word1,word2,gui);
	            }
	        });
	        
	        gui.myWord2.getDocument().addDocumentListener(new DocumentListener() {
	        	
	            @Override
	            public void removeUpdate(DocumentEvent e) {
	                word2 = gui.myWord2.getText().toLowerCase();list(word1,word2,gui);
	            }

	            @Override
	            public void insertUpdate(DocumentEvent e) {
	            	word2 = gui.myWord2.getText().toLowerCase();list(word1,word2,gui);
	            }

	            @Override
	            public void changedUpdate(DocumentEvent arg0) {
	            	word2 = gui.myWord2.getText().toLowerCase();list(word1,word2,gui);
	            }
	        });
	        
	        
	    }

	    private static void list(String word1, String word2, GUI gui) {
	        long startTime = System.currentTimeMillis();//Start the timer.//
	        int distance = editDistDP(word1, word2, word1.length(), word2.length(), gui);//get words and find distance.
	        gui.text.append("Minimum edit distance between " + word1 + " and " + word2 +" :" +"\n");
	        gui.text.append(distance + "\n\nUsed operations on first word: \n");
	        while(!stack.isEmpty()) {
	        	gui.text.append(stack.pop() + "\n");
	        }
	        gui.text.append("\n");
	        gui.text.append("time passed: " + (System.currentTimeMillis() - startTime) + " milliseconds\n");//End the timer.
	                                                                                                          
	    }
	}


	class GUI {
	    JFrame frame;
	    JTextArea text;
	    JTextField myWord1,myWord2;
	    JLabel l1, l2;

	    GUI() {
	        frame = new JFrame();// creating instance of JFrame
	        frame.setTitle("Mustafa Ozsarac - 2016510086 - Part 2");
	        
	        l1 = new JLabel();
		    l1.setBounds(10, 30, 270, 50);
		    l1.setForeground(Color.RED);
		    l1.setText("Please enter first Turkish word:" );
		    frame.add(l1);
		    
	        myWord1 = new JTextField("");
	        myWord1.setBounds(250, 30, 270, 50);
	        frame.add(myWord1);
		    
	        l2 = new JLabel();
		    l2.setBounds(10, 90, 270, 50);
		    l2.setForeground(Color.RED);
		    l2.setText("Please enter second Turkish word:" );
		    frame.add(l2);
		    
	        myWord2 = new JTextField("");
	        myWord2.setBounds(250, 90, 270, 50);
	        frame.add(myWord2);

	        text = new JTextArea();
	        text.setBounds(10, 160, 510, 600);
	        text.setEditable(false);
	        frame.add(text);



	        frame.setSize(550, 800);// 550 width and 800 height
	        frame.setLayout(null);// using no layout managers
	        frame.setVisible(true);// making the frame visible
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    }

}

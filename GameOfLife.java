/* Siya Chandrashekar , Dillon Bevan 
  5/2/2021
  Project 3 */

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;
import java.util.Hashtable;
import java.util.Timer;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
import java.util.concurrent.TimeUnit;


public class GameOfLife  extends TimerTask implements ActionListener{
	// Declare the variables used
    JButton startstopToggle, reset, sizeUp, sizeDown, colorToggle;
    JPanel menuPanel;
    JPanel gamePanel;
    JLabel[][] gridMap;
    JFrame settingsMenu;
    JSlider density;
    JLabel stepCount;
    Color cellColor;
    Color deadColor;
    int stepCounter = 0;
    boolean[][] life;
    boolean runStatus = false;
    boolean startStopText = true;
    boolean tempStatus = false;
    boolean colors = true;
    int gridSize = 20;
    int neighborNum = 0;
    float densityVal = (float) 0.5;
    Timer timer;


    public static void main(String[] args) {
        new GameOfLife();
    }

    public GameOfLife() {
        initializeBoard();
    }

    public void initializeBoard() {
    	// Declare base cell color and background
    	cellColor = Color.black;
    	deadColor = Color.white;

    	// Create a timer
        timer = new Timer();
        timer.scheduleAtFixedRate(this, 0, 150);
        
        // Create the JFrame
        settingsMenu = new JFrame();

        settingsMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        settingsMenu.setLayout(new BorderLayout());
        settingsMenu.setSize(700, 700);

        // Create the area for the buttons
        menuPanel = new JPanel();

        // Create the buttons and sliders on the board
        startstopToggle = new JButton("Start");
        reset = new JButton("Reset");
        sizeUp = new JButton("Size up");
        sizeDown = new JButton("Size down");
        colorToggle = new JButton("Colors: ON");
        density = new JSlider();
        stepCount = new JLabel("Step Count: " + stepCounter);
        
        // Add the buttons to the menu
        menuPanel.add(startstopToggle);
        menuPanel.add(reset);
        menuPanel.add(density);
        menuPanel.add(stepCount);
        menuPanel.add(sizeUp);
        menuPanel.add(sizeDown);
        menuPanel.add(colorToggle);

        // Add action listeners to the buttons
        startstopToggle.addActionListener(this);
        reset.addActionListener((this));
        sizeDown.addActionListener((this));
        sizeUp.addActionListener(this);
        colorToggle.addActionListener(this);
        
        // Density flourish slider and settings
        density.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
         	   if(e.getSource().equals(density)){
         	   densityVal = (((JSlider)e.getSource()).getValue());
         	   densityVal /= 100;
         	   }
            }
        });
        density.setMinorTickSpacing(10);
        density.setMajorTickSpacing(50);
        density.setName("Density");
        density.setPaintTicks(true);
        density.setPaintLabels(true);
        
        // Density labels
        Hashtable<Integer, JLabel> position = new Hashtable<Integer, JLabel>();
        position.put(0, new JLabel("0%"));
        position.put(50, new JLabel("Density"));
        position.put(100, new JLabel("100%"));
        
        density.setLabelTable(position);
        
        // Create the grid layout for the menu
        menuPanel.setLayout(new GridLayout(2, 4));

        // Create the JPanel that has the actual game visuals default size 20 x 20
        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(gridSize, gridSize));
        gridMap = new JLabel[gridSize][gridSize];
        life = new boolean[gridSize][gridSize];

        // Creates JLabels for each individual cell
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                gridMap[i][j] = new JLabel();

                gamePanel.add(gridMap[i][j]);
                gridMap[i][j].setOpaque(true);
            }
        }

        // Establish the layout of our buttons on the game
        settingsMenu.add(menuPanel, BorderLayout.NORTH);
        settingsMenu.add(gamePanel, BorderLayout.CENTER);
        settingsMenu.setVisible(true);

        // Creates a random board with a certain density
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (Math.random() < densityVal) {
                    gridMap[i][j].setBackground(cellColor);

                } else {
                    gridMap[i][j].setBackground(deadColor);
                }
            }
        }
        gamePanel.setVisible(false);
        gamePanel.setVisible(true);

    }

    @Override
    public void run() {
    	// Refreshes the board with the timer and increases the number of steps
        if (runStatus) {
            boardUpdate();
            stepCounter++;
            stepCount.setText("Step Count: " + stepCounter);
        }
    }

    public void boardUpdate() {
    	// Game logic, counts the number of neighbors and determines if cells live or die
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                neighborNum = 0;
                for (int x = i - 1; x < i + 2; x++) {
                    for (int y = j - 1; y < j + 2; y++) {
                        try {
                            if (gridMap[x][y].getBackground().equals(cellColor)) {
                                if (!(x == i && y == j)) {
                                    neighborNum++;
                                }
                            }


                        } catch (Exception e) {

                        }
                    }
                }
                if (neighborNum == 3) {
                    life[i][j] = true;
                } else if (neighborNum < 2 || neighborNum > 3) {
                    life[i][j] = false;
                } else {
                    life[i][j] = gridMap[i][j].getBackground().equals(cellColor);
                }
            }

        }
        
        // Random colors, make it look cool flourish
	    if (colors) {
	        if(stepCounter % 6 == 0) {
	        	cellColor = Color.red;
	        }
	        else if(stepCounter % 6 == 1) {
	        	cellColor = Color.orange;
	        }
	        else if(stepCounter % 6 == 2) {
	        	cellColor = Color.yellow;
	        }
	        else if(stepCounter % 6 == 3) {
	        	cellColor = Color.green;
	        }
	        else if(stepCounter % 6 == 4) {
	        	cellColor = Color.blue;
	        }
	        else if(stepCounter % 6 == 5) {
	        	cellColor = Color.pink;
	        }
	    } else {
	    	cellColor = Color.black;
	    }
        
	    // Updates the cells on the board
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (life[i][j]) {
                    gridMap[i][j].setBackground(cellColor);
                } else {
                    gridMap[i][j].setBackground(Color.white);
                }
            }
        }

        gamePanel.setVisible(false);
        gamePanel.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	// Reset Button Function
        if (e.getSource().equals(reset)) {
            tempStatus = runStatus;

            if (runStatus == true) {
                runStatus = false;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException interruptedException) {

                }
            }
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    if (Math.random() < densityVal) {
                        gridMap[i][j].setBackground(cellColor);

                    } else {
                        gridMap[i][j].setBackground(deadColor);

                    }
                }
            }
            stepCounter = 0;
            stepCount.setText("Step Count: " + stepCounter);
            gamePanel.setVisible(false);
            gamePanel.setVisible(true);

            runStatus = tempStatus;
        }
        
        // Stop Start Button Function
        if (e.getSource().equals(startstopToggle)) {
            startStopText = !startStopText;
            if (startStopText) {
                startstopToggle.setText("Start");
                runStatus = false;
            } else {
                startstopToggle.setText("Stop");
                runStatus = true;
            }


        }
        
        // Color button Function
        if (e.getSource().equals(colorToggle)) {
            colors = !colors;
            if (colors) {
                colorToggle.setText("Colors: ON");
            } else {
                colorToggle.setText("Colors: OFF");
            }


        }
        // Size flourish, SIZE UP by 5 cells
        if (e.getSource().equals(sizeUp)) {
            tempStatus = runStatus;

            if (runStatus == true) {
                runStatus = false;

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException interruptedException) {

                }
            }
            stepCounter = 0;
            stepCount.setText("Step Count: " + stepCounter);

            gamePanel.removeAll();

            gridSize = gridSize + 5;

            gridMap = new JLabel[gridSize][gridSize];
            life = new boolean[gridSize][gridSize];
            gamePanel.setLayout(new GridLayout(gridSize, gridSize));

            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    gridMap[i][j] = new JLabel();

                    gamePanel.add(gridMap[i][j]);
                    gridMap[i][j].setOpaque(true);

                }
            }

            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    if (Math.random() < densityVal) {
                        gridMap[i][j].setBackground(cellColor);

                    } else {
                        gridMap[i][j].setBackground(deadColor);

                    }

                }
            }
            gamePanel.setVisible(false);
            gamePanel.setVisible((true));
            runStatus = tempStatus;
        }
        
        
        // Size flourish, SIZE DOWN by 5 cells
        if (e.getSource().equals(sizeDown) && gridSize > 5) {
            tempStatus = runStatus;
            if (runStatus == true) {
                runStatus = false;
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException interruptedException) {

                }
            }
            stepCounter = 0;
            stepCount.setText("Step Count: " + stepCounter);
            gamePanel.removeAll();

            gridSize = gridSize - 5;

            gridMap = new JLabel[gridSize][gridSize];
            life = new boolean[gridSize][gridSize];
            gamePanel.setLayout(new GridLayout(gridSize, gridSize));

            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    gridMap[i][j] = new JLabel();

                    gamePanel.add(gridMap[i][j]);
                    gridMap[i][j].setOpaque(true);

                }
            }

            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    if (Math.random() < densityVal) {
                        gridMap[i][j].setBackground(cellColor);

                    } else {
                        gridMap[i][j].setBackground(deadColor);

                    }

                }
            }
            gamePanel.setVisible(false);
            gamePanel.setVisible((true));
            runStatus = tempStatus;

        }

    }

}
   import javax.swing.*;
   import java.awt.*;
   import java.awt.image.BufferedImage;
   import java.io.*;
   import java.net.*;
   import javax.imageio.ImageIO;
   import java.applet.*;
   import java.awt.event.*;
   import java.io.IOException;

   public class Tron extends JFrame implements ActionListener {
   
   // panels for the menu layout
   
      private JPanel menu = new JPanel();
      private JPanel afterMenu = new JPanel(new GridLayout(3,1));
      private JPanel north = new JPanel();
      private JPanel center = new JPanel(new GridLayout(6, 1));
      private JPanel row1 = new JPanel();
      private JPanel row2 = new JPanel();
      private JPanel row3 = new JPanel();
      private JPanel south = new JPanel(new GridLayout(3, 1));
   
   // these are all the images that will get loaded from the path
      private String[] names = { "tron", "players", "1player", "2players",
         "difficulty", "easy", "hard", "speed", "normal", "fast", "restart", "mainmenu","play" };
   // this will hold all the images so they can be worked on
      private ImageIcon[] images = new ImageIcon[names.length];
   // these are the buttons holders so we can set them to black
      private JButton[] button = new JButton[names.length-5]; // there are 5 non button images
      private int j = 0; // jbutton index counter to work with them
      private JButton play; // holds play button
   
   // variables to be passed into game
      private GameConfig config = new GameConfig();
      
      //board variable
      private Board game;
   
   // Starts menu
      public Tron() throws IOException {
      
      // this loop loads the images names in the img[] array
         for (int i = 0; i < names.length; i++)
            images[i] = new ImageIcon((names[i] + ".JPG"));
      
      // this loop is for resizing the images
         for (int i = 0; i < names.length; i++) {
            if (i == 0) { // for TRON logo
               Image image = images[i].getImage();
               image = image.getScaledInstance(-1, 135, Image.SCALE_SMOOTH);
               images[i].setImage(image);
               north.add(new JLabel(images[i]));
            } 
            else if (i == names.length - 1) { // for PLAY button
               Image image = images[i].getImage();
               image = image.getScaledInstance(-1, 65, Image.SCALE_SMOOTH);
               images[i].setImage(image);
               play = new JButton(images[i]);
               play.setBorderPainted(false);
               play.addActionListener(this);
               play.setBackground(Color.black);
               south.add(play);
            } 
            else { // for all the other images
               Image imager = images[i].getImage();
               imager = imager.getScaledInstance(-1, 30, Image.SCALE_SMOOTH);
               images[i].setImage(imager);
            }
         }// end image resizing loop
      
      // this loop loads the images in the center panel rows
         //starts at 1 instead of 0 cause 0 is the logo
         //ends at length-1 cause that's the play button
         for (int i = 1; i < names.length - 1; i++) {
            if (i == 1)
               row1.add(new JLabel(images[i]));
            else if (i == 2 || i == 3) {
               buttonize(i);
               row1.add(button[j]);
               j++;
            } 
            else if (i == 4)
               row2.add(new JLabel(images[i]));
            else if (i == 5 || i == 6) {
               buttonize(i);
               row2.add(button[j]);
               j++;
            } 
            else if (i == 7)
               row3.add(new JLabel(images[i]));
            else if (i == 8 || i == 9) {
               buttonize(i);
               row3.add(button[j]);
               j++;
            }
            else if (i == 10 || i == 11) {
                buttonize(i);
                afterMenu.add(button[j]);
                j++;
             }
         }
      
      // add the rows to the center panel grids
         center.add(row1);
         center.add(row2);
         center.add(row3);
      //set row colors
         row1.setBackground(Color.black);
         row2.setBackground(Color.black);
         row3.setBackground(Color.black);
         
         //add afterMenu
         afterMenu.setPreferredSize(new Dimension(800, 800));
         afterMenu.setBackground(Color.black);
      
      // add panels to menu panel
         menu.add(north, BorderLayout.NORTH);
         menu.add(center, BorderLayout.CENTER);
         menu.add(south, BorderLayout.SOUTH);
      
      //size and color menu and panels
         menu.setPreferredSize(new Dimension(800, 800));
         menu.setBackground(Color.black);
         north.setBackground(Color.black);
         center.setBackground(Color.black);
         south.setBackground(Color.black);
      
      //add menu to JFrame
         add(menu);
         //add(afterMenu);
         afterMenu.setVisible(false);
         menu.requestFocus();
         pack();
      // window settings
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setBackground(Color.BLACK);
         setLocationRelativeTo(null);
         setTitle("Tron");
         setResizable(false);
         setVisible(true);
      //play music
         Sound.music();
      }
      
      public void endGame() throws IOException {
    	  game.setVisible(false);
          menu.setVisible(false);
          afterMenu.setVisible(true);
      }
   
   //method to make buttons
      public void buttonize(int i) {
         button[j] = new JButton(images[i]);
         button[j].setBorderPainted(false);
         button[j].addActionListener(this);
         button[j].setBackground(Color.black);
      }
   
   //runs game
      public void initGame() throws IOException {
         menu.setVisible(false);
         afterMenu.setVisible(false);
         game = new Board(800, 800, config);
         add(game); // have to add arguments to this once ready
         game.setPreferredSize(new Dimension(800, 800));
         pack();
      //request focus to use keyboard controls
         game.requestFocus();
      
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
         setLocationRelativeTo(null);
         setTitle("Tron");
         setResizable(false);
         setVisible(true);
      }
   
      public void actionPerformed(ActionEvent e) {
    	  //if play button is clicked
         if (e.getSource() == play) {
            try {
               initGame();
            } 
            catch (IOException e1) {
            // TODO Auto-generated catch block
               e1.printStackTrace();
            }
         
         } 
         else if (e.getSource() == button[0]) // 1player
            config.setPlayers(1);
         else if (e.getSource() == button[1]) // 2player
            config.setPlayers(2);
         else if (e.getSource() == button[2]) // Easy
            config.setDifficulty( 1);
         else if (e.getSource() == button[3]) // Hard
            config.setDifficulty(2);
         else if (e.getSource() == button[4]) // Normal
            config.setSpeed(1);
         else if (e.getSource() == button[5]) // Fast
            config.setSpeed(2);
         else if (e.getSource() == button[6]){ // Restart - DN
             try {
             initGame();
             }
             catch (IOException e1) {
            	 // TODO Auto-generated catch block
            	 e1.printStackTrace();
             }
         }
         else if (e.getSource() == button[7]){ // Main Menu - DN
             //kill board somehow when game over???
             menu.setVisible(true);
             afterMenu.setVisible(false); 
         }
      }
   
      public static void main(String[] args) throws IOException {
         new Tron();
      }
   }
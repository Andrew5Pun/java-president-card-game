package President;

/*Andrew P
 Dec 30, 2019
 PA_President_Summative
*/

import java.awt.*;
import hsa.Console;
import sun.audio.*; // allows audio
import javax.swing.*;
import java.awt.image.BufferedImage; //allows buffered images
import javax.imageio.*; // allows image loading
import java.io.*; // allows file access
import java.util.Random;

public class PA_President_Summative
{
    static Console c; // The output console
    static Graphics2D graphics; //Graphics2D
    static Random rand = new Random (); //random seed



    //debug options. Change to true/false to enable or disable or you can change it in the in-game settings
    static boolean showcards = false; //debug for showing other players' cards
    static boolean AIp1 = false; //debug to let player 1 be controlled by AI

    //below are some static directory variables. I did this to easily change the directory from Mac to Windows format. Also useful for changing directories later.

    static String rootdir = "President/";
    static String sounddir = "sounds/";
    static String cardsdir = "cards new/";
    static String backgrounddir = "Background/";

    public static void playAudio (String file) throws Exception //for .wav only
    {
        InputStream in = new FileInputStream (file);
        AudioStream as = new AudioStream (in);
        AudioPlayer.player.start (as);
    }

    public static void randomCardSound() throws Exception { //plays a random card sound
        int randomsound;
        randomsound = rand.nextInt(6) + 1;
        playAudio(rootdir + sounddir + randomsound + ".wav");
    }



    public static void errorSound() throws Exception { //play an error sound
        playAudio(rootdir + sounddir + "error.wav" );
    }

    public static String[] fill (String fname) throws IOException
    {

        FileReader fr = new FileReader (fname); //read the file name
        BufferedReader filein = new BufferedReader (fr); //initialize buffer

        String[] list = new String[100000]; //create new string that has an arbituary size
        String line;
        int counter = 0;
        while ((list[counter] = filein.readLine ()) != null) { //file has not ended
            counter++;
        }
        String newlist[] = new String[counter];
        for (int i = 0; i < counter; i++) {
            newlist[i] = list[i]; //put all items from list into newlist
        }

        filein.close (); // close file


        return newlist;
    }

    public static Image[] loadCardImages ()  // loads card png files into array
    {
        Image pictureDeck[] = new Image [52]; // create array of 52 card images

        for (int x = 0 ; x < 52 ; x++)
        {
            pictureDeck[x] = loadImage (rootdir + cardsdir + (x + 1) + ".png"); // loads appropriate image from cards2 folder
            pictureDeck[x] = pictureDeck[x].getScaledInstance(200, 265, Image.SCALE_DEFAULT); //scales cards to new sizes
        }

        return pictureDeck;
    }

    public static Image loadImage (String name)  //loads image from file
    {
        Image img = null;
        try
        {
            img = ImageIO.read (new File (name)); // load file into Image object
        }
        catch (IOException e)
        {
            System.out.println ("File not found");
        }

        return img;
    }

    public static int convert (String card) //convert string value of card to integer
    {
        int rank, suit;
        char rankChar, rankChar2 = 3, suitChar;
        if (card.equals("Pass") || card.equals("PASS") || card.equals("pass") || card.equals("p") || card.equals("P")) { //pass will be converted to int 500
            return 500; //500 signifies pass
        }

        if (card.length() < 2 || card.length() > 3) { // if the card length is less than 2 or greater than 3, return a large number signifying invalid
            return 100; //100 signifies error
        }
        if (card.length() == 2) {
            rankChar = card.charAt (0); // first character
            suitChar = card.charAt (1); // second character
        } else { //assume card length 3 for 10 of cards
            rankChar = card.charAt (0); // first character
            rankChar2 = card.charAt (1); // second character
            suitChar = card.charAt (2); // third character
        }

        if (rankChar >= '2' && rankChar <= '9')
            rank = (int) rankChar - 48 - 2; // convert char digit to integer, subtract 2 to get rank
        else if (rankChar == 't' || rankChar == 'T') //ten
            rank = 8;
        else if (rankChar == 'j' || rankChar == 'J') //jack
            rank = 9;
        else if (rankChar == 'q' || rankChar == 'Q') //queen
            rank = 10;
        else if (rankChar == 'k' || rankChar == 'K') //king
            rank = 11;
        else if (rankChar == 'a' || rankChar == 'A' || rankChar == '1') //ace
            rank = 12;
        else //if the character is something else, then it is invalid.
            rank = 100; //change rank to 100.

        if (rankChar == '1' && rankChar2 == '0') { //check if the char is 10
            rank = 8;
        }

        if(suitChar == 's' || suitChar == 'S') //spades
            suit = 0;
        else if(suitChar == 'h' || suitChar == 'H') //hearts
            suit = 1;
        else if(suitChar == 'c' || suitChar == 'C') //clubs
            suit = 2;
        else if(suitChar == 'd' || suitChar == 'D') //diamonds
            suit = 3;
        else //if the character is something else, then it is invalid
            suit = 4; //change suit to 4

        return suit * 13 + rank; // combine to get 0-51 value
    }

    public static int[] initDeck ()  // initialize standard deck
    {
        int deck[] = new int [52];
        for (int x = 0 ; x < deck.length ; x++)
            deck [x] = x; // cards represented by numbers 0-51

        return deck;
    }

    public static void shuffle (int deck[]) //shuffle cards
    {
        Random ran = new Random();
        int temp, randomindex; // initialize temporary variable and random index

        for (int i = 0; i < deck.length; i++) { // index through deck
            temp = deck[i]; // temporary variable is equal to deck
            randomindex = ran.nextInt(deck.length); // create a random number between 0 and 51
            deck[i] = deck[randomindex]; // set card at position to the card at random index
            deck[randomindex] = temp; // set the card at random index to temporary variable

        }
    }

    public static void deal (int deck[], int p1[], int p2[], int p3[], int p4[]) //deal cards
    {
        for (int i = 0; i < 13; i++) {
            p1[i] = deck[i+26]; //fill p1's hand
            p2[i] = deck[i+13]; //fill p2's hand with deck
            p3[i] = deck[i]; //fill p3's hand with deck
            p4[i] = deck[i+39]; //fill p4's hand with deck
        }
        //c.println(p1[12]);
    }

    public static void sort (int list[]) //sorting by suit
    {
        int temp;
        for (int x = 0 ; x < list.length - 1; x++) // sort first length-1 values
        {
            int lowPos = x; // assume first value is lowest
            for (int y = x + 1; y < list.length; y++) // check rest of list
                if (list [y] < list [lowPos]) // if you find a lower value
                    lowPos = y; // make it the lowest
            temp = list [x]; // swap low value with value in its proper position
            list [x] = list [lowPos];
            list [lowPos] = temp;
        }
    }

    public static void valuesort (int list[]) //sorting by value
    {
        int temp;
        for (int x = 0 ; x < list.length - 1; x++) // sort first length-1 values
        {
            int lowPos = x; // assume first value is lowest
            for (int y = x + 1; y < list.length; y++) {// check rest of list
                if (cardvalue(list[y]) < cardvalue(list [lowPos]) || (cardvalue(list[y]) == cardvalue(list[lowPos]) && suitvalue(list[y]) < suitvalue(list[lowPos]))) // if you find a lower value cardwise
                    lowPos = y; // make it the lowest
            }
            temp = list [x]; // swap low value with value in its proper position
            list [x] = list [lowPos];
            list [lowPos] = temp;
        }

    }


    public static int[] remove (int[] hand, int[] cards) //removes cards from hand
    {
        if (cards.length <= hand.length) { //check if cards length is less than hand length
            int copy[] = new int[hand.length];
            int counter = 0;
            for (int i = 0; i < hand.length; i++) {

                boolean cardneeded = false;
                for (int p = 0; p < cards.length; p++) { //checks to see if card at position i is any of the cards in the cards meant for removing
                    if (hand[i] == cards[p]) {

                        cardneeded = true; //if it finds a card that needs to be removed it will change cardneeded to true
                    }
                }
                if (cardneeded == false) { //if the card does not need to be removed then it is copied directly to the hand
                    copy[counter] = hand[i]; //copy hand to new hand
                    counter++;
                }
            }

            int newhand[] = new int[counter]; //make a new array with correct size

            for (int i = 0; i < newhand.length; i++) {
                newhand[i] = copy[i]; //copy all cards to deck
            }
            //c.print(counter);
            return newhand;
        }
        return hand;

    }

    public static int[] remove (int[] hand, int card) //overloaded method for single card
    {

        if (hand.length > 0 && contains(hand, card) == true) { //if the array does not have a size of zero, and it contains the card:
            int[] newhand = new int[hand.length - 1];

            int pos = cardat(hand, card); //find out where the card is
            int counter = 0; //intialize counter
            for (int i = 0; i < hand.length; i++) {
                if (i != pos) { //skip the element pos when copying
                    newhand[counter] = hand[i];
                    counter++;
                }

            }
            return newhand;
        }
        return hand;
    }


    public static boolean inputvalid (int[] hand, int[] card) throws Exception {
        boolean[] validarray = new boolean[card.length]; //make a boolean array
        for (int i = 0; i < hand.length; i++) { //go through the deck
            for (int p = 0; p < card.length; p++) { //check each element in the card array
                if (hand[i] == card[p]) {
                    validarray[p] = true; //if the card has been found change its index to true in the boolean array
                }
            }

        }
        for (int i = 0; i < card.length; i++) { //check each element in the boolean array
            if (validarray[i] != true) { //if one of the elements is not true, that means that there is an element in card that is not present in hand

                return false; //return false
            }
        }
        return true;
    }

    public static int cardvalue (int card) { //returns value of card
        int newvalue;
        newvalue = card % 13; //value of card is found by doing mod 13 on the card number
        return newvalue;
    }

    public static int suitvalue (int card) { //returns suit value of card
        int value;
        value = card / 13; //suit value of card is found by dividing by 13 on the card number

        int newvalue = 0;
        //the following if statements are due to the numbering system being different in president
        if (value == 0) newvalue = 3; //spades
        if (value == 1) newvalue = 2; //hearts
        if (value == 2) newvalue = 0; //clubs
        if (value == 3) newvalue = 1; //diamonds

        return newvalue;
    }

    public static boolean validPlay(int[] hand, int[] playedcards) { //method for figuring out if play is valid

        if (playedcards.length == 0) return true; //if there are no upcards, you can play anything you want.

        int val = cardvalue(hand[0]); //find value of first card
        if (val == 0) return true; //2s have a card value of 2. Thus, it will always return true because you can always play 2.

        if (0 > val || val > 51) { //if val is not between 51 and 0
            return false; //play is invalid
        }

        for (int i = 1; i < hand.length; i++) { //first check if multiple cards are the same
            if (cardvalue(hand[i]) != val) return false; //if the cards do not match in card value then the play is invalid

        }
        if (val <= cardvalue(playedcards[0])) return false; //if the val is less than or equal to the card value of playedcards then it is invalid

        return true; //valid play
    }

    public static int[] cardcount(int[] hand, int cardvalue, int num) { //a boolean method to return whether a hand has num amount cards of cardvalue value
        valuesort(hand); //sort the hand by value
        int[] list = new int[0]; //make an empty array
        int currentcardval = 600;
        int i = 0;
        while (i < hand.length && list.length < num) { //go through the hand and put all cards of that value into the array
            if (cardvalue(hand[i]) > cardvalue) {
                if (cardvalue(hand[i]) != currentcardval) { //if the card value is not the same, it must reset the list
                    list = new int[0]; //make an empty array
                    currentcardval = cardvalue(hand[i]); //set the wanted card value to the card
                }
                list = add(list, hand[i]); //add the card to list
            }
            i++;
        }
        return list;
    }

    public static int[] bestPlay(int hand[], int currentplay[]) { //method for AI to figure out what is the best play
        int newplay[] = new int[0]; //card array with planned cards to play
        if (hand.length > 0) { //make sure the player has cards.
            if (currentplay.length > 0) { //if current play is greater than zero
                int val = cardvalue(currentplay[0]); //find value of first card in upcards

                //for playing multiple cards
                int[] cardcount = cardcount(hand, val, currentplay.length); //make an array with the amount of cards
                if (cardcount.length >= currentplay.length) {
                    newplay = cardcount;
                    return newplay;
                }


                //for playing 1 card
                for (int i = 0; i < hand.length; i++) {
                    if (cardvalue(hand[i]) > val && currentplay.length == 1) { //play 1 card if there is just 1 card
                        newplay = add(newplay, hand[i]);
                        return newplay;
                    }
                    if (cardvalue(hand[i]) == 0 && hand.length < 2 && hand.length < 1) {
                        newplay = add(newplay, hand[i]);
                        return newplay;
                    }
                }

                for (int i = 0; i < hand.length; i++) {
                    if (cardvalue(hand[i]) == 0) {
                        newplay = add(newplay, hand[i]);
                        return newplay;
                    }
                }

            } else { //if there are no cards in currentplay just play any card
                Random ran = new Random();
                int random = ran.nextInt(hand.length); //choose a random index
                newplay = add(newplay, hand[random]); //choose a random card
                return newplay;
            }
        }
        newplay = add(newplay, 500);
        return newplay; //return array with 500 to signify pass
    }

    public static boolean contains (int list[], int p) { //Find out who has run out of cards first
        for (int i = 0; i < list.length; i++) {
            if (list[i] == p) return true; //if the element is p, return true
        }
        return false; //otherwise return false
    }

    public static int cardat (int list[], int p) { //Find out where card is located
        for (int i = 0; i < list.length; i++) {
            if (list[i] == p) return i; //if the element is p, return true
        }
        return -1; //otherwise return false
    }

    public static void showp1 (int[] hand, Image[] imageDeck) { //show player 1's cards
        BufferedImage bufferedimg = new BufferedImage (930, 800, BufferedImage.TYPE_INT_ARGB);
        graphics = bufferedimg.createGraphics ();

        Image table = loadImage(rootdir + backgrounddir + "table player1.png"); //load table mask
        table = table.getScaledInstance(930, 615, Image.SCALE_DEFAULT); //scale table mask

        graphics.drawImage (table, 0, 0, null); //draw table mask
        for (int i = 0; i < hand.length; i++) { //show player 1's cards
            graphics.drawImage (imageDeck[hand[i]], 460 - (200 + 40 * (hand.length - 1)) / 2 + 40 * i, 500, null); //draw out each card
        }
        graphics.fillRect(-20, 613, 950, 200); //draw a bit of the scoreboard
        //c.println("ok");
        c.drawImage (bufferedimg, 0, 0, null);
    }

    public static void showp2 (int[] hand, Image[] imageDeck) { //show player 2's cards
        BufferedImage bufferedimg = new BufferedImage (930, 800, BufferedImage.TYPE_INT_ARGB);
        graphics = bufferedimg.createGraphics ();


        Image backcard = loadImage (rootdir + cardsdir + "b sideways.png"); //load back of card
        backcard = backcard.getScaledInstance(265, 200, Image.SCALE_DEFAULT); //scale card

        Image[] newimageDeck = new Image[imageDeck.length];
        if (showcards == true) {
            for (int i = 0; i < imageDeck.length; i++) {
                newimageDeck[i] = imageDeck[i].getScaledInstance(265, 200, Image.SCALE_DEFAULT); //scale card
            }
        }

        Image table = loadImage(rootdir + backgrounddir + "table player2.png"); //load table mask
        table = table.getScaledInstance(930, 615, Image.SCALE_DEFAULT); //scale table mask

        graphics.drawImage (table, 0, 0, null); //draw table mask
        for (int i = 0; i < hand.length; i++) { //show player 2's cards
            if (showcards == true) graphics.drawImage (newimageDeck[hand[i]], 50-265, 309 - (200 - 28 * (hand.length-1)) / 2 - 28 * i, null);
            else graphics.drawImage (backcard, 50-265, 309 - (200 + 28 * (hand.length-1)) / 2 + 28 * i, null);
        }
        c.drawImage (bufferedimg, 0, 0, null);
    }

    public static void showp3 (int[] hand, Image[] imageDeck) { //show player 3's cards
        BufferedImage bufferedimg = new BufferedImage (930, 800, BufferedImage.TYPE_INT_ARGB);
        graphics = bufferedimg.createGraphics ();


        Image backcard = loadImage (rootdir + cardsdir + "b.png");
        backcard = backcard.getScaledInstance(200, 265, Image.SCALE_DEFAULT);

        Image table = loadImage(rootdir + backgrounddir + "table player3.png"); //load table mask
        table = table.getScaledInstance(930, 615, Image.SCALE_DEFAULT); //scale table mask

        Image[] newimageDeck = new Image[imageDeck.length];
        if (showcards == true) {
            for (int i = 0; i < imageDeck.length; i++) {
                newimageDeck[i] = imageDeck[i].getScaledInstance(200, 265, Image.SCALE_DEFAULT); //scale card
            }
        }

        graphics.drawImage (table, 0, 0, null); //draw table mask

        for (int i = 0; i < hand.length; i++) { //show player 3's cards
            if (showcards == true) graphics.drawImage (newimageDeck[hand[i]], 460 - (200 - 28 * (hand.length - 1)) / 2 - 28 * i, 51-265, null);
            else graphics.drawImage (backcard, 460 - (200 - 28 * (hand.length - 1)) / 2 - 28 * i, 51-265, null);
        }
        c.drawImage (bufferedimg, 0, 0, null);
    }

    public static void showp4 (int[] hand, Image[] imageDeck) { //show player 4's cards
        BufferedImage bufferedimg = new BufferedImage (930, 800, BufferedImage.TYPE_INT_ARGB);
        graphics = bufferedimg.createGraphics ();


        Image backcard = loadImage (rootdir + cardsdir + "b sideways.png");
        backcard = backcard.getScaledInstance(265, 200, Image.SCALE_DEFAULT);

        Image table = loadImage(rootdir + backgrounddir + "table player4.png"); //load table mask
        table = table.getScaledInstance(930, 615, Image.SCALE_DEFAULT); //scale table mask

        Image[] newimageDeck = new Image[imageDeck.length];
        if (showcards == true) {
            for (int i = 0; i < imageDeck.length; i++) {
                newimageDeck[i] = imageDeck[i].getScaledInstance(265, 200, Image.SCALE_DEFAULT); //scale card
            }
        }

        graphics.drawImage (table, 0, 0, null); //draw table mask

        for (int i = 0; i < hand.length; i++) { //show player 4's cards
            if (showcards == true) graphics.drawImage (newimageDeck[hand[i]], 868, 309 - (200 + 28 * (hand.length-1)) / 2 + 28 * i, null);
            else graphics.drawImage (backcard, 868, 309 - (200 - 28 * (hand.length-1)) / 2 - 28 * i, null);
        }
        c.drawImage (bufferedimg, 0, 0, null);
    }

    public static void scoreboard(int p1s, int p2s, int p3s, int p4s, int round, int Pres, int VPres, int Scum, int turn) { //show the scoreboard
        BufferedImage bufferedimg = new BufferedImage (930, 800, BufferedImage.TYPE_INT_ARGB);
        graphics = bufferedimg.createGraphics ();

        graphics.setColor(new Color (255, 255, 255)); //set colour to white
        graphics.fillRect(-20, 613, 950, 200); //scoreboard background is a rectangle
        Font f = new Font ("Broadway", Font.BOLD, 35);
        graphics.setColor(new Color (0, 0, 0));
        graphics.setFont (f);
        graphics.drawString ("Scores:", 9, 650); //Display message
        f = new Font ("Broadway", Font.PLAIN, 25);
        graphics.setFont (f);
        graphics.drawString (("Player 1: " + p1s), 9, 680); //Display player 1 score
        graphics.drawString (("Player 2: " + p2s), 9, 705); //Display player 2 score
        graphics.drawString (("Player 3: " + p3s), 9, 730); //Display player 3 score
        graphics.drawString (("Player 4: " + p4s), 9, 755); //Display player 4 score
        f = new Font ("Broadway", Font.BOLD, 35);
        graphics.setFont (f);
        graphics.drawString(("Round " + round), 728, 650); //Display round number
        f = new Font ("Broadway", Font.PLAIN, 25);
        graphics.setFont (f);

        graphics.drawString (("Player " + turn + "'s Turn"), 734, 680); //display the turn
        if (Pres != 0) {
            graphics.drawString (("Current President: Player " + Pres), 590, 705); //Display president
        } else {
            graphics.drawString (("Current President: Nobody"), 590, 705); //There is no president
        }

        if (VPres != 0) {
            graphics.drawString (("Current Vice President: Player " + VPres), 532, 730); //Display vp
        } else {
            graphics.drawString (("Current Vice President: Nobody"), 532, 730); //There is no vp
        }

        if (Scum != 0) {
            graphics.drawString (("Current Scumbag: Player " + Scum), 592, 755); //display scumbag
        } else {
            graphics.drawString (("Current Scumbag: Nobody"), 592, 755); //there is no scumbag
        }



        c.drawImage (bufferedimg, 0, 0, null);
    }

    public static void endroundscoreboard(int p1s, int p2s, int p3s, int p4s, int round, int Pres, int VPres, int Scum) { //show the end of round scoreboard
        BufferedImage bufferedimg = new BufferedImage (930, 800, BufferedImage.TYPE_INT_ARGB);

        graphics = bufferedimg.createGraphics ();

        Image table = loadImage (rootdir + backgrounddir + "tablebig.png"); //load the new background

        table = table.getScaledInstance(930, 800, Image.SCALE_DEFAULT);

        graphics.drawImage (table, 0, 0, null);

        graphics.setColor(new Color (255, 255, 255));

        Font f = new Font ("Broadway", Font.BOLD, 45);
        graphics.setFont (f);
        graphics.drawString (("End of Round " + round), 273, 249); //display round number

        f = new Font ("Broadway", Font.PLAIN, 35);
        graphics.setFont (f);
        graphics.drawString ("Scores:", 390, 290);

        f = new Font ("Broadway", Font.PLAIN, 25);
        graphics.setFont (f);
        graphics.drawString (("Player 1: " + p1s), 390, 325); //Display player 1 score
        graphics.drawString (("Player 2: " + p2s), 390, 350); //Display player 2 score
        graphics.drawString (("Player 3: " + p3s), 390, 375); //Display player 3 score
        graphics.drawString (("Player 4: " + p4s), 390, 400); //Display player 4 score
        graphics.drawString ("President: Player " + Pres, 345, 440); //Display president
        graphics.drawString ("Vice President: Player " + VPres, 315, 465); //Display vice president
        graphics.drawString ("Scumbag: Player " + Scum, 350, 490); //Display scumbag

        graphics.drawString ("Press enter to continue", 320, 540); //prompt for key press

        c.drawImage (bufferedimg, 0, 0, null);

        c.readChar();

    }

    public static int[] add (int[] list, int p) {
        int[] newlist = new int[list.length + 1];
        if (list.length > 0) {
            for (int i = 0; i < list.length; i++) {
                newlist[i] = list[i]; //copy contents of list to newlist
            }
            newlist[list.length] = p; //add element p to newlist.
        } else {
            newlist[0] = p;
        }
        return newlist;
    }

    public static void background () { //show the background
        Image table = loadImage (rootdir + backgrounddir + "table.png");

        table = table.getScaledInstance(930, 615, Image.SCALE_DEFAULT);
        c.drawImage (table, 0, 0, null);
    }

    public static void stackshow (int[] playedcards, Image[] imageDeck) { //show the deck in the middle with the last cards played
        BufferedImage bufferedimg = new BufferedImage (930, 800, BufferedImage.TYPE_INT_ARGB);
        graphics = bufferedimg.createGraphics ();

        Image table = loadImage(rootdir + backgrounddir + "table deck.png"); //load table mask
        table = table.getScaledInstance(930, 615, Image.SCALE_DEFAULT); //scale table mask

        graphics.drawImage (table, 0, 0, null); //draw table mask
        for (int i = 0; i < playedcards.length; i++) { //display every card in the array
            graphics.drawImage (imageDeck [playedcards[i]], 460 - (200 + 40 * (playedcards.length - 1)) / 2 + 40 * i, 142, null); //draw the card in the middle
        }
        c.drawImage (bufferedimg, 0, 0, null);

    }

    public static int[] arrayinput (String input) { //convert string input to array with integers.
        String[] list = input.split(" "); //make a string array and split it at the spaces
        int[] numlist = new int[list.length];
        for (int i = 0; i < list.length; i++) {
            numlist[i] = convert(list[i]);
        }
        return numlist;
    }

    public static int findturn (int p1[], int p2[], int p3[], int p4[]) { //Find out who's turn it is (has 3 of clubs)

        //whoever has the 3 of clubs (3C) goes first.

        if (contains(p1, convert("3C")) == true) {
            return 1;
        }
        if (contains(p2, convert("3C")) == true) {
            return 2;
        }
        if (contains(p3, convert("3C")) == true) {
            return 3;
        }
        if (contains(p4, convert("3C")) == true) {
            return 4;
        }
        return 0;
    }

    public static int turnadvance(int turn, int p1[], int p2[], int p3[], int p4[]) { //advance turn number
        turn = turn+1; //increase turn by 1

        resetcursor();

        if (turn == 5) turn = 1; //if turn is 5 (4+1) reset to 1.

        if ((p1.length + p2.length + p3.length + p4.length) > 0) { //if there are still cards left
            while ((p1.length == 0 && turn == 1) || (p2.length == 0 && turn == 2) || (p3.length == 0 && turn == 3) || (p4.length == 0 && turn == 4)) {//skip players who do not have any cards
                turn = turn+1; //increase turn by 1

                if (turn == 5) turn = 1; //if turn is 5 (4+1) reset to 1.
                resetcursor();
            }
        }
        return turn;

    }

    public static void resetcursor() { //sets cursor to desired position and clears line
        c.setCursor(46, 1); //set cursor to this position
        c.println(); //print a line to clear the line
        c.setCursor(46, 1); //set the cursor back to the position
    }

    public static void printwithdelim (String line, String delim) { //I wanted to make a print method for my text file which parsed a delimiter as a new line.

        String[] list = line.split(delim); //make a string array with line split it at the delimiter

        for (int i = 0; i < list.length; i++) {
            c.println(list[i]);
        }

        c.println(); //print a line at the end
    }

    public static void instructions() throws IOException { //display instructions
        c.clear();
        char choice;
        choice = c.readChar();
        int counter = 0;
        String[] instructionbook = fill(rootdir + "instructions.txt");
        do {
            c.clear();
            c.println("\n\n==== INSTRUCTIONS ====\n(input 0 to quit)\n");
            printwithdelim(instructionbook[counter], "@"); //print contents of instruction book but create new lines at instances of \n
            c.println("\nPress enter to continue");
            choice = c.readChar();
            counter++;
        }
        while (choice != '0' && counter < instructionbook.length); //loop while char is not 0 and counter is in bound


    }


    public static int winner(int p1s, int p2s, int p3s, int p4s, int Pres, int scorenum) { //return the winner

        int winner1 = 0, winner2 = 0, winner1s = 0, winner2s = 0;

        if (p1s >= scorenum) {
            if (winner1 == 0) { //if the winner1 has not been chosen
                winner1 = 1; //set winner1 to player
                winner1s = p1s; //set winner1 score to player's score
            } else { //if winner1 has been chosen
                winner2 = 1; //set winner2 to player
                winner2s = p1s; //set winner2 score to player's score
            }
        }
        if (p2s >= scorenum) {
            if (winner1 == 0) { //if the winner1 has not been chosen
                winner1 = 2; //set winner1 to player
                winner1s = p2s; //set winner1 score to player's score
            } else { //if winner1 has been chosen
                winner2 = 2; //set winner2 to player
                winner2s = p2s; //set winner2 score to player's score
            }
        }
        if (p3s >= scorenum) {
            if (winner1 == 0) { //if the winner1 has not been chosen
                winner1 = 3; //set winner1 to player
                winner1s = p3s; //set winner1 score to player's score
            } else { //if winner1 has been chosen
                winner2 = 3; //set winner2 to player
                winner2s = p3s; //set winner1 score to player's score
            }
        }
        if (p4s >= scorenum) {
            if (winner1 == 0) { //if the winner1 has not been chosen
                winner1 = 4; //set winner1 to player
                winner1s = p4s; //set winner1 score to player's score
            } else { //if winner1 has been chosen
                winner2 = 4; //set winner2 to player
                winner2s = p4s; //set winner1 score to player's score
            }
        }

        //compare winner1 and winner 2

        if (winner1s > winner2s) {
            return winner1; //return higher winner
        }
        if (winner2s > winner1s) {
            return winner2; //return higher winner
        }

        if (winner1 == winner2) { //if the scores are equal
            return Pres; //return whoever is the president
        }
        return winner1;
    }


    public static void main (String[] args) throws Exception
    {
        c = new Console (47, 115, 14, "President - Andrew Pun");
        Image imageDeck[] = loadCardImages ();
        int scorenum = 11; //initiaize how many points to play up to (11 by default)

        while (true) {

            char choice;
            do {
                Image table = loadImage (rootdir + backgrounddir + "menu.png"); //load the new background

                //display winner message

                table = table.getScaledInstance(930, 800, Image.SCALE_DEFAULT);

                c.setColor(new Color (255, 255, 255));
                c.drawImage (table, 0, 0, null);
                choice = c.readChar();

                if (choice == '3') { //debug settings
                    do {
                        c.clear();
                        c.println("\n\n==== DEBUG SETTINGS ====\n");
                        c.println("1. See other player's cards: " + showcards);
                        c.println("2. AI controls player 1: " + AIp1);
                        c.println("3. How many points to win: " + scorenum);
                        c.println("0. Quit");
                        choice = c.readChar();


                        if (choice == '1') { //toggle showcards

                            if (showcards == true) {

                                showcards = false;

                            } else {

                                showcards = true;

                            }

                        }
                        if (choice == '2') { //toggle ai controlling player

                            if (AIp1 == true) {

                                AIp1 = false;

                            } else {

                                AIp1 = true;

                            }

                        }
                        if (choice == '3') { //set round numbers
                            c.clear();
                            int choiceint; //new integer input
                            c.print("How many points do you want to play to? "); //prompt for input
                            choiceint = c.readInt(); //read input
                            scorenum = choiceint; //set score to choice
                        }

                    }
                    while (choice != '0');
                }
                if (choice == '2') { //instructions
                    instructions(); //display instructions
                }
                if (choice == '1') { //start a game




                    int[] arrayinput; //initialize arrayinput
                    int p1s = 0, p2s = 0, p3s = 0, p4s = 0; //initialize all scores
                    int p1[], p2[], p3[], p4[]; //initialize the hands of each player
                    int Pres = 0, VPres = 0, Scum = 0; //initialize president, vice president, and scum (1, 2, 3, 4). Meant to denote which player is which during each round
                    int round = 0; //initialize round number
                    int playedcards[];

                    // ======= Start game =======
                    while (p1s < scorenum && p2s < scorenum && p3s < scorenum && p4s < scorenum) {
                        c.clear();
                        Random ran  = new Random();
                        String input = "aa"; //initialize input and reset it to arbituary value
                        int missedturns = 0; //intialize variable missed turn which signifies which player has the right to play any card after all players have passed
                        int[] currentstack = new int[0]; //make the deck in the middle
                        int[] newplay = new int[0]; //make an array of cards about to be played
                        int turn = 0; //variable to denote who's turn it is (1,2,3,4)
                        round++; //advance round number

                        //initialize all decks to size zero

                        p1 = new int[0];
                        p2 = new int[0];
                        p3 = new int[0];
                        p4 = new int[0];

                        int deck[] = initDeck (); //create new deck
                        shuffle(deck); //shuffle the deck
                        background(); //display background
                        /*
                         for (int i = 0; i < deck.length; i++) {
                         randomCardSound();
                         if (i % 4 == 0) {
                         p1 = add(p1, deck[i]);
                         valuesort(p1);
                         showp1(p1, imageDeck);
                         }
                         if (i % 4 == 1) {
                         p2 = add(p2, deck[i]);
                         valuesort(p2);
                         showp2(p2, imageDeck);
                         }
                         if (i % 4 == 2) {
                         p3 = add(p3, deck[i]);
                         valuesort(p3);
                         showp3(p3, imageDeck);
                         }
                         if (i % 4 == 3) {
                         p4 = add(p4, deck[i]);
                         valuesort(p4);
                         showp4(p4, imageDeck);
                         }

                         //Thread.sleep(100);
                         }
                         */

                        p1 = new int[13];
                        p2 = new int[13];
                        p3 = new int[13];
                        p4 = new int[13];
                        deal(deck, p1, p2, p3, p4);


                        valuesort(p1); //sort all cards
                        valuesort(p2); //sort all cards
                        valuesort(p3); //sort all cards
                        valuesort(p4); //sort all cards
                        /*
                         for (int i = 0; i < 13; i++) {
                         p1 = remove(p1, p1[0]);
                         //p2 = remove(p2, p2[0]);
                         //p3 = remove(p3, p3[0]);
                         //p4 = remove(p4, p4[0]);
                         }*/
                        //background();


                        showp2(p2, imageDeck);
                        showp3(p3, imageDeck);
                        showp4(p4, imageDeck);
                        showp1(p1, imageDeck);

                        scoreboard(p1s, p2s, p3s, p4s, round, Pres, VPres, Scum, turn); //display scoreboard
                        if (round > 1) { //card trading

                            int tempcard = 0; //initialize temporary card

                            //scumbag gives their highest card
                            if (Scum == 1) {
                                if (AIp1 == false) {
                                    resetcursor(); //reset cursor
                                    c.print("It is time to trade with the President.");
                                    Thread.sleep(1000);
                                    resetcursor(); //reset cursor
                                    c.print("You will give them your highest card. That's how President works!");
                                    Thread.sleep(1000);
                                    resetcursor(); //reset cursor
                                    tempcard = p1[p1.length-1]; //temporary card is set to highest card
                                    p1 = remove(p1, p1[p1.length-1]); //remove the card

                                } else {
                                    tempcard = p1[p1.length-1]; //temporary card is set to highest card
                                    p1 = remove(p1, p1[p1.length-1]); //remove the card
                                }
                                showp1(p1, imageDeck);
                                scoreboard(p1s, p2s, p3s, p4s, round, Pres, VPres, Scum, turn); //display scoreboard
                            }
                            if (Scum == 2) {
                                tempcard = p2[p2.length-1]; //temporary card is set to highest card
                                p2 = remove(p2, p2[p2.length-1]); //remove the card
                                showp2(p2, imageDeck);
                            }
                            if (Scum == 3) {
                                tempcard = p3[p3.length-1]; //temporary card is set to highest card
                                p3 = remove(p3, p3[p3.length-1]); //remove the card
                                showp3(p3, imageDeck);
                            }
                            if (Scum == 4) {
                                tempcard = p4[p4.length-1]; //temporary card is set to highest card
                                p4 = remove(p4, p4[p4.length-1]); //remove the card
                                showp4(p4, imageDeck);
                            }

                            randomCardSound();
                            Thread.sleep(800);
                            //showp1(p1, imageDeck);

                            //president takes card and gives their own card
                            if (Pres == 1) {
                                if (AIp1 == false) {
                                    arrayinput = arrayinput(input);
                                    resetcursor(); //reset cursor
                                    c.print("It is time to trade with the scumbag.");
                                    boolean hastraded = false; //flag variable for notifying when player has traded
                                    Thread.sleep(1000);
                                    while (inputvalid(p1, arrayinput) == false && hastraded == false) {
                                        resetcursor(); //reset cursor
                                        c.print("Type in the card you want to give them: "); //prompt for input
                                        input = c.readLine(); //prompt for input

                                        arrayinput = arrayinput(input); //convert the line to an array

                                        if (inputvalid(p1, arrayinput) == true) {
                                            hastraded = true;
                                            p1 = remove(p1, arrayinput[0]); //remove the card
                                            randomCardSound();
                                            showp1(p1, imageDeck);
                                            scoreboard(p1s, p2s, p3s, p4s, round, Pres, VPres, Scum, turn); //display scoreboard
                                            Thread.sleep(800);
                                            p1 = add(p1, tempcard); //give president scum's card
                                            tempcard = arrayinput[0]; //set tempcard to arrayinput[0]
                                            resetcursor();
                                        } else {
                                            errorSound();
                                            resetcursor();
                                            c.print("Invalid input. Try again.");
                                            resetcursor(); //reset cursor
                                            c.print(arrayinput[0] + " " + arrayinput.length);
                                            Thread.sleep(900);
                                        }
                                    }
                                } else {
                                    int randomcard = ran.nextInt(p1.length); //choose a random card
                                    p1 = remove(p1, p1[randomcard]); //remove the card
                                    randomCardSound();
                                    Thread.sleep(800);
                                    p1 = add(p1, tempcard); //give president scum's card
                                    tempcard = p1[randomcard]; //set temp card to the random card

                                }
                                valuesort(p1);
                                showp1(p1, imageDeck);
                                scoreboard(p1s, p2s, p3s, p4s, round, Pres, VPres, Scum, turn); //display scoreboard
                            }
                            if (Pres == 2) {
                                int randomcard = ran.nextInt(p1.length); //choose a random card
                                p2 = remove(p2, p2[randomcard]); //remove the card
                                randomCardSound();
                                Thread.sleep(800);
                                p2 = add(p2, tempcard); //give president scum's card
                                valuesort(p2);
                                tempcard = p2[randomcard]; //set temp card to the random card
                                showp2(p2, imageDeck);
                            }
                            if (Pres == 3) {
                                int randomcard = ran.nextInt(p1.length); //choose a random card
                                p3 = remove(p3, p3[randomcard]); //remove the card
                                randomCardSound();
                                Thread.sleep(800);
                                p3 = add(p3, tempcard); //give president scum's card
                                valuesort(p3);
                                tempcard = p3[randomcard]; //set temp card to the random card
                                showp3(p3, imageDeck);
                            }
                            if (Pres == 4) {
                                int randomcard = ran.nextInt(p1.length); //choose a random card
                                p4 = remove(p4, p4[randomcard]); //remove the card
                                randomCardSound();
                                Thread.sleep(800);
                                p4 = add(p4, tempcard); //give president scum's card
                                valuesort(p4);
                                tempcard = p4[randomcard]; //set temp card to the random card
                                showp4(p4, imageDeck);
                            }

                            randomCardSound();
                            Thread.sleep(800);
                            if (Scum == 1) {
                                p1 = add(p1, tempcard); //add new card
                                valuesort(p1);
                                showp1(p1, imageDeck);
                                scoreboard(p1s, p2s, p3s, p4s, round, Pres, VPres, Scum, turn); //display scoreboard
                            }
                            if (Scum == 2) {
                                p2 = add(p2, tempcard); //add new card
                                valuesort(p2);
                                showp2(p2, imageDeck);
                            }
                            if (Scum == 3) {
                                p3 = add(p3, tempcard); //add new card
                                valuesort(p3);
                                showp3(p3, imageDeck);
                            }
                            if (Scum == 4) {
                                p4 = add(p4, tempcard); //add new card
                                valuesort(p4);
                                showp4(p4, imageDeck);
                            }

                            randomCardSound();
                            Thread.sleep(800);
                        }

                        scoreboard(p1s, p2s, p3s, p4s, round, Pres, VPres, Scum, turn);

                        if (round == 1) { //on round 1, the player with the 3 of clubs goes first

                            turn = findturn(p1, p2, p3, p4); //figure out who has the 3 of clubs. They will go first.
                            scoreboard(p1s, p2s, p3s, p4s, round, Pres, VPres, Scum, turn); //display scoreboard


                            //player who has 3 of clubs plays first
                            if (turn == 2) {
                                //p2 plays 3c
                                p2 = remove(p2, convert("3C"));
                                newplay = add(newplay, convert("3C"));
                                showp2(p2, imageDeck);

                            } else if (turn == 3) {
                                //p3 plays 3c
                                p3 = remove(p3, convert("3C"));
                                newplay = add(newplay, convert("3C"));
                                showp3(p3, imageDeck);

                            } else if (turn == 4) {
                                //p4 plays 3c
                                p4 = remove(p4, convert("3C"));
                                newplay = add(newplay, convert("3C"));
                                showp4(p4, imageDeck);

                            } else if (turn == 1) {
                                if (AIp1 == false) {
                                    //p1 is prompted to play 3c
                                    arrayinput = arrayinput(input);
                                    resetcursor(); //reset cursor
                                    c.print("You have the 3 of clubs! Place it down.");
                                    Thread.sleep(1500);
                                    while (arrayinput[0] != convert("3C")) {
                                        resetcursor(); //reset cursor
                                        c.print("Enter 3C to place down the 3 of clubs: ");
                                        input = c.readLine(); //prompt for input

                                        arrayinput = arrayinput(input); //convert the line to an array

                                        if (arrayinput[0] == convert("3C")) { //if card is 3C, play is valid

                                            p1 = remove(p1, convert("3C"));
                                            newplay = add(newplay, convert("3C"));
                                            resetcursor();
                                        } else {
                                            errorSound();
                                            resetcursor();
                                            c.print("Invalid play. Try again.");
                                            Thread.sleep(900);
                                        }
                                    }
                                } else { //AI controls player
                                    p1 = remove(p1, convert("3C"));
                                    newplay = add(newplay, convert("3C"));
                                    showp4(p1, imageDeck);
                                }
                                input = "aa"; //reset input
                                showp1(p1, imageDeck); //display cards
                            }

                            currentstack = newplay; //set stack to new played cards
                            stackshow(currentstack, imageDeck); //display stack
                            scoreboard(p1s, p2s, p3s, p4s, round, Pres, VPres, Scum, turn); //display scoreboard
                            randomCardSound();
                            Thread.sleep(800);
                            turn = turnadvance(turn, p1, p2, p3, p4);
                        } else { //on later rounds the president starts the game
                            turn = Pres; //the president starts the game
                        }

                        currentstack = newplay; //set stack to new played cards
                        stackshow(currentstack, imageDeck); //display stack


                        scoreboard(p1s, p2s, p3s, p4s, round, Pres, VPres, Scum, turn); //display scoreboard

                        int NewPres = 0, NewVPres = 0, NewCitizen = 0, NewScum = 0; //initialize president, vice president, citizen (unlisted), and scum (1, 2, 3, 4). Meant to denote which player is which during each round

                        while ((p1.length + p2.length + p3.length + p4.length) > 0) { //loop while the players still have cards
                            input = "aa"; //reset the input to arbituary value
                            int[] bestPlay;
                            if (turn == 2) {
                                if (p2.length > 0) { //check if the player has cards

                                    if (NewCitizen > 0) { //if a citizen has been chosen and the player still has cards
                                        NewScum = 2; //set this round's scumbag to player 2
                                        p2 = new int[0]; //set cards to 0
                                        showp2(p2, imageDeck);

                                    } else {

                                        if (missedturns == 2) {
                                            Thread.sleep(400);
                                            currentstack = new int[0]; //reset current stack if a there are too many missed turns
                                            stackshow(currentstack, imageDeck); //display stack
                                        }
                                        bestPlay = bestPlay(p2, currentstack);
                                        if (bestPlay[0] != 500) {
                                            missedturns = 0; //reset missed turns
                                            p2 = remove(p2, bestPlay); //play the card by removing the chosen card and putting it in the stack
                                            newplay = bestPlay;
                                            showp2(p2, imageDeck);
                                            randomCardSound();

                                            if (p2.length == 0) { //if the player has reached 0 cards
                                                if (cardvalue(newplay[0]) == 0  && NewScum == 0) { //if the player placed a 2
                                                    NewScum = 2; //set this round's scumbag to player 2
                                                } else if (NewPres == 0) { //if there is no president
                                                    NewPres = 2; //set this round's president to player 2
                                                } else if (NewVPres == 0) { //if there is no vice president
                                                    NewVPres = 2; //set this round's vice president to player 2
                                                } else if (NewCitizen == 0) { //if there is no citizen
                                                    NewCitizen = 2; //set citizen to player 2
                                                } else if (NewScum == 0) { //if there is no scumbag
                                                    NewScum = 2; //set this round's scumbag to player 2
                                                }
                                            }

                                        } else {
                                            if (missedturns == 0) {
                                                missedturns = 1; //set missed turns to 1 (previous player) if you are skipping and the previous player was able to play
                                            }
                                        }
                                    }

                                }
                            } else if (turn == 3) {
                                if (p3.length > 0) { //check if the player has cards

                                    if (NewCitizen > 0) { //if a citizen has been chosen and the player still has cards
                                        NewScum = 3; //set this round's scumbag to player 3
                                        p3 = new int[0]; //set cards to 0
                                        showp3(p3, imageDeck);

                                    } else {

                                        if (missedturns == 3) {
                                            currentstack = new int[0]; //reset current stack if a there are too many missed turns
                                            stackshow(currentstack, imageDeck); //display stack
                                            Thread.sleep(400);
                                        }
                                        bestPlay = bestPlay(p3, currentstack);
                                        if (bestPlay[0] != 500) { //check if best play is not skip
                                            missedturns = 0; //reset missed turns to zero
                                            p3 = remove(p3, bestPlay); //play the card by removing the chosen card and putting it in the stack
                                            newplay = bestPlay;
                                            showp3(p3, imageDeck);
                                            randomCardSound();

                                            if (p3.length == 0) { //if the player has reached 0 cards
                                                if (cardvalue(newplay[0]) == 0  && NewScum == 0) { //if the player placed a 2
                                                    NewScum = 3; //set this round's scumbag to player 3
                                                } else if (NewPres == 0) { //if there is no president
                                                    NewPres = 3; //set this round's president to player 3
                                                } else if (NewVPres == 0) { //if there is no vice president
                                                    NewVPres = 3; //set this round's vice president to player 3
                                                } else if (NewCitizen == 0) { //if there is no citizen
                                                    NewCitizen = 3; //set citizen to player 3
                                                } else if (NewScum == 0) { //if there is no scumbag
                                                    NewScum = 3; //set this round's scumbag to player 3
                                                }
                                            }
                                        } else {
                                            if (missedturns == 0) {
                                                missedturns = 2; //set missed turns to 2 (previous player) if you are skipping and the previous player was able to play
                                            }
                                        }
                                    }
                                }
                            } else if (turn == 4) {
                                if (p4.length > 0) { //check if the player has cards

                                    if (NewCitizen > 0) { //if a citizen has been chosen and the player still has cards
                                        NewScum = 4; //set this round's scumbag to player 4
                                        p4 = new int[0]; //set cards to 0
                                        showp4(p4, imageDeck);

                                    } else {

                                        if (missedturns == 4) {
                                            currentstack = new int[0]; //reset current stack if a there are too many missed turns
                                            stackshow(currentstack, imageDeck); //display stack
                                            Thread.sleep(400);
                                        }
                                        bestPlay = bestPlay(p4, currentstack);
                                        if (bestPlay[0] != 500) {
                                            missedturns = 0; //reset missed turns to zero
                                            p4 = remove(p4, bestPlay); //play the card by removing the chosen card and putting it in the stack
                                            newplay = bestPlay;
                                            showp4(p4, imageDeck);
                                            randomCardSound();

                                            if (p4.length == 0) { //if the player has reached 0 cards
                                                if (cardvalue(newplay[0]) == 0  && NewScum == 0) { //if the player placed a 2
                                                    NewScum = 4; //set this round's scumbag to player 4
                                                } else if (NewPres == 0) { //if there is no president
                                                    NewPres = 4; //set this round's president to player 4
                                                } else if (NewVPres == 0) { //if there is no vice president
                                                    NewVPres = 4; //set this round's vice president to player 4
                                                } else if (NewCitizen == 0) { //if there is no citizen
                                                    NewCitizen = 4; //set citizen to player 4
                                                } else if (NewScum == 0) { //if there is no scumbag
                                                    NewScum = 4; //set this round's scumbag to player 4
                                                }
                                            }

                                        } else {
                                            if (missedturns == 0) {
                                                missedturns = 3; //set missed turns to 3 (previous player) if you are skipping and the previous player was able to play
                                            }
                                        }
                                    }

                                }
                            } else if (turn == 1) {
                                if (p1.length > 0) { //check if the player has cards

                                    if (NewCitizen > 0) { //if a citizen has been chosen and the player still has cards
                                        NewScum = 1; //set this round's scumbag to player 1
                                        p1 = new int[0]; //set cards to 0
                                        showp1(p1, imageDeck);
                                    } else {

                                        if (missedturns == 1) {
                                            currentstack = new int[0]; //reset current stack if a there are too many missed turns
                                            stackshow(currentstack, imageDeck); //display stack
                                            Thread.sleep(400);
                                        }
                                        boolean played = false; //flag variable to see if you played
                                        bestPlay = new int[1]; //reset best play
                                        bestPlay[0] = 600;
                                        while (played == false) {
                                            if (AIp1 == false) {
                                                resetcursor(); //reset cursor
                                                c.print("Input the cards you would like to play: ");
                                                input = c.readLine(); //prompt for input
                                                arrayinput = arrayinput(input); //convert the line to an array

                                                bestPlay = arrayinput;
                                            } else { //AI controls player
                                                bestPlay = bestPlay(p1, currentstack); //set best play to bestplay in p1
                                            }
                                            if (validPlay(bestPlay, currentstack) == true && inputvalid(p1, bestPlay) == true) { //if play is valid, play the cards
                                                missedturns = 0; //reset missed turns to zero
                                                p1 = remove(p1, bestPlay); //play the card by removing the chosen card and putting it in the stack
                                                newplay = bestPlay;
                                                showp1(p1, imageDeck);
                                                randomCardSound();
                                                played = true;

                                                if (p1.length == 0) { //if the player has reached 0 cards
                                                    if (cardvalue(newplay[0]) == 0  && NewScum == 0) { //if the player placed a 2
                                                        NewScum = 1; //set this round's scumbag to player 1
                                                    } else if (NewPres == 0) { //if there is no president
                                                        NewPres = 1; //set this round's president to player 1
                                                    } else if (NewVPres == 0) { //if there is no vice president
                                                        NewVPres = 1; //set this round's vice president to player 1
                                                    } else if (NewCitizen == 0) { //if there is no citizen
                                                        NewCitizen = 1; //set citizen to player 1
                                                    } else if (NewScum == 0) { //if there is no scumbag
                                                        NewScum = 1; //set this round's scumbag to player 1
                                                    }
                                                }

                                            } else if (bestPlay[0] == convert("pass")) { //if player passes
                                                if (missedturns == 0) {
                                                    missedturns = 4; //set missed turns to 4 (previous player) if you are skipping and the previous player was able to play
                                                }
                                                played = true;
                                            } else { //not a valid play
                                                errorSound();
                                                resetcursor();
                                                c.print("Invalid play. Try again.");
                                                Thread.sleep(900);
                                            }
                                        }
                                    }

                                    input = "aa"; //reset the input to arbituary value
                                }
                            }

                            currentstack = newplay; //set stack to new played cards
                            stackshow(currentstack, imageDeck); //display stack
                            scoreboard(p1s, p2s, p3s, p4s, round, Pres, VPres, Scum, turn); //display scoreboard
                            resetcursor();
                            Thread.sleep(800);

                            if (missedturns == 1 && p1.length == 0) { //if player is meant to clear the deck, but does not have any cards
                                missedturns = 4; //set missed turns to player 2
                            }
                            if (missedturns == 2 && p2.length == 0) { //if player is meant to clear the deck, but does not have any cards
                                missedturns--; //set missed turns to player 3
                            }
                            if (missedturns == 3 && p3.length == 0) { //if player is meant to clear the deck, but does not have any cards
                                missedturns--; //set missed turns to player 4
                            }
                            if (missedturns == 4 && p4.length == 0) { //if player is meant to clear the deck, but does not have any cards
                                missedturns--; //set missed turns to player 1
                            }


                            if ((currentstack.length > 0 && cardvalue(currentstack[0]) == 0) || currentstack.length == 4) { //if a 2 has been played or 4 cards have been played
                                currentstack = new int[0]; //reset current stack
                                stackshow(currentstack, imageDeck); //display stack
                                Thread.sleep(400);
                            } else { //advance turn normally
                                turn = turnadvance(turn, p1, p2, p3, p4);
                            }


                            //advance turn if someone runs out of cards
                            if (turn == 1 && p1.length == 0) {
                                turn = turnadvance(turn, p1, p2, p3, p4);
                            }
                            if (turn == 2 && p2.length == 0) {
                                turn = turnadvance(turn, p1, p2, p3, p4);
                            }
                            if (turn == 2 && p2.length == 0) {
                                turn = turnadvance(turn, p1, p2, p3, p4);
                            }
                            if (turn == 2 && p2.length == 0) {
                                turn = turnadvance(turn, p1, p2, p3, p4);
                            }

                            resetcursor();
                            scoreboard(p1s, p2s, p3s, p4s, round, Pres, VPres, Scum, turn);

                        }

                        //set all roles for next round

                        Pres = NewPres;
                        VPres = NewVPres;
                        Scum = NewScum;

                        //president gets 2 points

                        if (NewPres == 1) {
                            p1s += 2;

                        }
                        if (NewPres == 2) {
                            p2s += 2;

                        }
                        if (NewPres == 3) {
                            p3s += 2;

                        }
                        if (NewPres == 4) {
                            p4s += 2;

                        }

                        //vice president gets 1 point

                        if (NewVPres == 1) {
                            p1s += 1;

                        }
                        if (NewVPres == 2) {
                            p2s += 1;

                        }
                        if (NewVPres == 3) {
                            p3s += 1;

                        }
                        if (NewVPres == 4) {
                            p4s += 1;

                        }

                        c.clear();
                        endroundscoreboard(p1s, p2s, p3s, p4s, round, Pres, VPres, Scum); //display the end of round scoreboard

                    }

                    Font f = new Font ("Broadway", Font.BOLD, 40);
                    c.setFont (f);
                    c.clear();

                    table = loadImage (rootdir + backgrounddir + "tablebig.png"); //load the new background

                    //display winner message

                    table = table.getScaledInstance(930, 800, Image.SCALE_DEFAULT);

                    c.setColor(new Color (255, 255, 255));
                    c.drawImage (table, 0, 0, null);

                    c.drawString (("Player " + winner(p1s, p2s, p3s, p4s, Pres, scorenum) + " has won the game!"), 200, 325); //Display player 1 score

                    f = new Font ("Broadway", Font.PLAIN, 25);
                    c.setFont (f);

                    c.drawString ("Press enter to continue", 320, 540); //prompt for key press

                    c.readChar();


                }
                c.setCursor(1, 1);
            }
            while (choice != '0'); //exit when 0
        }

    } // main method
}

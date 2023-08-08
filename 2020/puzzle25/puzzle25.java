import java.util.Scanner;
import java.io.File;

public class puzzle25 {
    static final int INITIAL_SUBJECT_NUM = 7;

    public static void main(String[] args) {
        try {
            String filename = args[0];
            File inputFile = new File(filename);
            Scanner stdin = new Scanner(inputFile);
            long cardPublicKey = Long.parseLong(stdin.nextLine());
            long doorPublicKey = Long.parseLong(stdin.nextLine());
            stdin.close();

            Card card = new Card(cardPublicKey);
            Door door = new Door(doorPublicKey);
            try {
                long encryptionKey = performHandshake(card, door);
                System.out.println("ENCRYPTION KEY: " + encryptionKey);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
        }
    }

    private static long performHandshake(Card card, Door door) throws Exception {
        System.out.println("PERFORMING HANDSHAKE...");
        card.sendPublicKey(door);
        door.sendPublicKey(card);
        long cardEncryptionKey = card.generateEncryptionKey();
        if (cardEncryptionKey != door.generateEncryptionKey())
            throw new Exception("CARD READ ERROR");
        return cardEncryptionKey;
    }

}

class Card {
    private long publicKey;
    private int loopSize;
    private long doorPublicKey;

    Card(long publicKey) {
        this.publicKey = publicKey;
        this.loopSize = computeLoopSize(puzzle25.INITIAL_SUBJECT_NUM);
    }

    long getPublicKey() { return this.publicKey; }
    
    void sendPublicKey(Door door) { door.receiveCardPublicKey(this); }

    void receiveDoorPublicKey(Door door) { this.doorPublicKey = door.getPublicKey(); }

    long generateEncryptionKey() { return this.transformSubjectNumber(doorPublicKey); }

    private long transformSubjectNumber(long subjectNum) {
        long val = 1;
        for (int i = 0; i < this.loopSize; i++) {
            val *= subjectNum;
            val %= 20201227;
        }
        return val;
    }
    
    private int computeLoopSize(long subjectNum) {
        int loopSize = 0;
        long val = 1;
        boolean done = false;
        while (!done) {
            val *= subjectNum;
            val %= 20201227;
            loopSize++;
            if (val == this.publicKey)
                done = true;
        }
        return loopSize;
    }
}

class Door {
    private long publicKey;
    private int loopSize;
    private long cardPublicKey;

    Door(long publicKey) {
        this.publicKey = publicKey;
        this.loopSize = computeLoopSize(puzzle25.INITIAL_SUBJECT_NUM);
    }

    long getPublicKey() { return this.publicKey; }

    void sendPublicKey(Card card) { card.receiveDoorPublicKey(this); }

    void receiveCardPublicKey(Card card) { this.cardPublicKey = card.getPublicKey(); }
    
    long generateEncryptionKey() { return this.transformSubjectNumber(cardPublicKey); }
    
    private long transformSubjectNumber(long subjectNum) {
        long val = 1;
        for (int i = 0; i < this.loopSize; i++) {
            val *= subjectNum;
            val %= 20201227;
        }
        return val;
    }
    
    private int computeLoopSize(long subjectNum) {
        int loopSize = 0;
        long val = 1;
        boolean done = false;
        while (!done) {
            val *= subjectNum;
            val %= 20201227;
            loopSize++;
            if (val == this.publicKey)
                done = true;
        }
        return loopSize;
    }
}

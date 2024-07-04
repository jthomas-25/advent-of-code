import java.util.Scanner;
import java.io.File;
import java.util.Hashtable;

public class Puzzle16 {
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File file = new File(filename);
            Scanner stdin = new Scanner(file);
            String hexString = stdin.nextLine();
            stdin.close();
            String bitString = convertHexToBinary(hexString);
            PacketTree tree = parsePackets(bitString);
            //tree.print();
            printPart1Answer(tree);
            printPart2Answer(tree);
        }
        catch (Exception e) {
        }
    }

    private static void printPart1Answer(PacketTree tree) {
        System.out.println(String.format("Version sum: %d", tree.versionSum()));
    }
    
    private static void printPart2Answer(PacketTree tree) {
        System.out.println(tree.evaluate());
    }

    private static PacketTree parsePackets(String rootPacket) {
        PacketTree tree = new PacketTree();
        parsePacket(rootPacket, tree, null);
        return tree;
    }

    private static int parsePacket(String bitString, PacketTree tree, PacketTree.Node parent) {
        int bit = 0;
        int version = convertBinaryToInt(readBits(bitString, bit, 3));
        bit += 3;
        int typeId = convertBinaryToInt(readBits(bitString, bit, 3));
        bit += 3;
        Packet packet;
        PacketTree.Node node;
        String packetString;
        if (typeId == 4) {
            packetString = bitString.substring(0, 6);
            String binString = "";
            String bitGroup;
            do {
                bitGroup = readBits(bitString, bit, 5);
                bit += 5;
                packetString += bitGroup;
                binString += bitGroup.substring(1);
            } while (!bitGroup.startsWith("0"));
            long value = Long.parseLong(binString, 2);
            packet = new Packet(packetString, version, typeId, value);
            node = tree.insertAt(packet, parent);
        }
        else {
            packetString = bitString;
            packet = new Packet(packetString, version, typeId);
            node = tree.insertAt(packet, parent);
            char lengthTypeID = bitString.charAt(bit);
            bit++;
            if (lengthTypeID == '0') {
                int numBitsInSubPackets = convertBinaryToInt(readBits(bitString, bit, 15));
                bit += 15;
                int bitsRead = 0;
                while (bitsRead != numBitsInSubPackets) {
                    int n = parsePacket(bitString.substring(bit), tree, node);
                    bit += n;
                    bitsRead += n;
                }
            }
            else {
                int numSubPackets = convertBinaryToInt(readBits(bitString, bit, 11));
                bit += 11;
                for (int i = 0; i < numSubPackets; i++) {
                    bit += parsePacket(bitString.substring(bit), tree, node);
                }
            }
        }
        return bit;
    }
    
    private static String readBits(String bitString, int start, int numBits) {
        return bitString.substring(start, start + numBits);
    }

    private static int convertBinaryToInt(String binString) {
        return Integer.parseInt(binString, 2);
    }

    private static String convertHexToBinary(String hexString) {
        Hashtable<Character, String> hexToBinary = new Hashtable<>();
        String[] binStrings = {"0000", "0001", "0010", "0011",
                               "0100", "0101", "0110", "0111",
                               "1000", "1001", "1010", "1011",
                               "1100", "1101", "1110", "1111"};
        for (int i = 0; i < 16; i++) {
            hexToBinary.put("0123456789ABCDEF".charAt(i), binStrings[i]);
        }
        String bitString = "";
        for (int i = 0; i < hexString.length(); i++) {
            bitString += hexToBinary.get(hexString.charAt(i));
        }
        return bitString;
    }
}

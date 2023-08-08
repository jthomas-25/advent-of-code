import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;

public class puzzle14 {
    static Hashtable<Long, Long> memory;
    static ArrayList<String> insts;
    static String bitmask;
    static final int BITMASK_LENGTH = 36;

    public static void main(String[] args) {
        try {
            String filename = args[0];
            File initProgramFile = new File(filename);
            Scanner stdin = new Scanner(initProgramFile);
            insts = new ArrayList<>();
            while (stdin.hasNextLine())
                insts.add(stdin.nextLine());
            stdin.close();

            memory = new Hashtable<>();
            System.out.println("Part One:");
            printPart1Answer();
            memory.clear();
            System.out.println("Part Two:");
            printPart2Answer();

        } catch (Exception e) {
        }
    }

    private static void printPart1Answer() {
        runInitProgramVersion1();
        long sum = getSumOfMemoryValues();
        System.out.println(sum);
    }

    private static void printPart2Answer() {
        runInitProgramVersion2();
        long sum = getSumOfMemoryValues();
        System.out.println(sum);
    }

    private static long getSumOfMemoryValues() {
        long sum = 0;
        Enumeration<Long> memAddresses = memory.keys();
        while (memAddresses.hasMoreElements()) {
            long address = memAddresses.nextElement();
            long value = memory.get(address);
            sum += value;
        }
        return sum;
    }

    private static void runInitProgramVersion1() {
        for (String inst : insts) {
            String[] parts = inst.split(" = ");
            if (parts[0].equals("mask")) {
                bitmask = parts[1];
            } else {
                parts[0] = parts[0].replace("mem[","").replace("]","");
                long address = Long.parseUnsignedLong(parts[0]);
                long value = Long.parseUnsignedLong(parts[1]);
                String bin = getBinaryRep(value);
                char[] bits = bin.substring((Long.SIZE-BITMASK_LENGTH)).toCharArray();
                for (int i = 0; i < bits.length; i++) {
                    char bit = bitmask.charAt(i);
                    if (bit != 'X')
                        bits[i] = bit;
                }
                value = Long.parseUnsignedLong(String.valueOf(bits),2);
                memory.put(address, value);
            }
        }
    }
    
    private static String getBinaryRep(long value) {
        String zeros = "";
        String binaryRep = Long.toBinaryString(value);
        int numLeadingZeros = Long.numberOfLeadingZeros(value);
        if (value == 0)
            numLeadingZeros -= 1;
        for (int i = 0; i < numLeadingZeros; i++)
            zeros += "0";
        return zeros + binaryRep;
    }

    private static void runInitProgramVersion2() {
        for (String inst : insts) {
            String[] parts = inst.split(" = ");
            if (parts[0].equals("mask")) {
                bitmask = parts[1];
            } else {
                parts[0] = parts[0].replace("mem[","").replace("]","");
                long address = Long.parseUnsignedLong(parts[0]);
                long value = Long.parseUnsignedLong(parts[1]);
                String bin = getBinaryRep(address);
                char[] bits = bin.substring((Long.SIZE-BITMASK_LENGTH)).toCharArray();
                int numFloatingBits = 0;
                for (int i = 0; i < bits.length; i++) {
                    char bit = bitmask.charAt(i);
                    if (bit != '0') {
                        bits[i] = bit;
                        if (bit == 'X')
                            numFloatingBits++;
                    }
                }
                long[] addresses = calcMemoryAddresses(bits, numFloatingBits);
                for (long memAddress : addresses)
                    memory.put(memAddress, value);
            }
        }
    }

    private static long[] calcMemoryAddresses(char[] bits, int numFloatingBits) {
        long[] addresses = new long[(int)Math.pow(2,numFloatingBits)];
        for (int i = 0; i < addresses.length; i++) {
            String possibleBitValuesString = padWithZeros(Integer.toBinaryString(i), numFloatingBits);
            char[] possibleBitValues = possibleBitValuesString.toCharArray();
            int bitValuePos = 0;
            String bin = "";
            for (int j = 0; j < bits.length; j++) {
                char bit = bits[j];
                if (bit == 'X') {
                    bin += possibleBitValues[bitValuePos];
                    bitValuePos++;
                } else {
                    bin += bit;
                }
            }
            addresses[i] = Long.parseUnsignedLong(bin,2);
        }
        return addresses;
    }

    private static String padWithZeros(String s, int size) {
        String zeros = "";
        for (int i = 0; i < size; i++)
            zeros += "0";
        return zeros.substring(0, size - s.length()) + s;
    }
}

class Packet {
    private String binString;
    private int version;
    private int typeId;
    private long value;

    Packet(String binString, int version, int typeId) {
        this.binString = binString;
        this.version = version;
        this.typeId = typeId;
    }
    
    Packet(String binString, int version, int typeId, long value) {
        this(binString, version, typeId);
        this.value = value;
    }

    public boolean equals(Packet p) {
        return binString.equals(p.binString);
    }

    public String toString() {
        return String.format("Packet: version %d, typeID %d, %s", version, typeId, binString);
        //return binString;
    }
    
    int getVersion() { return version; }

    int getTypeId() { return typeId; }

    long getValue() { return value; }
}

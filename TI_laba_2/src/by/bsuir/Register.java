package by.bsuir;

public class Register {
    private int state;

    public Register(int state){
        this.state = state;
    }

    public int move(){
        String keyByte = "";
        for(int i = 1; i < 9; i++){
            keyByte += getBit(state,32);
            state = (state << 1) + (getBit(state,32)^getBit(state,28)^getBit(state,27)^getBit(state,1));
        }
        return Integer.parseInt(keyByte,2);
    }

    public int getBit(int state, int bit){
        return ((state >> (bit-1)) & 1);
    }
}

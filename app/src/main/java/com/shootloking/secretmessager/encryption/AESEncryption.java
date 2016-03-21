package com.shootloking.secretmessager.encryption;

/**
 * Created by shau-lok on 3/5/16.
 */
public class AESEncryption {

    public static final int NumberOfRound = 10; //10轮加密

    public static final int NumberOfKeyWords = 4; //密钥扩展需要4个字

    public static final int BLOCK_SIZE = 16;

    private int[] subKeys;

    /**
     * 轮常量 密钥扩展只用到10轮
     */
    public static final int[] Rcon = new int[]{0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36};


    public AESEncryption() {

    }

    /**
     * GF(2^8) 两个字节相乘
     *
     * @param a
     * @param b
     * @return
     */
    private static int GFMul(int a, int b) {

        if (a == 1) {
            return b & 0xFF;
        }
        if (b == 1) {
            return a & 0xFF;
        }
        int result = 0;
        int high_bit; //最高位
        a &= 0xff;
        b &= 0xff;


        for (int i = 0; i < 8; i++) {
            if ((b & 1) != 0) {
                result ^= a;
            }
            high_bit = a & 0x80;
            a <<= 1;
            if (high_bit != 0) {
                a ^= 0x1b;// x^8 + x^4 + x^3 + x + 1 不可约多项式
            }
            b >>= 1;

        }
        return result & 0xff;
    }


    /**
     * 密钥扩展
     *
     * @param aes_keys
     */
    public void KeyExpansion(byte[] aes_keys) {

        int column = 4;
        int totalColumns = 44;  //44个字组成
        int subkey_size = 44 * 4; //176个字节

        subKeys = new int[subkey_size];

        for (int i = 0; i < column * 4; i++) {
            subKeys[i] = aes_keys[i]; //先把前边4个aes_keys赋值给subKeys
        }


        int RconIndex = 0;

        for (int i = column * 4; i < subkey_size; i += 4) {

            if (i % (4 * column) == 0) {


                //循环左移&替换字节
                for (int j = 0; j < 4; j++) {
                    if (j < 3) {
                        subKeys[i + j] = AESCommon.SBOX[subKeys[i - 4 + j + 1] & 0xFF];//前一列下边三个向上移
                    } else {
                        subKeys[i + j] = AESCommon.SBOX[subKeys[i - 4] & 0xFF]; //前一列最顶那个移到最底
                    }
                }

                for (int j = 0; j < 4; j++) {
                    subKeys[i + j] ^= subKeys[i - 4 * column + j];//与前一块的第一列xor
                }

                subKeys[i] ^= Rcon[RconIndex++]; //伦常量xor
            } else {
                //剩下三列均与前边xor
                for (int j = 0; j < 4; j++) {
                    subKeys[i + j] = subKeys[i + j - 4] ^ subKeys[i + j - 4 * column];
                }
            }

        }
    }


    /**
     * 字节替换
     *
     * @param state
     */
    private void SubBytes(byte[] state) {
        for (int i = 0; i < BLOCK_SIZE; i++) {
            state[i] = (byte) AESCommon.SBOX[state[i] & 0xff];
        }
    }

    /**
     * 行位移变换
     *
     * @param state
     */
    private byte[] ShiftRows(byte[] state) {

        byte[] stateTmp = new byte[BLOCK_SIZE];

        //第一行
        stateTmp[0] = state[0];
        stateTmp[4] = state[4];
        stateTmp[8] = state[8];
        stateTmp[12] = state[12];

        //第二行
        stateTmp[1] = state[5];
        stateTmp[5] = state[9];
        stateTmp[9] = state[13];
        stateTmp[13] = state[1];

        //第三行
        stateTmp[2] = state[10];
        stateTmp[6] = state[14];
        stateTmp[10] = state[2];
        stateTmp[14] = state[6];

        //第四行
        stateTmp[3] = state[15];
        stateTmp[7] = state[3];
        stateTmp[11] = state[7];
        stateTmp[15] = state[11];

        return stateTmp;
    }

    /**
     * 列混淆变换
     *
     * @param state
     * @return
     */
    private byte[] MixColumns(byte[] state) {
        byte[] state_tmp = new byte[BLOCK_SIZE];

        for (int i = 0; i < 4; i++) {

            int t = 4 * i;

            state_tmp[t + 0] = (byte) (
                    GFMul(state[t + 0], 0x02)
                            ^ GFMul(state[t + 1], 0x03)
                            ^ GFMul(state[t + 2], 0x01)
                            ^ GFMul(state[t + 3], 0x01));

            state_tmp[t + 1] = (byte) (
                    GFMul(state[t + 0], 0x01)
                            ^ GFMul(state[t + 1], 0x02)
                            ^ GFMul(state[t + 2], 0x03)
                            ^ GFMul(state[t + 3], 0x01));

            state_tmp[t + 2] = (byte) (
                    GFMul(state[t + 0], 0x01)
                            ^ GFMul(state[t + 1], 0x01)
                            ^ GFMul(state[t + 2], 0x02)
                            ^ GFMul(state[t + 3], 0x03));

            state_tmp[t + 3] = (byte) (
                    GFMul(state[t + 0], 0x03)
                            ^ GFMul(state[t + 1], 0x01)
                            ^ GFMul(state[t + 2], 0x01)
                            ^ GFMul(state[t + 3], 0x02));
        }
        return state_tmp;
    }

    /**
     * 轮密钥加变换
     *
     * @param state
     * @param index
     */
    private void AddRoundKey(byte[] state, int index) {
        for (int i = 0; i < BLOCK_SIZE; i++) {
            state[i] = (byte) (state[i] ^ subKeys[i + index]);
        }
    }


    private byte[] AESEncrypt(byte[] state) {

        AddRoundKey(state, 0);

        int round = 1;

        while (round <= NumberOfRound - 1) {
            SubBytes(state);
            state = ShiftRows(state);
            state = MixColumns(state);
            AddRoundKey(state, BLOCK_SIZE * round);
            round++;
        }

        SubBytes(state);
        state = ShiftRows(state);
        AddRoundKey(state, BLOCK_SIZE * round);

//        Debug.error("AES加密", new String(state));
        return state;
    }

    public void CFBEncrypt(byte[] plain, byte[] cipher, byte[] iv, int len) {

        int offset = 0;
        byte[] ivCopy = new byte[BLOCK_SIZE];

        for (int i = 0; i < BLOCK_SIZE; i++) {
            ivCopy[i] = iv[i];
        }

        while (len >= BLOCK_SIZE) {
            ivCopy = AESEncrypt(ivCopy);

            for (int i = 0; i < BLOCK_SIZE; i++) {
                cipher[offset + i] = (byte) (plain[offset + i] ^ ivCopy[i]);
                ivCopy[i] = cipher[offset + i];
            }
            offset += BLOCK_SIZE;
            len -= BLOCK_SIZE;
        }
        if (len > 0) {

           ivCopy =  AESEncrypt(ivCopy);

            for (int i = 0; i < len; i++) {
                cipher[i + offset] = (byte) (plain[offset + i] ^ ivCopy[i]);
            }

        }

    }

    public void CFBDecrypt(byte[] cipher, byte[] plain, byte[] iv, int len) {

        int offset = 0;
        byte[] ivCopy = new byte[BLOCK_SIZE];

        for (int i = 0; i < BLOCK_SIZE; i++) {
            ivCopy[i] = iv[i];
        }

        while (len >= BLOCK_SIZE) {
            ivCopy = AESEncrypt(ivCopy);

            for (int i = 0; i < BLOCK_SIZE; i++) {
                plain[offset + i] = (byte) (cipher[offset + i] ^ ivCopy[i]);
                ivCopy[i] = cipher[offset + i];
            }
            offset += BLOCK_SIZE;
            len -= BLOCK_SIZE;
        }
        if (len > 0) {
           ivCopy= AESEncrypt(ivCopy);

            for (int i = 0; i < len; i++) {
                plain[offset + i] = (byte) (cipher[offset + i] ^ ivCopy[i]);
            }
        }

    }


}

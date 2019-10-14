
public class SM4 {
	private static final int[] Sbox = {
		0xd6, 0x90, 0xe9, 0xfe, 0xcc, 0xe1, 0x3d, 0xb7, 0x16, 0xb6, 0x14, 0xc2, 0x28, 0xfb, 0x2c, 0x05,
	    0x2b, 0x67, 0x9a, 0x76, 0x2a, 0xbe, 0x04, 0xc3, 0xaa, 0x44, 0x13, 0x26, 0x49, 0x86, 0x06, 0x99,
	    0x9c, 0x42, 0x50, 0xf4, 0x91, 0xef, 0x98, 0x7a, 0x33, 0x54, 0x0b, 0x43, 0xed, 0xcf, 0xac, 0x62,
	    0xe4, 0xb3, 0x1c, 0xa9, 0xc9, 0x08, 0xe8, 0x95, 0x80, 0xdf, 0x94, 0xfa, 0x75, 0x8f, 0x3f, 0xa6,
	    0x47, 0x07, 0xa7, 0xfc, 0xf3, 0x73, 0x17, 0xba, 0x83, 0x59, 0x3c, 0x19, 0xe6, 0x85, 0x4f, 0xa8,
	    0x68, 0x6b, 0x81, 0xb2, 0x71, 0x64, 0xda, 0x8b, 0xf8, 0xeb, 0x0f, 0x4b, 0x70, 0x56, 0x9d, 0x35,
	    0x1e, 0x24, 0x0e, 0x5e, 0x63, 0x58, 0xd1, 0xa2, 0x25, 0x22, 0x7c, 0x3b, 0x01, 0x21, 0x78, 0x87,
	    0xd4, 0x00, 0x46, 0x57, 0x9f, 0xd3, 0x27, 0x52, 0x4c, 0x36, 0x02, 0xe7, 0xa0, 0xc4, 0xc8, 0x9e,
	    0xea, 0xbf, 0x8a, 0xd2, 0x40, 0xc7, 0x38, 0xb5, 0xa3, 0xf7, 0xf2, 0xce, 0xf9, 0x61, 0x15, 0xa1,
	    0xe0, 0xae, 0x5d, 0xa4, 0x9b, 0x34, 0x1a, 0x55, 0xad, 0x93, 0x32, 0x30, 0xf5, 0x8c, 0xb1, 0xe3,
	    0x1d, 0xf6, 0xe2, 0x2e, 0x82, 0x66, 0xca, 0x60, 0xc0, 0x29, 0x23, 0xab, 0x0d, 0x53, 0x4e, 0x6f,
	    0xd5, 0xdb, 0x37, 0x45, 0xde, 0xfd, 0x8e, 0x2f, 0x03, 0xff, 0x6a, 0x72, 0x6d, 0x6c, 0x5b, 0x51,
	    0x8d, 0x1b, 0xaf, 0x92, 0xbb, 0xdd, 0xbc, 0x7f, 0x11, 0xd9, 0x5c, 0x41, 0x1f, 0x10, 0x5a, 0xd8,
	    0x0a, 0xc1, 0x31, 0x88, 0xa5, 0xcd, 0x7b, 0xbd, 0x2d, 0x74, 0xd0, 0x12, 0xb8, 0xe5, 0xb4, 0xb0,
	    0x89, 0x69, 0x97, 0x4a, 0x0c, 0x96, 0x77, 0x7e, 0x65, 0xb9, 0xf1, 0x09, 0xc5, 0x6e, 0xc6, 0x84,
	    0x18, 0xf0, 0x7d, 0xec, 0x3a, 0xdc, 0x4d, 0x20, 0x79, 0xee, 0x5f, 0x3e, 0xd7, 0xcb, 0x39, 0x48
	};
	private static final long[] FK = { 
		0xa3b1bac6L, 0x56aa3350L, 0x677d9197L, 0xb27022dcL 
		};
    private static final long[] CK = {
		0x00070e15L, 0x1c232a31L, 0x383f464dL, 0x545b6269L, 
		0x70777e85L, 0x8c939aa1L, 0xa8afb6bdL, 0xc4cbd2d9L, 
		0xe0e7eef5L, 0xfc030a11L, 0x181f262dL, 0x343b4249L, 
		0x50575e65L, 0x6c737a81L, 0x888f969dL, 0xa4abb2b9L, 
		0xc0c7ced5L, 0xdce3eaf1L, 0xf8ff060dL, 0x141b2229L, 
		0x30373e45L, 0x4c535a61L, 0x686f767dL, 0x848b9299L, 
		0xa0a7aeb5L, 0xbcc3cad1L, 0xd8dfe6edL, 0xf4fb0209L, 
		0x10171e25L, 0x2c333a41L, 0x484f565dL, 0x646b7279L 
        };
    private long[] X = new long[36];
    private long[] K = new long[36];	//存放密钥扩展
	private int[] M = new int[16];		//明文
	private int[] C = new int[16];		//密文
	private long[] result = new long[4];	//结果
	private int[] key = new int[16];	//密钥
    
    private long cycle_left (long a, int dist) {
    	return ((a << dist) & 0xffffffffL) | (a >> (32 - dist));
    }
    private int S_box (int a) {
    	return Sbox[a];
    }
    private long tao (long A) {
    	return ((long)S_box((int)((A >> 0) & 0xff)) << 0)
    		 ^ ((long)S_box((int)((A >> 8) & 0xff)) << 8)
    		 ^ ((long)S_box((int)((A >> 16) & 0xff)) << 16)
    		 ^ ((long)S_box((int)((A >> 24) & 0xff)) << 24);
    }
    private long L (long B) {
    	return B ^ cycle_left(B, 2) ^ cycle_left(B, 10) ^ cycle_left(B, 18) ^ cycle_left(B, 24);
    }
    private long T (long X) {
    	return L(tao(X));
    }
    private long L_inv (long B) {
    	return B ^ cycle_left(B, 13) ^ cycle_left(B, 23);
    }
    private long T_inv (long X) {
    	return L_inv(tao(X));
    }
    private void key_expansion() {
    	long[] MK = new long[4];
    	load_int2long(MK, key);
    	for (int i = 0; i < 4; i++) {
    		K[i] = MK[i] ^ FK[i];
    	}
    	for (int i = 0; i < 32; i++) {
    		K[i + 4] = K[i] ^ T_inv(K[i + 1] ^ K[i + 2] ^ K[i + 3] ^ CK[i]);
    	}
    }
    private long rk (int n) {
    	return K[n + 4];
    }
    private long F (long X0, long X1, long X2, long X3, long rk) {
    	return X0 ^ T(X1 ^ X2 ^ X3 ^ rk);
    }
    private void R () {
    	result[0] = X[35];
    	result[1] = X[34];
    	result[2] = X[33];
    	result[3] = X[32];
    }
    private void load_int2long (long[] B, int[] A) {
    	for (int i =0; i < 4; i++) {
    		B[i] = 0;
    		for (int j = 0; j < 4; j++) {
    			B[i] = B[i] << 8;
    			B[i] += A[i * 4 + j];
    		}
    	}
    }
    public void encrypt () {
    	key_expansion();
    	load_int2long(X, M);
    	for (int i =0; i < 32; i++) {
    		X[i + 4] = F(X[i], X[i + 1], X[i + 2], X[i + 3], rk(i));
    	}
    	R();
    }
    public void decrypt () {
    	key_expansion();
    	load_int2long(X, C);
    	for (int i =0; i < 32; i++) {
    		X[i + 4] = F(X[i], X[i + 1], X[i + 2], X[i + 3], rk(31 - i));
    	}
    	R();
    }
    public void setM (String str) {
		char[] s = str.toCharArray();
		int tmp;
		for (int i = 0; i < 16; i++) {
			tmp = 0;
			for (int j = 0; j < 2; j++) {
				if (j == 1) {
					tmp *= 16;
				}
				if (s[2 * i + j] <= '9') {
					tmp += s[2 * i + j] - '0';
				} else if (s[2 * i + j] <= 'F'){
					tmp += s[2 * i + j] - 'A' + 10;
				} else {
					tmp += s[2 * i + j] - 'a' + 10;
				} 
			}
			M[i] = tmp;
		}
	}
	public void setC (String str) {
		char[] s = str.toCharArray();
		int tmp;
		for (int i = 0; i < 16; i++) {
			tmp = 0;
			for (int j = 0; j < 2; j++) {
				if (j == 1) {
					tmp *= 16;
				}
				if (s[2 * i + j] <= '9') {
					tmp += s[2 * i + j] - '0';
				} else if (s[2 * i + j] <= 'F'){
					tmp += s[2 * i + j] - 'A' + 10;
				} else {
					tmp += s[2 * i + j] - 'a' + 10;
				} 
			}
			C[i] = tmp;
		}
	}
	public void setKey (String str) {
		char[] s = str.toCharArray();
		int tmp;
		for (int i = 0; i < 16; i++) {
			tmp = 0;
			for (int j = 0; j < 2; j++) {
				if (j == 1) {
					tmp *= 16;
				}
				if (s[2 * i + j] <= '9') {
					tmp += s[2 * i + j] - '0';
				} else if (s[2 * i + j] <= 'F'){
					tmp += s[2 * i + j] - 'A' + 10;
				} else {
					tmp += s[2 * i + j] - 'a' + 10;
				} 
			}
			key[i] = tmp;
		}
	}
	public String getResult () {
		String hexString = "";
		char[] hex = {
			'0', '1', '2', '3',
			'4', '5', '6', '7',
			'8', '9', 'a', 'b',
			'c', 'd', 'e', 'f'
		};
		StringBuilder ss = new StringBuilder();
		int bit;
		long temp;
		for (int i = 0; i < 4; i++) {
			for (int j = 7; j >= 0; j--) {
				temp = result[i] & (0x0f * (long)Math.pow(16, j));
				bit = (int) (temp >> (j * 4));
				ss.append(hex[bit]);
			}
		}
		hexString = ss.toString();
		return hexString;
	}
	public static void main(String argv[]) {
		SM4 sm = new SM4();
		sm.setM("0123456789ABCDEFFEDCBA9876543210");
		sm.setKey("0123456789ABCDEFFEDCBA9876543210");
		sm.encrypt();
		System.out.println(sm.getResult());
	}
}

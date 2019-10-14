public class DES {
	private static final int[] replace1_C = new int[] {
		57, 49, 41, 33, 25, 17,  9,
		 1, 58, 50, 42, 34, 26, 18,
		10,  2, 59, 51, 43, 35, 27,
		19, 11,  3, 60, 52, 44, 36};
	private static final int[] replace1_D = new int[] {
		63, 55, 47, 39, 31, 23, 15,
		 7, 62, 54, 46, 38, 30, 22,
		14,  6, 61, 53, 45, 37, 29,
		21, 13,  5, 28, 20, 12,  4};
	private static final int[] replace2 = new int[] {
		14, 17, 11, 24,  1,  5,
		 3, 28, 15,  6, 21, 10,
		23, 19, 12,  4, 26,  8,
		16,  7, 27, 20, 13,  2,
		41, 52, 31, 37, 47, 55,
		30, 40, 51, 45, 33, 48,
		44, 49, 39, 56, 34, 53,
		46, 42, 50, 36, 29, 32};
	private static final int[] cycle_bit = new int[] {
		1, 1, 2, 2, 2, 2, 2, 2,
		1, 2, 2, 2, 2, 2, 2, 1
	};
	private static final int[] replace_IP = new int[] {
		58, 50, 42, 34, 26, 18, 10, 2,
		60, 52, 44, 36, 28, 20, 12, 4,
		62, 54, 46, 38, 30, 22, 14, 6,
		64, 56, 48, 40, 32, 24, 16, 8,
		57, 49, 41, 33, 25, 17,  9, 1,
		59, 51, 43, 35, 27, 19, 11, 3,
		61, 53, 45, 37, 29, 21, 13, 5,
		63, 55, 47, 39, 31, 23, 15, 7};
	private static final int[] replace_P = new int[] {
		16,  7, 20, 21, 29, 12, 28, 17,
		 1, 15, 23, 26,  5, 18, 31, 10,
		 2,  8, 24, 14, 32, 27,  3,  9, 
		19, 13, 30,  6, 22, 11,  4, 25
	};
	private static final int[] replace_IPcontrary = new int[] {
		 4, 8, 48, 16, 56, 24, 64, 32,
		39, 7, 47, 15, 55, 23, 63, 31,
		38, 6, 46, 14, 54, 22, 62, 30, 
		37, 5, 45, 13, 53, 21, 61, 29, 
		36, 4, 44, 12, 52, 20, 60, 28, 
		35, 3, 43, 11, 51, 19, 59, 27,
		34, 2, 42, 10, 50, 18, 58, 26,
		33, 1, 41,  9, 49, 17, 57, 25};
	private static final int[] choose_E = new int[] {
		32,  1,  2,  3,  4,  5,
		 4,  5,  6,  7,  8,  9,
		 8,  9, 10, 11, 12, 13,
		12, 13, 14, 15, 16, 17,
		16, 17, 18, 19, 20, 21,
		20, 21, 22, 23, 24, 25,
		24, 25, 26, 27, 28, 29,
		28, 29, 30, 31, 32,  1};
	private static final int[][][] S = new int[][][] {
		{{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},//S1
		 {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
		 {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
		 {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}},
		
		{{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},//S2
		 {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
		 {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
		 {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}},
		
		{{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},//S3
		 {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
		 {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
		 {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}},
		
		{{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},//S4
		 {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
		 {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
		 {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}},
		
		{{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},//S5
		 {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
		 {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
		 {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}},
		
		{{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},//S6
		 {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
		 {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
		 {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}},
		
		{{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},//S7
		 {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
		 {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
		 {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}},
		
		{{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},//S8
		 {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
		 {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
		 {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}}
	};
	
	private int[] key = new int[64];
	private int[][] key_child = new int[16][48];
	private int[] M = new int[64];
	private int[] C = new int[64];
	private boolean debug = false;
	
	private void leftbit (int[] Array, int n) {
		for (int i = 0; i < n; i++) {
			int temp = Array[0];
			for (int j = 0; j < 27; j++) {
				Array[j] = Array[j + 1];
			}
			Array[27] = temp;
		}
	}
	private void HEX_to_int (String a, int b[]) {
		char[] A = a.toCharArray();
		int temp;
		for (int i = 0; i < 16; i++) {
			if (A[i] <= '9') {
				temp = A[i] - '0';
			} else if (A[i] <= 'F'){
				temp = A[i] - 'A' + 10;
			} else {
				temp = A[i] - 'a' + 10;
			} 
			b[i * 4 + 3] = temp & 1;
			b[i * 4 + 2] = (temp & 2) / 2;
			b[i * 4 + 1] = (temp & 4) / 4;
			b[i * 4] = (temp & 8) / 8;
		}
	}
	
	public void setKey (String KEY) {	//秘钥KEY 表现为一个16位16进制数
		HEX_to_int(KEY, key);
		int[] C0 = new int[28];
		int[] D0 = new int[28];
		for (int i = 0; i < 28; i++) {
			C0[i] = key[replace1_C[i] - 1];
			D0[i] = key[replace1_D[i] - 1];
		}
		
		for (int i = 0; i < 16; i++) {
			int n = cycle_bit[i];
			leftbit(C0, n);
			leftbit(D0, n);
				
			for (int j = 0; j < 48; j++) {
				if (replace2[j] <= 28) {
					key_child[i][j] = C0[replace2[j] - 1];
				} else {
					key_child[i][j] = D0[replace2[j] - 29];
				}
			}
		}
	}
	public void setC (String ciphertext) {	//密文cipherext 表现为一个16位16进制数
		HEX_to_int(ciphertext, C);
		int[][] L = new int[17][32];
		int[][] R = new int[17][32];
		for (int i = 0; i < 32; i++) {	//初始置换IP
			L[0][i] = C[replace_IP[i] - 1];
			R[0][i] = C[replace_IP[i + 32] - 1];
		}
		for (int i = 1; i <= 16; i++) {	//16次加密
			for (int j = 0; j < 32; j++) {	//对L赋值
				L[i][j] = R[i - 1][j];
			}
			int[] temp48 = new int[48];		//进行选择运算E将32位扩至48位
			for (int j = 0; j < 48; j++) {	
				temp48[j] = R[i - 1][choose_E[j] - 1] ^ key_child[16 - i][j];			
			}
			int[] temp32 = new int[32];	
			for (int j = 0; j < 8; j++) {	//8个S盒子
				int x, y, t;
				x = temp48[j * 6] * 2 + temp48[j * 6 + 5];
				y = temp48[j * 6 + 1] * 8 + temp48[j * 6 + 2] * 4 
						+ temp48[j * 6 + 3] * 2 + temp48[j * 6 + 4];
				t = S[j][x][y];
				temp32[j * 4] = (t & 8) / 8;
				temp32[j * 4 + 1] = (t & 4) / 4;
				temp32[j * 4 + 2] = (t & 2) / 2;
				temp32[j * 4 + 3] = t & 1;
			}
			int[] f_result = new int[32];
			for (int j = 0; j < 32; j++) {
				f_result[j] = temp32[replace_P[j] - 1];	//置换运算P
				R[i][j] = f_result[j] ^ L[i - 1][j];	//对R进行赋值
			}
			
			if (debug) {
				int l = 0;
				System.out.printf("L%d : ", i);
				while (l < 32) {
					System.out.print(L[i][l]);
					l++;
				}
				System.out.print('\n');
				l = 0;
				System.out.printf("R%d : ", i);
				while (l < 32) {
					System.out.print(R[i][l]);
					l++;
				}
				System.out.print('\n');
			}
		}	//16次加密完成
		for (int i = 0; i < 64; i++) {	//逆初始置换生成64位密文
			int k = replace_IPcontrary[i] - 1;
			if (k <= 31) {
				M[i] = R[16][k];
			} else {
				M[i] = L[16][k - 32];
			}
		}
	}
	public void setM (String plaintext) {	//明文plaintext 表现为一个16位16进制数
		HEX_to_int(plaintext, M);
		int[][] L = new int[17][32];
		int[][] R = new int[17][32];
		for (int i = 0; i < 32; i++) {	//初始置换IP
			L[0][i] = M[replace_IP[i] - 1];
			R[0][i] = M[replace_IP[i + 32] - 1];
		}
		for (int i = 1; i <= 16; i++) {	//16次加密
			for (int j = 0; j < 32; j++) {	//对L赋值
				L[i][j] = R[i - 1][j];
			}
			int[] temp48 = new int[48];		//进行选择运算E将32位扩至48位
			for (int j = 0; j < 48; j++) {	
				temp48[j] = R[i - 1][choose_E[j] - 1] ^ key_child[i - 1][j];			
			}
			int[] temp32 = new int[32];	
			for (int j = 0; j < 8; j++) {	//8个S盒子
				int x, y, t;
				x = temp48[j * 6] * 2 + temp48[j * 6 + 5];
				y = temp48[j * 6 + 1] * 8 + temp48[j * 6 + 2] * 4 
						+ temp48[j * 6 + 3] * 2 + temp48[j * 6 + 4];
				t = S[j][x][y];
				temp32[j * 4] = (t & 8) / 8;
				temp32[j * 4 + 1] = (t & 4) / 4;
				temp32[j * 4 + 2] = (t & 2) / 2;
				temp32[j * 4 + 3] = t & 1;
			}
			int[] f_result = new int[32];
			for (int j = 0; j < 32; j++) {
				f_result[j] = temp32[replace_P[j] - 1];	//置换运算P
				R[i][j] = f_result[j] ^ L[i - 1][j];	//对R进行赋值
			}
			
			if (debug) {
				int l = 0;
				System.out.printf("L%d : ", i);
				while (l < 32) {
					System.out.print(L[i][l]);
					l++;
				}
				System.out.print('\n');
				l = 0;
				System.out.printf("R%d : ", i);
				while (l < 32) {
					System.out.print(R[i][l]);
					l++;
				}
				System.out.print('\n');
			}
		}	//16次加密完成
		for (int i = 0; i < 64; i++) {	//逆初始置换生成64位密文
			int k = replace_IPcontrary[i] - 1;
			if (k <= 31) {
				C[i] = R[16][k];
			} else {
				C[i] = L[16][k - 32];
			}
		}
	}
	public int[] get_key_child (int n) {	//返回第 n 个子秘钥
		return key_child[n - 1];
	}
	public int[] get_M () {		//返回明文
		return M;
	}
	public int[] get_C () {		//返回密文
		return C;
	}
	public void setDebug (boolean a) { //设置是否显示L、R信息
		debug = a;
	}
	public static void hex_output (int a[]) {
		for (int i = 0; i < 16; i++) {
			int hex = 0;
			hex = 8 * a[i * 4] + 4 * a[i * 4 + 1] + 2 * a[i * 4 + 2] + a[i * 4 + 3];
			System.out.printf("%x", hex);
		}
		System.out.print('\n');
	}
	public static String str_out (int a[]) {
		char[] c = new char[16];
		for (int i = 0; i < 16; i++) {
			int hex = 0;
			hex = 8 * a[i * 4] + 4 * a[i * 4 + 1] + 2 * a[i * 4 + 2] + a[i * 4 + 3];
			if (hex < 10) {
				c[i] = (char)('0' + hex);
			} else {
				c[i] = (char)('a' + hex - 10);
			}
		}
		return String.valueOf(c);
	}
}

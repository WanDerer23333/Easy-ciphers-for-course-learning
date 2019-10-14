# Easy-ciphers-for-course-learning
常见加密算法的简单实现：DES/AES/SM4

## DES/AES/SM4
三个分组密码仅实现了最简单的加解密功能：
- DES 64位
- AES 128位
- SM4 128位

使用方法统一为：
1. 声明对应算法的对象，如：DES des = new DES（）;
2. 在该对象中通过 setM, setC, setKey 方法，设置明文、密文及密钥
3. 获取结果的方法：
   - AES、SM4：设置好运算所需的密钥以及明文或密文，使用 encrypt 或 decrypt 运算，使用 getResult 获得字符串结果
   - DES 需要先设置密钥，之后在设置明文或密文时会直接运算出结果，通过 get_C 或 get_M 取得对应的数组，再使用 hex_output 打印或者 str_out 转为字符串


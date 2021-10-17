/**
This code is part of a Network Security Project

Author : Rishabh Saxena
Roll no : 2019129
IIITDM JABALPUR

This class performs Simplied-AES transformations on a 16-bit input with the help of a 16 bit key

The key feature of this class is that even though the transformations are for a 16-bit input only(based on the Simplified-AES algo) but 
it can perform encryption/decryption for any size of input. It does this by dividing the input into indvidual 16-bit values 
and then perform the operation

Encryption for any length of plaintext :
    i)if the length of plaintext string is odd, 
      then append the null character to the start(eg: "hello" -> "\0hello" [\0 = nullcharacter])
    ii) divide the whole input string to digrams(eg: "\0hello" - > "\0h","el","lo")
    iii) initiate a list which will store the ciphertexts of each digram
    iv) for each digram formed
         1-store the ascii of the two characters in digram separately("el" - > [01100101,01101100])
         2- initiate a 16-bit variable plaintext_digram and assign the value of first character's ascii to the left 8 bits 
            and
            assign the value of second character's ascii to the right 8 bits (16 bit plaintext_digram = 011001010110110)
         3- Encrypt the plaintext digram using Simplified AES and append its value to the list
         4- perform this operation for each digram
    v) Join the the list which contains the ciphertexts of digrams as a string , which are seperated by a delimiter("-")
        [eg: if ciphertext_list= [3,4,5] = >final ciphertext string "3-4-5-"]   
    vi)Return the cipher text formed

    Decryption for any length of cipher text :
    i) Seperate the ciphertext with the delimiter to form a list of ciphertexts(eg ciphertext "3-4-5-" = > cipher_text_list=[3,4,5])
    ii)initiate a string which wii store the plaintext
    iii)for value in cipher_text_list 
            1-Decrypt the ciphertxt value
            2-Extract the ascii of individual characters of a digram
                (plaintext_digram = 011001010110110  --->   [01100101,01101100] )
            3-convert the ascii values to characters([01100101,01101100] --> "el")
            4-add the character to the string of plaintext
            5-Repeat for every value in the cipher_text_list
    iv) If the first character of the formed plaintext is null('\0'),remove it
    v)return plaintext
    

**/
import java.util.*;
import static java.util.Map.entry;   

class AES_variant{

    /*
nibble<->S-box(nibble)     nibble<->S-box(nibble)    
  
  0000<-> 1001              1000<-> 0110
  0001<-> 0100              1001<-> 0010
  0010<-> 1010              1010<-> 0000
  0011<-> 1011              1011<-> 0011
  0100<-> 1101              1100<-> 1100
  0101<-> 0001              1101<-> 1110
  0110<-> 1000              1110<-> 1111
  0111<-> 0101              1111<-> 0111
    */
    private static Map<Integer, Integer> s_box = Map.ofEntries(
    entry(0b0000, 0b1001),
    entry(0b0001, 0b0100),
    entry(0b0010, 0b1010),
    entry(0b0011, 0b1011),
    entry(0b0100, 0b1101),
    entry(0b0101, 0b0001),
    entry(0b0110, 0b1000),
    entry(0b0111, 0b0101),
    entry(0b1000, 0b0110),
    entry(0b1001, 0b0010),
    entry(0b1010, 0b0000),
    entry(0b1011, 0b0011),
    entry(0b1100, 0b1100),
    entry(0b1101, 0b1110),
    entry(0b1110, 0b1111),
    entry(0b1111, 0b0111)
   
    );

    private static Map<Integer, Integer> s_box_inverse = Map.ofEntries(
    entry(0b0000, 0b1010),
    entry(0b0001, 0b0101),
    entry(0b0010, 0b1001),
    entry(0b0011, 0b1011),
    entry(0b0100, 0b0001),
    entry(0b0101, 0b0111),
    entry(0b0110, 0b1000),
    entry(0b0111, 0b1111),
    entry(0b1000, 0b0110),
    entry(0b1001, 0b0000),
    entry(0b1010, 0b0010),
    entry(0b1011, 0b0011),
    entry(0b1100, 0b1100),
    entry(0b1101, 0b0100),
    entry(0b1110, 0b1101),
    entry(0b1111, 0b1110)
   
    );

    private int[][] mix_columns_matrix={{1,4},
                                        {4,1}};
    private int[][] mix_columns_matrix_inverse={{9,2},
                                                {2,9}};               
                                                
    //sunstitute nibbles for AES encryption
    private int[][] substituteNibbles(int[][] input){
        int s00_sub_nibble=s_box.get(input[0][0]);
        int s01_sub_nibble=s_box.get(input[0][1]);
        int s10_sub_nibble=s_box.get(input[1][0]);
        int s11_sub_nibble=s_box.get(input[1][1]);
        int [][] ar={
                {s00_sub_nibble, s01_sub_nibble},
                {s10_sub_nibble, s11_sub_nibble}
        };
        return ar;
    }

     //sunstitute nibbles for AES decryption
    private int[][] substituteNibblesInverse(int[][] input){
        int s00_sub_nibble=s_box_inverse.get(input[0][0]);
        int s01_sub_nibble=s_box_inverse.get(input[0][1]);
        int s10_sub_nibble=s_box_inverse.get(input[1][0]);
        int s11_sub_nibble=s_box_inverse.get(input[1][1]);
        int [][] ar={
                {s00_sub_nibble, s01_sub_nibble},
                {s10_sub_nibble, s11_sub_nibble}
        };
        return ar;
    }

    //shift rows operation(common for encryption and decryption)
    private int[][] shiftRows(int[][] input){
        /*
        input[0,0]->input[0,0]
        input[0,1]->input[0,1]
        input[1,0]->input[1,1]
        input[1,1]->input[1,0]

        */
        int [][] ar={
            {input[0][0], input[0][1]},
            {input[1][1], input[1][0]}
         };
    return ar;

    }

    //function to multiply polymomials used in mix_columns of AES encryption and decryption
    private static int multiplyGF(int val_0,int val_1){
        int p=0;
        while(val_1!=0){
            if((val_1& 0b1)!=0){
                p^=val_0;
            }
            val_0<<=1;
            if((val_0&0b10000)!=0){
                val_0^=0b11;

            }
            val_1>>=1;
        }
        return p & 0b1111;
    }

    //mix columns in AES encryption
    private int[][] mixColumns(int[][] input){
       
       
        int [][] ar={
            {(input[0][0]^multiplyGF(mix_columns_matrix[0][1], input[1][0])),(input[0][1]^multiplyGF(mix_columns_matrix[0][1], input[1][1]))},
            {(input[1][0]^multiplyGF(mix_columns_matrix[1][0], input[0][0])),(input[1][1]^multiplyGF(mix_columns_matrix[1][0], input[0][1]))}
        };

        
        return ar;
    }

    //mix columns in AES Decryption
    private int[][] mixColumnsInverse(int[][] input){
       
       
        int [][] ar={
            {(multiplyGF(mix_columns_matrix_inverse[0][0],input[0][0])^multiplyGF(mix_columns_matrix_inverse[0][1], input[1][0])),
             (multiplyGF(mix_columns_matrix_inverse[0][0],input[0][1])^multiplyGF(mix_columns_matrix_inverse[0][1], input[1][1]))},
            {(multiplyGF(mix_columns_matrix_inverse[1][1],input[1][0])^multiplyGF(mix_columns_matrix_inverse[1][0], input[0][0])),
             (multiplyGF(mix_columns_matrix_inverse[1][1],input[1][1])^multiplyGF(mix_columns_matrix_inverse[1][0], input[0][1]))}
        };

        
        return ar;
    }


    //Function to print binary of any number base 10
    public static void toBinary(int decimal){    
        int binary[] = new int[40];    
        int index = 0;    
        while(decimal > 0){    
          binary[index++] = decimal%2;    
          decimal = decimal/2;    
        }    
        for(int i = index-1;i >= 0;i--){    
          System.out.print(binary[i]);    
        }    
   System.out.println();//new line  
   }   
   
   //function to extrac 4-4 bits from a 16-bit input 
    public int[] extractBits(int key){
        int i=4;
        int [] four_bit=new int[4];
        int mask =~(-1<<4); // to extract the last four bits a 16 bit number--- ..00001111
       
        for(int j=0;j<i;j++){
            four_bit[j]=key & mask;
            key>>=i;
            
        }
        return four_bit;

    }

    //the g function used in the key expansion of AES encryption and decryption
    private static int key_expansion_g_function(int word, int round_num){
        int mask1=~(-1<<4);// to extract the last four bits a 16 bit number--- ..000000001111
        int mask2=mask1<<4;// to extract the last four bits a 16 bit number--- ..000011110000
        int n0=(word & mask2)>>4;
        int n1=(word & mask1);
        //int rot_word = n1<<4 & n0>>4;
        //rotation and substitution
        int n0_dash =s_box.get(n1);
        int n1_dash = s_box.get(n0);

        int rot_sub_word =  getWord(n0_dash, n1_dash);
        int r_con=0;
        if(round_num == 1){
            r_con = 128; //1000 0000

        }
        else if(round_num == 2){
            r_con = 48; //0011 0000

        }
        int w_dash = rot_sub_word ^ r_con;
        return w_dash;

    }

    //to get 8-bit word
    private static int getWord(int word0,int word1){
        return (word0<<4)|word1;
    }

    //expands keys 
    private int[] expandKey(int[][] original_key){
        //word 0 and 1
        int word0=getWord(original_key[0][0], original_key[1][0]);
        int word1=getWord(original_key[0][1], original_key[1][1]);
        //word 2 and 3
        int word_1_g = key_expansion_g_function(word1, 1);
        int word2 = word0 ^ word_1_g;
        int word3 = word1 ^ word2;
        //word 4 and 5
        int word_3_g = key_expansion_g_function(word3, 2);
        int word4= word2 ^ word_3_g;
        int word5 = word3 ^ word4;

        int []  words_final = {word0, word1, word2, word3, word4, word5};
        return words_final;



    }

    //function to add round key in aes encryption and decryption
    private int[][] addRoundKey(int[][] plaintext, int word0, int word1){
        int[] roundWord=extractBits((word0<<8) | word1);
       
        AES_variant ob=new AES_variant();
        int[][] roundWord_matrix = ob.arrangeMatrix(roundWord);
      
        int[][] transformed=new int[2][2];
        for(int i=0;i<2;i++){
            for(int j=0;j<2;j++){
                transformed[i][j] = plaintext[i][j]^roundWord_matrix[i][j];
            }
        }
        return  transformed;
    }

    //arranges the 4-4 bit array in the format expected by AES algorithm
    public int[][] arrangeMatrix(int[] arr){
        int[][] matrix = {
            {arr[3],arr[1]},
            {arr[2],arr[0]}
        };
        return matrix;
    }

   //function to print matrix
    public void printMatrixBinary(int[][] mat){
        for(int i=0;i<mat.length;i++){
            for(int j=0;j<mat[0].length;j++){
                toBinary(mat[i][j]);
            }
        }
    }

    //pre round transformation in AES Encryption
    private int[][] preRoundTransformation(int[][] input, int key_0, int key_1 ){
        
        AES_variant ob=new AES_variant();
        int[][] pre_round_input = ob.addRoundKey(input, key_0, key_1);
        return pre_round_input;
        
    }

    //Round 1 encryption AES
    private int[][] round1Encryption(int[][] plaintext_after_pre_round_transformation, int key_2, int key_3){
        //substitute nibbles
        AES_variant ob=new AES_variant();
        int [][] substitutedNibbles = ob.substituteNibbles(plaintext_after_pre_round_transformation);
        //shift rows
        int [][] shift_rows = ob.shiftRows(substitutedNibbles);
        //mix columns
        int [][] mix_cols = ob.mixColumns(shift_rows);
        
        //add round key
        int [][] round_1key_add = ob.addRoundKey(mix_cols, key_2, key_3);
        
        int[][] state_end_round_1 = round_1key_add;
        return state_end_round_1;
    }

    //Round 2 encryption AES
    private int[][] round2Encryption(int[][] state_end_round_1, int key_4, int key_5){
        //substitute nibbles
        AES_variant ob=new AES_variant();
        int [][] substitutedNibbles = ob.substituteNibbles(state_end_round_1);
        //shift rows
        int [][] shift_rows = ob.shiftRows(substitutedNibbles);
        
        
        //add round key
        int [][] round_2key_add = ob.addRoundKey(shift_rows, key_4, key_5);
        
        int[][] state_end_round_2 = round_2key_add;
        return state_end_round_2;
    }
    //returns a 16-bit integer from a 4-4 bit 2x2 matrix
    public int returnSimplifiedMatrix(int [][] matrix){
        return matrix[0][0]<<12 | matrix[1][0]<<8 | matrix[0][1]<<4 | matrix[1][1];
    }

    //encrypts a 16-bit integer
    public int EncryptAES(int plaintext,int key){
        AES_variant ob=new AES_variant();
        
       
        int[] extractedBitsKey = ob.extractBits(key);
        
        //key expansion
        int[][] original_key = ob.arrangeMatrix(extractedBitsKey);
        int[] key_expanded = ob.expandKey(original_key);

        
        // Preround transformation
        int[] plaintext_bits = ob.extractBits(plaintext);
        int [][] plaintext_bits_matrix = ob.arrangeMatrix(plaintext_bits);
        
        int[][] pre_round_plaintext =ob.preRoundTransformation(plaintext_bits_matrix, key_expanded[0], key_expanded[1]);
        
        
        //round 1
        int[][] state_end_round_1 =ob.round1Encryption(pre_round_plaintext,  key_expanded[2], key_expanded[3]);
        
        //round 2

        int[][] state_end_round_2 =ob.round2Encryption(state_end_round_1,  key_expanded[4], key_expanded[5]);
        
        int ciphertext = ob.returnSimplifiedMatrix(state_end_round_2);
        return ciphertext;



    }

    //decryption round 1
    private int[][] round1Decryption(int[][] ciphertext_bits_matrix, int key4,int key5){
        //round 1 inverse
        //add round key
        AES_variant ob=new AES_variant();
        int [][] add_round1_key_inverse = ob.addRoundKey(ciphertext_bits_matrix, key4, key5);
        //shift rows
        int [][] shift_rows_round1_inverse = ob.shiftRows(add_round1_key_inverse);
        //substitute
        int [][] substitutedNibbles_round1_inverse = ob.substituteNibblesInverse(shift_rows_round1_inverse);
        int [][] state_end_round1_inverse = substitutedNibbles_round1_inverse;
        return state_end_round1_inverse;

    }

    //decryption round 2
    private int[][] round2Decryption(int[][] state_end_round1_inverse, int key2,int key3){
        //round 1 inverse
        //add round key
        AES_variant ob=new AES_variant();
        //round 2 inverse
        // add round key
        int [][] add_round2_key_inverse = ob.addRoundKey(state_end_round1_inverse,key2, key3);
        //mix columns
        int [][] mix_col_round2_inverse = ob.mixColumnsInverse(add_round2_key_inverse);
        //shift rows
        int [][] shift_rows_round2_inverse = ob.shiftRows(mix_col_round2_inverse);
        //substitute 
        int [][] substitutedNibbles_round2_inverse = ob.substituteNibblesInverse(shift_rows_round2_inverse);
        int [][] state_end_round2_inverse=substitutedNibbles_round2_inverse;
        return state_end_round2_inverse;

    }
    //last transformation of adding key0,key1 in decryption
    private int[][] postRoundTransformation(int[][] state_end_round2_inverse, int key0,int key1){
        AES_variant ob=new AES_variant();
        int[][] post_round_plaintext =ob.preRoundTransformation(state_end_round2_inverse, key0, key1);
        int[][] plaintext_formed = post_round_plaintext;
        return plaintext_formed;
    }
    //encrypts 16-bit ciphertext and returns plaintext
    public int DecryptAES(int ciphertext,int key){
        AES_variant ob=new AES_variant();
        
       
        int[] extractedBitsKey = ob.extractBits(key);
        
        //key expansion
        int[][] original_key = ob.arrangeMatrix(extractedBitsKey);
        int[] key_expanded = ob.expandKey(original_key);

        
        
        int[] ciphertext_bits = ob.extractBits(ciphertext);
        int [][] ciphertext_bits_matrix = ob.arrangeMatrix(ciphertext_bits);
        
        
        
        
        //round 1 inverse
        
        int [][] state_end_round1_inverse = ob.round1Decryption(ciphertext_bits_matrix, key_expanded[4],key_expanded[5]);

        //round 2 inverse
       
        int [][] state_end_round2_inverse = ob.round2Decryption(state_end_round1_inverse, key_expanded[2],key_expanded[3]);

        //post round transformation
        int[][] plaintext_formed = ob.postRoundTransformation(state_end_round2_inverse,  key_expanded[0],key_expanded[1]);
        
        int plaintext = ob.returnSimplifiedMatrix(plaintext_formed);
        return plaintext;
        



    }

    //Encrypts any length of string plaintext 
    public String EncryptAES(String input_plaintext, int key){
        int length_input_plaintext = input_plaintext.length();
        
       
        ArrayList<Integer> array_cipher = new ArrayList<>();
        AES_variant ob_test=new AES_variant();
        // i)if the length of plaintext string is odd, 
        // then append the null character to the start(eg: "hello" -> "\0hello" [\0 = nullcharacter])
        if(length_input_plaintext%2!=0){
            input_plaintext='\0'+input_plaintext;
        }
        for(int i=0;i<length_input_plaintext;i+=2){
            //get digram
            String current_16_bits = input_plaintext.substring(i, i+2);
            /*initiate a 16-bit variable plaintext_digram and assign the value of first character's ascii to the left 8 bits 
            and
            assign the value of second character's ascii to the right 8 bits (16 bit plaintext_digram = 011001010110110)
            Encrypt the plaintext digram using Simplified AES and append its value to the list*/
            int plaintext_current_16_bits = ((int)current_16_bits.charAt(0))<<8  | (int)current_16_bits.charAt(1);
            int ciphertext_current_16_bits = ob_test.EncryptAES(plaintext_current_16_bits, key);
            array_cipher.add(ciphertext_current_16_bits);
        }

        //form a ciphertext string
        StringBuilder ciphertext_stringBuilder= new StringBuilder();
        for(int i=0;i<array_cipher.size();i++){
            String temp = "" + array_cipher.get(i)+'-';
            ciphertext_stringBuilder.append(temp);
        }
        String ciphertext_string = ciphertext_stringBuilder.toString();
        
        return ciphertext_string;

    }

    //Decrypts any length of string cipher 
    public String DecryptAES( String ciphertext_string, int key){
        ArrayList<Integer> ciphertexts = new ArrayList<>();
        String delimiter = "-";
        
        for(int i=0;i<ciphertext_string.length() -1;i++){
            //get the ending index of an individual enciphered digram frame
            int index_delimiter = ciphertext_string.indexOf(delimiter, i);
            String cipher_current_string = ciphertext_string.substring(i, index_delimiter);
            //get individual ciphertext
            int cipher_current = Integer.parseInt(cipher_current_string);
            ciphertexts.add(cipher_current);
            i=index_delimiter;
        }
        StringBuilder plaintext_final=new StringBuilder();
        AES_variant ob_test=new AES_variant();
        //for each ciphertext decrypt and form the plaintext
        for(int i=0;i<ciphertexts.size();i++){
            //decrypt
            int ciphertext_16_bits = ciphertexts.get(i);
            int plaintext_16_bits =ob_test.DecryptAES(ciphertext_16_bits, key);
           //convert to string and add
            String plaintext_current_16bit_string = ""+(char)(plaintext_16_bits>>8) +((char)(plaintext_16_bits & ~(-1<<8)));
            plaintext_final.append(plaintext_current_16bit_string);


        }
        //return final plaintext
        String plaintext_final_string = plaintext_final.toString();
        if(plaintext_final_string.charAt(0)=='\0'){
            plaintext_final_string=plaintext_final_string.substring(1);
        }
        
        return plaintext_final_string;
    }
}
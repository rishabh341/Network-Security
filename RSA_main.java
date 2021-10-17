/**
This code is part of a Network Security Project

Author : Rishabh Saxena
Roll no : 2019129
IIITDM JABALPUR

This class performs RSA encryption and decryption on any given input. First a set of private/public key is generated with the help of 
RSA_key_gen.java.  The class provides various functionalities, meaning you can encrypt or decrypt data with your own public/private key 
or provide the key pair manually.

Feature of this class is that it can encrypt/decrypt any size of plaintext using byte stuffing method.

Encryption :

        for each character in plaintext
            i)  the ascii value is retrieved (an integer)
            ii) enchipher the ascii value with RSA with the key(cipher)
            iii)find the number of digits in enciphered value(num_dig)
            iv) create a string = string(num_dig) + string(cipher) [byte stuffing method]
            v) Add the created string to the ciphertext string
            vi) repeat these steps for every character
            return the ciphertext

Decryption : 
        
        set the index_byte_stuff = 0 [first character of the ciphertext which denotes the length of ciphertext for a character formed during encryption]
        while index_byte_stuff < length of ciphertext
            i) store the substring starting from 'index_byte_stuff + 1' to the value of ciphertext[index_byte_stuff]
            ii)Decipher the integer value of the extracted substring with RSA algo(the ascii value a character)
            iii)convert the extracted ascii value to character
            iv) add the character to the plaintext string
            v)set the index_byte_stuff = integer(ciphertext[index_byte_stuff]) //set the index to the starting of next frame of data
        
        return plaintext

**/



class RSA_main {
    private RSA_key_gen parameters;
    private long[] priv_key;
    long[] pub_key;
    private long d;
    long e,n;
    //constructor call automatically generates the keys
    public RSA_main(){
        parameters=new RSA_key_gen();
        long[][] key_para=parameters.generateKeyParameters();
        priv_key=key_para[1];
        pub_key=key_para[0];
        d=priv_key[0];
        e=pub_key[0];
        n=pub_key[1];


    }
    //for setting the key manually
    //it is advised not to use this as the automatically generated parameters follow every mathematical rule strictly
    //allows to set the parameters manually 
    //!!!!! if using this then one must be entirely sure that the parameters follow all the mathematical conditions
    public void setParameters(long p,long q,long e){
        this.e = e;
        this.n = p*q;
        this.d = parameters.returnD(p, q, e);
        this.priv_key[0] = this.d;
        this.priv_key[1] = this.n;
        this.pub_key[0] = this.e;
        this.pub_key[1] = this.n;

    }

    //to get access to your private key
    public int getPrivateKey(){
        return (int)d;
    }

    //enciphers the plaintext using user's own public key
    public String Encipher(String plaintext){
        StringBuilder encryptedText=new StringBuilder();

        //convert the string plaintext to a character array
        char[] plaintext_char= plaintext.toCharArray();

        /*
            for each character in plaintext
            i)  the ascii value is retrieved (an integer)
            ii) enchipher the ascii value with RSA with the key(cipher)
            iii)find the number of digits in enciphered value(num_dig)
            iv) create a string = string(num_dig) + string(cipher) [byte stuffing method]
            v) Add the created string to the ciphertext string
            vi) repeat these steps for every character
            return the ciphertext
        */
        for(char i : plaintext_char){
            //convert to ascii
            int ascii_val=(int)i;
            //encrypt
            long M=(long)ascii_val;
            //cipher using RSA algo
            long cipher=1;
            for(int j=1;j<=(int)e;j++){
                cipher=(cipher*M)%n;
            }
            //count digits in the cipher text
            int dig_count=0;
            long temp=cipher;
            while(temp!=0){
                temp=temp/10;
                dig_count++;
            }
            //add the cipher text to encryptedText string
            String cipherString=""+dig_count+""+cipher;
            encryptedText.append(cipherString);
        }
        //return string
        String encrypted=encryptedText.toString();
        return encrypted;
    }

    
    //enciphers a plaintext using an external key
    public String Encipher(String plaintext, long e, long n){
        StringBuilder encryptedText=new StringBuilder();
        //convert plaintext to array of characters
        char[] plaintext_char= plaintext.toCharArray();
        for(char i : plaintext_char){
            //convert to ascii value
            int ascii_val=(int)i;
            //encrypt
            long M=(long)ascii_val;
            //cipher
            long cipher=1;
            for(int j=1;j<=(int)e;j++){
                cipher=(cipher*M)%n;
            }
            //count digits
            int dig_count=0;
            long temp=cipher;
            while(temp!=0){
                temp=temp/10;
                dig_count++;
            }
            //create the individual cipher using byte stuffing
            String cipherString=""+dig_count+""+cipher;
            encryptedText.append(cipherString);
        }
        //return ciphertext
        String encrypted=encryptedText.toString();
        return encrypted;
    }

    //decrypts using the user's own private key

    public String Decipher(String ciphertext){

        /*
        set the index_byte_stuff = 0 [first character of the string]
        while index_byte_stuff < length of ciphertext
            i) store the substring starting from 'index_byte_stuff + 1' to the value of ciphertext[index_byte_stuff]
            ii)Decipher the integer value of the extracted substring with RSA algo(the ascii value a character)
            iii)convert the extracted ascii value to character
            iv) add the character to the plaintext string
            v)set the index_byte_stuff = integer(ciphertext[index_byte_stuff]) //set the index to the starting of next frame of data
        
        return plaintext
        */
        StringBuilder plainText=new StringBuilder();
        long ciphertext_len=ciphertext.length();
        //[first character of the string]
        int current_index=0;
        while(current_index<ciphertext_len){
            //find the length/num_dig of the ciphertext of a character
            char len_token=ciphertext.charAt(current_index);

            //find the end index for cipher
            int end_index=current_index + (int)len_token -48 + 1;

            //extract value
            String token_cipher=ciphertext.substring(current_index+1, end_index);
            long  value=Long.parseLong(token_cipher);

            //update the index to the index of next enciphered frame
            current_index = end_index;
            //decipher with RSA
            long decipher=1;
            for(int i=1;i<=(int)d;i++){
                decipher=(decipher*value)%n;
            }
            //add to the plaintext
            char decipher_char=(char)decipher;
            plainText.append(""+decipher_char);

        }
        //return plaintext
        String decrypted=plainText.toString();
        return decrypted;
    }

    //decipher using a manually provided key
    public String Decipher(String ciphertext, long e, long n){
        StringBuilder plainText=new StringBuilder();
        long ciphertext_len=ciphertext.length();
        //[first character of the string]
        int current_index=0;
        while(current_index<ciphertext_len){

            //find the length/num_dig of the ciphertext of a character
            char len_token=ciphertext.charAt(current_index);

            //find the end index for cipher
            int end_index=current_index + (int)len_token -48 + 1;

            //extract value
            String token_cipher=ciphertext.substring(current_index+1, end_index);
            long  value=Long.parseLong(token_cipher);
            //update the index to the index of next enciphered frame
            current_index = end_index;

            //decipher using RSA
            long decipher=1;
            for(int i=1;i<=(int)e;i++){
                decipher=(decipher*value)%n;
            }
            char decipher_char=(char)decipher;
            plainText.append(""+decipher_char);

        }
        //return plaintext
        String decrypted=plainText.toString();
        return decrypted;
    }
    
}

# Network Security Project
## Rishabh Saxena
### _2019129 CSE IIITDMJ_  


## Overview 


##### This is an client-server encryption/decryption system for secure communication. The system uses multiple encryption algorithms like - RSA and Simplified-AES. The entire code is written in 'java'. The key features of this entire system are :

 - Uses the concepts of Object Oriented Programming in Java
 - No dependency on any external package/library
 - Can handle the encryption and decryption of any length of input 

### The description of files are as follows :
 	
##	1-Extended_gcd.java
          
          This class calulates the GCD of two numbers using the extended_gcd algorithm and returns the array with {gcd, s, t}(values of extended gcd)

##	2-Prime_store.java
	   This class stores the prime numbers upto 1000 using the Sieve of Eratothenes algorithm. The class has functions to return two random prime numbers. Key features :
                  a- Uses Sieve of eratothenes algorithm
		          b- Returns two random prime numbers >=500
                               or
                     return two random numbers within a specified range

##	3-RSA_key_gen.java
	   This class generates public key + private key pair for a user using the services of Extended_gcd.java & Prime_store.java.
        It can generate keys in two ways :
            a-automatic creation
            b-user manually gives the parameters(p,q,e)[ the user can invoke the function returnD(long p,long q, long e)]
## 4-RSA_main.java
        This class performs RSA encryption and decryption on any given input. First a set of private/public key is generated with the help of  RSA_key_gen.java.  The class provides various functionalities, meaning you can encrypt or decrypt data with your own public/private key  or provide the key pair manually.

     A major feature of this class is that it can encrypt/decrypt any size of plaintext using byte stuffing method.

- #### _Encryption in RSA for any length plaintext_ :

        for each character in plaintext
            i)  the ascii value is retrieved (an integer)
            ii) enchipher the ascii value with RSA with the key(cipher)
            iii)find the number of digits in enciphered value(num_dig)
            iv) create a string = string(num_dig) + string(cipher) [byte stuffing method]
            v) Add the created string to the ciphertext string
            vi) repeat these steps for every character
            return the ciphertext

- #### _Decryption in RSA for any length ciphertext_ : 
        
        set the index_byte_stuff = 0 
       //[first character of the ciphertext which denotes the length of ciphertext for a character formed during encryption]
        while index_byte_stuff < length of ciphertext
            i) store the substring starting from 'index_byte_stuff + 1' to the value of ciphertext[index_byte_stuff]
            ii)Decipher the integer value of the extracted substring with RSA algo(the ascii value a character)
            iii)convert the extracted ascii value to character
            iv) add the character to the plaintext string
            v)set the index_byte_stuff = integer(ciphertext[index_byte_stuff]) //set the index to the starting of next frame of data
        
        return plaintext

## Hash_md5.java 
    This class find the hash digest for a given Input using the MD5 hashing algorithm
    



## AES_variant.java
    This class performs Simplied-AES transformations on a 16-bit input with the help of a 16 bit key
    
    The key feature of this class is that even though the transformations are for a 16-bit input only(based on the Simplified-AES algo) but,
    it can perform encryption/decryption for any size of input. It does this by dividing the input into indvidual 16-bit values 
    and then perform the operation
    
- #### Encryption for any length of plaintext :
        i)if the length of plaintext string is odd, 
          then append the null character to the start(eg: "hello" -> "\0hello" [\0 =  nullcharacter])
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
    
- #### Decryption for any length of cipher text :
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


## Client.java
    Communicates with the server using TCP protocol and uses the RSA_main,java, AES_variant.java and Hash_md5.java for the encrypting the message, secret key, creating a digital signature. 

## Server.java
     Communicates with the client using TCP protocol and uses the RSA_main,java, AES_variant.java and Hash_md5.java for the decryption of the message, secret key, verifying the digital signature. 
/**
This code is part of a Network Security Project

Author : Rishabh Saxena
Roll no : 2019129
IIITDM JABALPUR

This class generates public key + private key pair for a user using the services of Extended_gcd.java & Prime_store.java.
It can generate keys in two ways :
    a-automatic creation
    b-user manually gives the parameters(p,q,e)[ the user can invoke the function returnD(long p,long q, long e)]

    

**/

import java.math.*;
class RSA_key_gen {

    //return the private key parameter 'd' based on values of p,q,e
    public long returnD(long p,long q, long e){
        long phi = (p-1)*(q-1);
        BigInteger biginteger1, biginteger2, result;
  
        
        biginteger1 = new BigInteger((e+""));
        biginteger2 = new BigInteger((phi+""));
        result= biginteger1.modInverse(biginteger2);
        //System.out.println(phi);
        long d= result.longValue();
        return d;
        
    }
    //returns the automatically created public key + private key parameters
    public long[][] generateKeyParameters() {
        Prime_store ob_prime=new Prime_store();
        Extended_gcd ob_gcd=new Extended_gcd();
        int[] prime_nums=ob_prime.giveTwoPrime();
        
        int p=prime_nums[0];
        int q=prime_nums[1];
        //System.out.println(p+" "+q);
        long n = p*q;
        //euler's totient phi
        long phi = (p-1)*(q-1);
        //find e
        long e;
        long[] gcd={};
        long[] gcdn={};
        for( e=2;e<phi;e++){
            gcd=ob_gcd.findGcd(e, phi);
            gcdn=ob_gcd.findGcd(e, n);
            if(gcd[0]==1 && gcdn[0]==1){
                break;
            }

        }
        
        //find inverse moduli
        RSA_key_gen obj=new RSA_key_gen();
        long d = obj.returnD(p, q, e);
        /*
        long[] public_key={e,n};
        long[] private_key={d,n};

       
        System.out.println("Public key {"+public_key[0]+","+public_key[1]+"}");
        System.out.println("Private key {"+private_key[0]+","+private_key[1]+"}");*/
        long[][] key_para={{e,n},
                            {d,n}};
        return key_para;
        



        


    }
}

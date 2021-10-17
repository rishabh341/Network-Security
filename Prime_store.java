 /**
This code is part of a Network Security Project

Author : Rishabh Saxena
Roll no : 2019129
IIITDM JABALPUR

This class stores the prime numbers upto 1000 using the Sieve of Eratothenes algorithm
The class has functions to return two random prime numbers

**/
 import java.util.*;
 class Prime_store {
    private int[] prime_numbers;
    private int n=1000;
    //constructor which stores the prime numbers using sieve of eratothenese
    public Prime_store(){
        //implementation of seive of eratothenes
        prime_numbers=new int[n+1];
        Arrays.fill(prime_numbers, 1);
        int p=2;
        while(p*p < n ){
            if(prime_numbers[p] == 1){
                for(int i= p*p ;i<=n;i+=p){
                    prime_numbers[i]=0;

                }
            }
            p++;
        }
    }   
    
    //function which return two random prime numbers which are >=threshold and <=limit
    public int[] giveTwoPrime( int threshold, int limit){
        //threshold = 500
        //the random prime numbers whose value is greater than threshold value
        int indexStart=(int)(Math.random()*(limit-threshold + 1)) + threshold;
        int p,q;
        for(int i=indexStart; ; i++){
            if(prime_numbers[i]==1){
                p=i;
                break;
            }
        }

        int indexStart_2=(int)(Math.random()*(limit-threshold + 1)) + threshold;
        
        for(int i=indexStart_2; ; i++){
            if(prime_numbers[i]==1){
                q=i;
                break;
            }
        }
        int[] result = {p,q};

        return result;
    }
    //function which return two random prime numbers which are >=500 and <=1000
    public int[] giveTwoPrime( ){
        int threshold = 500;
        //the random prime numbers whose value is greater than threshold value
        int indexStart=(int)(Math.random()*(n-threshold + 1)) + threshold;
        int p,q;
        for(int i=indexStart; ; i++){
            if(prime_numbers[i]==1){
                p=i;
                break;
            }
        }

        int indexStart_2=(int)(Math.random()*(n-threshold + 1)) + threshold;
        
        for(int i=indexStart_2; ; i++){
            if(prime_numbers[i]==1){
                q=i;
                break;
            }
        }
        int[] result = {p,q};

        return result;
    }
    
   
}

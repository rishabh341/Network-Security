/*
This code is part of a Network Security Project

Author : Rishabh Saxena
Roll no : 2019129
IIITDM JABALPUR

This class calulates the GCD of two numbers using the extended_gcd algorithm

*/

class Extended_gcd{
    /*
    takes two numbers and return the array with {gcd, s, t}(values of extended gcd)
    */
    public long[] findGcd(long a,long b){
        long s1=1;
        long s2=0;
        long t1=0;
        long t2=1;
        long r1=a;
        long r2=b;
        long r=0;
        long s=0;
        long t=0;
        long q=0;
        while(r2>0){
            q=r1/r2;
            r=r1-q*r2;

            s=s1-q*s2;
            t=t1-q*t2;
            //uncomment for intermediate values
            /*String intermediate_values= String.join("\t\t",String.valueOf(q), String.valueOf(r1),String.valueOf(r2),String.valueOf(r),String.valueOf(s1),String.valueOf(s2),String.valueOf(s),String.valueOf(t1),String.valueOf(t2),String.valueOf(t));
            System.out.println(intermediate_values);*/

            r1=r2;
            r2=r;

            s1=s2;
            s2=s;

            t1=t2;
            t2=t;
        }
       
        long[] result= {r1,s,t};
        return result;

        


    }
   
}

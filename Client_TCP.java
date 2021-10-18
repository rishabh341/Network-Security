/**
This code is part of a Network Security Project

Author : Rishabh Saxena
Roll no : 2019129
IIITDM JABALPUR

**/
import java.net.*;
import java.io.*;
class Client_TCP{
 public static void main(String[ ] args){

   
    
 try{
            System.out.println("This application is made by Rishabh Saxena 2019129 under a Network Security Project");
            //network configuration
            InetAddress acceptorHost = InetAddress.getByName("127.0.0.1");
            int serverPortNum = 9876;
            Socket clientSocket = new Socket(acceptorHost, serverPortNum);
            //setting communication stream for server-communication
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream( )));
            BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
            //take input from server
            System.out.println(br.readLine( ));
            PrintStream ps = new PrintStream(clientSocket.getOutputStream( ));
            System.out.print("Give me your message to send to server : ");
            String message_client=br2.readLine();
            System.out.println("Enter Secret Key");
            int key_client = Integer.parseInt(br2.readLine());

            //RSA KEY GENERATION
            RSA_main ob_rsa_client=new RSA_main();
            System.out.print("Want set RSA keys manually[Not recommended](Y/n) : ");
            char input_manual_keys = br2.readLine().charAt(0);
            if(input_manual_keys == 'Y' || input_manual_keys == 'y'){
               System.out.println("Enter public key parameters(p,q,e) : ");
               long p=Long.parseLong(br2.readLine());
               long q=Long.parseLong(br2.readLine());
               long e=Long.parseLong(br2.readLine());
              
               ob_rsa_client.setParameters(p, q, e);
           
            }
            System.out.println("Your RSA keys have been generated");
            System.out.println("Public key {e,n} = "+"{"+ob_rsa_client.e+","+ob_rsa_client.n+"}");
            System.out.print("Want to see your private key(Y/n) : ");
            char input_private_key_See = br2.readLine().charAt(0);
            if(input_private_key_See == 'Y' || input_private_key_See == 'y'){
               System.out.println("Private key {d,n} = "+"{"+ob_rsa_client.getPrivateKey()+","+ob_rsa_client.n+"}");
           
            }
           
           

   
            //Hashing the message
            Hash_md5 ob_hash_md5_client = new Hash_md5();
            String hash_client_message_digest = ob_hash_md5_client.getMd5(message_client);
            
            //encrypting hash message with client private key
            String rsa_encrypt_hash_client = ob_rsa_client.Encipher(hash_client_message_digest, ob_rsa_client.getPrivateKey(), ob_rsa_client.n);
            String client_signature = rsa_encrypt_hash_client;
            
    

            
            //Exchange public keys

            //server public key
            //System.out.println("Get server public key");
            int e_public_key_server = Integer.parseInt(br.readLine());
            int n_public_key_server = Integer.parseInt(br.readLine());
            int[] server_key_public ={e_public_key_server, n_public_key_server};
            /*for(int i=0;i<=3;i++){
               System.out.println(".");
               Thread.sleep(500);
            }*/
            ps.println((""+ob_rsa_client.e));
            ps.flush( );
            ps.println((""+ob_rsa_client.n));
            ps.flush( );
           // System.out.println("Keys exchanged");

            System.out.println();
            int key_test = key_client;

            //Message to AES
            AES_variant ob_aes_client=new AES_variant();
            //int key_test = 0b1010011100111011;
            String encrypted_secret_key_client = ob_rsa_client.Encipher((""+key_test), server_key_public[0], server_key_public[1]);
            System.out.println("encrypted secret key = "+ encrypted_secret_key_client);
            System.out.println();
           
            
            String ciphertext_aes_client = ob_aes_client.EncryptAES(message_client, key_test);
            

           
           
            System.out.println("Ciphertext of message(Simplified AES)= "+ciphertext_aes_client);
            System.out.println();
            System.out.println("Hash client message(digest) = "+hash_client_message_digest);
            System.out.println();
            System.out.println("Client digital signature = "+client_signature);
            System.out.println();

   

    //send encrypted_secret_key_client + ciphertext_aes_client + client_signature


            ps.println(ciphertext_aes_client);
            ps.flush( );
            ps.println(encrypted_secret_key_client);
            ps.flush( );
            ps.println(client_signature);
            ps.flush( );



            
            clientSocket.close( );
 }
 catch(Exception e){e.printStackTrace( );}
 }
 }
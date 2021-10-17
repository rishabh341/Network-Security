/**
This code is part of a Network Security Project

Author : Rishabh Saxena
Roll no : 2019129
IIITDM JABALPUR

**/
import java.net.*;
import java.io.*;
class Server1{
 public static void main(String[ ] args){
 try{
        String message = "Welcome, you have been connected to a secure server";
        int serverPortNum = 9876;
        ServerSocket connectionSocket = new ServerSocket(serverPortNum);
        RSA_main ob_rsa_server=new RSA_main();
        while (true){
        Socket dataSocket = connectionSocket.accept( );
        System.out.println("Connection request received from server\n\n");
        PrintStream socketOutput = new PrintStream(dataSocket.getOutputStream( ));
        socketOutput.println(message);
        socketOutput.flush( );
        BufferedReader br = new BufferedReader(new InputStreamReader(dataSocket.getInputStream( )));
        System.out.println("Public key {e,n} = "+"{"+ob_rsa_server.e+","+ob_rsa_server.n+"}");
        
            

            //Key exchange
            //send key
            socketOutput.println((""+ob_rsa_server.e));
            socketOutput.flush( );
            socketOutput.println((""+ob_rsa_server.n));
            socketOutput.flush( );
            System.out.println("Exchanging public keys");
            for(int i=0;i<=3;i++){
                System.out.println(".");
                Thread.sleep(500);
             }
            //receive key
            int e_public_key_client = Integer.parseInt(br.readLine());
            int n_public_key_client = Integer.parseInt(br.readLine());
            int[] client_key_public ={e_public_key_client, n_public_key_client};
            
            System.out.println("Done");
            System.out.print("Recieving information(ciphertext,encrypted secret key,client digital signature)");

            //receiving information
            String ciphertext_aes_server = br.readLine();
            String encrypted_secret_key_server = br.readLine();
            String client_signature = br.readLine();


            for(int i=0;i<=6;i++){
                System.out.print(".");
                Thread.sleep(500);
             }
             System.out.println();
             System.out.println("Received");
             //received information
             System.out.println("\n\n");

             System.out.println("Received ciphertext : "+ciphertext_aes_server);
             System.out.println("\n");

             System.out.println("Received encrypted secret key : "+encrypted_secret_key_server);
             System.out.println("\n");

             System.out.println("Received client signature : "+client_signature);
             System.out.println("\n");

             String secret_key_server_string = ob_rsa_server.Decipher(encrypted_secret_key_server);
             int secret_key_server = Integer.parseInt(secret_key_server_string);
             System.out.println("Decrypted secret key : "+secret_key_server);
             System.out.println("\n");
             // give ciphertext to AES with secret key
            AES_variant ob_aes_server = new AES_variant();
            String plaintext_aes_server = ob_aes_server.DecryptAES(ciphertext_aes_server, secret_key_server);


            //get message
            String plainText_aes_server_string = plaintext_aes_server ;
            System.out.println("Message received  = " + plainText_aes_server_string);
            System.out.println("\n");
            //authentication
            Hash_md5 ob_hash_md5_server = new Hash_md5();
            String hash_server_message_digest = ob_hash_md5_server.getMd5(plainText_aes_server_string);

            System.out.println("Hash digest = "+hash_server_message_digest);
            System.out.println("\n");
            String server_digest_rsa_client = ob_rsa_server.Decipher(client_signature, client_key_public[0], client_key_public[1]);
            System.out.println("rsa client public key hash digest = "+server_digest_rsa_client);
            System.out.println("\n");
            System.out.println("rsa server received digest = "+client_signature);
            System.out.println("\n");

            //compare authenticity
            boolean isAuthentic =  server_digest_rsa_client.equals(hash_server_message_digest);
            if(isAuthentic){
                System.out.println("This message has been authenticated");
            }
            else{
                System.out.println("No authenticity of this message is guaranteed");
            }
            System.out.println(server_digest_rsa_client.equals(hash_server_message_digest));




            



        
        
        dataSocket.close();
        
        
    }
    //connectionSocket.close();
 }
        catch(Exception e){e.printStackTrace( );}
    }
 }
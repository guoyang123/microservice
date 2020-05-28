package com.neuedu;

import com.neuedu.common.ServerResponse;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        ServerResponse serverResponse= ServerResponse.createServerResponseByFail(10);
        System.out.println(serverResponse.isSuccess());

    }
}

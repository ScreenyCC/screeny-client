package screeny.protocol.util;

import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PacketUtil {
    private final static Charset CHARSET = Charset.forName( "UTF-8" );

    public static void writeString( String string, ByteBuf byteBuf ) {
        byte[] stringBytes = string.getBytes( CHARSET );
        byteBuf.writeInt( stringBytes.length );
        byteBuf.writeBytes( stringBytes );
    }

    public static String readString( ByteBuf byteBuf ) {
        byte[] stringBytes = new byte[byteBuf.readInt()];
        byteBuf.readBytes( stringBytes );
        return new String( stringBytes, CHARSET );
    }

    public static void writeStringList( List<String> stringList, ByteBuf byteBuf ) {
        byteBuf.writeInt( stringList.size() );
        for ( String string : new ArrayList<>( stringList ) ) {
            writeString( string, byteBuf );
        }
    }

    public static List<String> readStringList( ByteBuf byteBuf ) {
        List<String> stringList = new ArrayList<>();
        int length = byteBuf.readInt();
        for ( int i = 0; i < length; i++ ) {
            stringList.add( readString( byteBuf ) );
        }
        return stringList;
    }

    public static void writeStringArray( String[] stringArray, ByteBuf byteBuf ) {
        byteBuf.writeInt( stringArray.length );
        for ( String string : stringArray ) {
            writeString( string, byteBuf );
        }
    }

    public static String[] readStringArray( ByteBuf byteBuf ) {
        int length = byteBuf.readInt();
        String[] stringArray = new String[length];
        for ( int i = 0; i < length; i++ ) {
            stringArray[i] = readString( byteBuf );
        }
        return stringArray;
    }

    public static Map<String, String> readStringHashMap( ByteBuf byteBuf ) {
        HashMap<String, String> hashMap = new HashMap<>();
        int size = byteBuf.readInt();
        for ( int i = 0; i < size; i++ ) {
            hashMap.put( readString( byteBuf ), readString( byteBuf ) );
        }
        return hashMap;
    }

    public static void writeStringHashMap( Map<String, String> map, ByteBuf byteBuf ) {
        byteBuf.writeInt( map.size() );
        for ( String key : map.keySet() ) {
            writeString( key, byteBuf );
            writeString( map.get( key ), byteBuf );
        }
    }

    public static Map<String, Integer> readIntegerHashMap( ByteBuf byteBuf ) {
        HashMap<String, Integer> hashMap = new HashMap<>();
        int size = byteBuf.readInt();
        for ( int i = 0; i < size; i++ ) {
            hashMap.put( readString( byteBuf ), byteBuf.readInt() );
        }
        return hashMap;
    }

    public static void writeIntegerHashMap( Map<String, Integer> map, ByteBuf byteBuf ) {
        byteBuf.writeInt( map.size() );
        for ( String key : map.keySet() ) {
            writeString( key, byteBuf );
            byteBuf.writeInt( map.get( key ) );
        }
    }
}

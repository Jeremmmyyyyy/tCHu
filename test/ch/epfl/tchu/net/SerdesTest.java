package ch.epfl.tchu.net;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SerdesTest {

    @Test
    void SerdeTest(){
        System.out.println(Serdes.LIST_STRING_SERDE.serialize(List.of("je", "m'appelle", "jeremy")));
    }

}
